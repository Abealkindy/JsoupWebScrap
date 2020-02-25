package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp

import android.util.Log

import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel
import com.google.gson.Gson
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup

import java.net.HttpCookie
import java.util.ArrayList

class AnimeNewReleasesPresenter(private val newReleasesInterface: AnimeNewReleasesInterface) {

    fun getNewReleasesAnimeData(pageCount: Int, newReleasesURL: String, hitStatus: String) {
        val cf = Cloudflare(newReleasesURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToRetrofit(pageCount, newUrl, hitStatus, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToRetrofit(pageCount, newReleasesURL, hitStatus, cookies)
                }
            }

            override fun onFail() {
                newReleasesInterface.onGetNewReleasesDataFailed()
            }
        })
    }

    private fun passToRetrofit(pageCount: Int, newUrl: String, hitStatus: String, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup
                    .connect("$newUrl/page/$pageCount")
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute()
            val doc = jsoupResponse.parse()
            val newepisodecon = doc.getElementsByClass("col-6 col-sm-4 col-md-3 col-xl-per5 mb40")
            val animeNewReleaseResultModelList = ArrayList<AnimeNewReleaseResultModel>()

            for (el in newepisodecon) {
                var animeThumbnailBackground = el.getElementsByClass("episode-ratio background-cover").attr("style")
                if (animeThumbnailBackground.contains("'")) {
                    animeThumbnailBackground = animeThumbnailBackground.replace("'", "")
                }
                val thumbnailCut = animeThumbnailBackground.substring(animeThumbnailBackground.indexOf("https://"), animeThumbnailBackground.indexOf(")"))
                val animeEpisode = el.getElementsByTag("h3").text()
                val animeEpisodeNumber = el.getElementsByClass("episode-number").text()
                val animeStatusAndType = el.getElementsByClass("text-h6").eachText()
                var animeEpisodeStatus = ""
                val animeEpisodeType: String
                if (animeStatusAndType.size < 2) {
                    animeEpisodeType = el.getElementsByClass("text-h6").eachText()[0]
                } else {
                    animeEpisodeStatus = el.getElementsByClass("text-h6").eachText()[0]
                    animeEpisodeType = el.getElementsByClass("text-h6").eachText()[1]
                }
                val epsiodeURL = el.getElementsByTag("a").attr("href")

                val animeNewReleaseResultModel = AnimeNewReleaseResultModel()
                animeNewReleaseResultModel.animeEpisode = animeEpisode
                animeNewReleaseResultModel.episodeThumb = thumbnailCut
                animeNewReleaseResultModel.episodeURL = epsiodeURL
                animeNewReleaseResultModel.animeEpisodeNumber = animeEpisodeNumber
                animeNewReleaseResultModel.animeEpisodeType = animeEpisodeType
                animeNewReleaseResultModel.animeEpisodeStatus = animeEpisodeStatus
                animeNewReleaseResultModelList.add(animeNewReleaseResultModel)
            }
            val animeNewReleaseResultModelListAfterCut = ArrayList(animeNewReleaseResultModelList.subList(6, animeNewReleaseResultModelList.size - 1))
            Log.e("resultBeforeCut", Gson().toJson(animeNewReleaseResultModelList))
            Log.e("resultAfterCut", Gson().toJson(animeNewReleaseResultModelListAfterCut))
            newReleasesInterface.onGetNewReleasesDataSuccess(animeNewReleaseResultModelListAfterCut, hitStatus)
        } catch (e: Exception) {
            e.printStackTrace()
            newReleasesInterface.onGetNewReleasesDataFailed()
        }

    }
}
