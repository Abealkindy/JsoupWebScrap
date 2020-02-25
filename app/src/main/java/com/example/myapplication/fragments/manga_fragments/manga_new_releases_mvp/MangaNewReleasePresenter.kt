package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp

import android.util.Log

import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel
import com.google.gson.Gson
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup
import org.jsoup.internal.StringUtil

import java.io.IOException
import java.net.HttpCookie
import java.util.ArrayList

class MangaNewReleasePresenter(private val newReleaseInterface: MangaNewReleaseInterface) {

    fun getNewReleasesMangaData(pageCount: Int, newReleasesURL: String, hitStatus: String) {
        val cf = Cloudflare(newReleasesURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToJsoup(pageCount, newUrl, hitStatus, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToJsoup(pageCount, newReleasesURL, hitStatus, cookies)
                }
            }

            override fun onFail() {
                newReleaseInterface.onGetNewReleasesDataFailed()
            }
        })
    }

    private fun passToJsoup(pageCount: Int, newReleasesURL: String, hitStatus: String, cookies: Map<String, String>) {

        try {
            val jsoupResponse = Jsoup
                    .connect("$newReleasesURL/page/$pageCount")
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute()
            val doc = jsoupResponse.parse()
            val newchaptercon = doc.getElementsByClass("utao")
            val mangaNewReleaseResultModelList = ArrayList<MangaNewReleaseResultModel>()
            for (el in newchaptercon) {
                val mangaType = el.getElementsByTag("ul").attr("class")
                var mangaThumbnailBackground = el.getElementsByTag("img").attr("data-src")

                if (StringUtil.isBlank(mangaThumbnailBackground)) {
                    mangaThumbnailBackground = el.getElementsByTag("img").attr("src")
                }

                if (!mangaThumbnailBackground.contains("https")) {
                    mangaThumbnailBackground = "https:$mangaThumbnailBackground"
                } else if (!mangaThumbnailBackground.contains("http")) {
                    mangaThumbnailBackground = "http:$mangaThumbnailBackground"
                }

                val mangaTitle = el.getElementsByTag("h3").text()
                val url = el.getElementsByTag("a").attr("href")
                val mangaStatusParameter = el.getElementsByClass("hot").text()
                val chapterReleaseTime = el.getElementsByTag("i").eachText()
                val chapterUrl = el.select("a[href^=https://komikcast.com/chapter/]").eachAttr("href")
                val chapterTitle = el.select("a[href^=https://komikcast.com/chapter/]").eachText()
                val mangaNewReleaseResultModel = MangaNewReleaseResultModel()
                val mangaDetailModel = MangaNewReleaseResultModel.LatestMangaDetailModel()
                if (!mangaStatusParameter.equals("Hot", ignoreCase = true) || StringUtil.isBlank(mangaStatusParameter)) {
                    mangaNewReleaseResultModel.isMangaStatus = false
                } else if (mangaStatusParameter.equals("Hot", ignoreCase = true)) {
                    mangaNewReleaseResultModel.isMangaStatus = true
                }
                mangaNewReleaseResultModel.mangaType = mangaType
                mangaNewReleaseResultModel.mangaTitle = mangaTitle
                mangaNewReleaseResultModel.mangaThumb = mangaThumbnailBackground
                if (url.startsWith("https://komikcast.com/komik/")) {
                    mangaNewReleaseResultModel.mangaDetailURL = url
                }
                val latestMangaDetailModelList = ArrayList<MangaNewReleaseResultModel.LatestMangaDetailModel>()
                mangaDetailModel.chapterReleaseTime = chapterReleaseTime
                mangaDetailModel.chapterTitle = chapterTitle
                mangaDetailModel.chapterURL = chapterUrl
                latestMangaDetailModelList.add(mangaDetailModel)
                mangaNewReleaseResultModel.latestMangaDetail = latestMangaDetailModelList
                mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel)
            }
            val mangaNewReleaseResultModelListAfterCut = ArrayList(mangaNewReleaseResultModelList.subList(9, mangaNewReleaseResultModelList.size))
            Log.e("resultBeforeCut", Gson().toJson(mangaNewReleaseResultModelList))
            Log.e("resultAfterCut", Gson().toJson(mangaNewReleaseResultModelListAfterCut))
            //store data from JSOUP
            newReleaseInterface.onGetNewReleasesDataSuccess(mangaNewReleaseResultModelListAfterCut, hitStatus)
        } catch (e: IOException) {
            e.printStackTrace()
            newReleaseInterface.onGetNewReleasesDataFailed()
        }

    }

}
