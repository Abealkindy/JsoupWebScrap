package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp

import android.util.Log

import com.example.myapplication.models.mangamodels.DiscoverMangaModel
import com.google.gson.Gson
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup
import org.jsoup.internal.StringUtil

import java.io.IOException
import java.net.HttpCookie
import java.util.ArrayList

class DiscoverMangaPresenter(private val discoverMangaInterface: DiscoverMangaInterface) {

    fun getDiscoverOrSearchData(discoverOrSearchURL: String) {
        val cf = Cloudflare(discoverOrSearchURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToJsoup(discoverOrSearchURL, cookies)
                }
            }

            override fun onFail() {
                discoverMangaInterface.onGetDiscoverMangaDataFailed()
            }
        })
    }

    private fun passToJsoup(discoverOrSearchURL: String, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup
                    .connect(discoverOrSearchURL)
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute()
            val doc = jsoupResponse.parse()
            val newchaptercon = doc.getElementsByClass("bs")
            val mangaNewReleaseResultModelList = ArrayList<DiscoverMangaModel>()
            for (el in newchaptercon) {
                val mangaType = el.getElementsByAttributeValueContaining("class", "type ").text()
                var mangaThumbnailBackground = el.getElementsByTag("img").attr("data-src")
                if (StringUtil.isBlank(mangaThumbnailBackground)) {
                    mangaThumbnailBackground = el.getElementsByTag("img").attr("src")
                }
                if (!mangaThumbnailBackground.contains("https")) {
                    mangaThumbnailBackground = "https:$mangaThumbnailBackground"
                } else if (!mangaThumbnailBackground.contains("http")) {
                    mangaThumbnailBackground = "http:$mangaThumbnailBackground"
                }

                val mangaTitle = el.getElementsByClass("tt").text()
                val chapterRating = el.getElementsByTag("i").text()
                val chapterUrl = el.select("a[href^=https://komikcast.com/chapter/]").attr("href")
                val mangaUrl = el.select("a[href^=https://komikcast.com/komik/]").attr("href")
                val chapterText = el.select("a[href^=https://komikcast.com/chapter/]").text()
                val completedStatusParameter = el.getElementsByClass("status Completed").text()
                val mangaNewReleaseResultModel = DiscoverMangaModel()
                mangaNewReleaseResultModel.isMangaStatus = !(StringUtil.isBlank(completedStatusParameter) || !completedStatusParameter.equals("Completed", ignoreCase = true))
                mangaNewReleaseResultModel.mangaURL = mangaUrl
                mangaNewReleaseResultModel.mangaType = mangaType
                mangaNewReleaseResultModel.mangaTitle = mangaTitle
                mangaNewReleaseResultModel.mangaThumb = mangaThumbnailBackground
                mangaNewReleaseResultModel.mangaRating = chapterRating
                mangaNewReleaseResultModel.mangaLatestChapter = chapterUrl
                mangaNewReleaseResultModel.mangaLatestChapterText = chapterText
                mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel)
            }
            Log.e("resultBeforeCutDiscover", Gson().toJson(mangaNewReleaseResultModelList))
            //store data from JSOUP
            discoverMangaInterface.onGetDiscoverMangaDataSuccess(mangaNewReleaseResultModelList)
        } catch (e: IOException) {
            e.printStackTrace()
            discoverMangaInterface.onGetDiscoverMangaDataFailed()
        }

    }
}
