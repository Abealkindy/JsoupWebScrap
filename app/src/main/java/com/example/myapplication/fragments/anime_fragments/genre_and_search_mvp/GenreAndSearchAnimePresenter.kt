package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp

import android.util.Log

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel
import com.google.gson.Gson
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup
import java.net.HttpCookie
import java.util.ArrayList

class GenreAndSearchAnimePresenter(private val genreAndSearchAnimeInterface: GenreAndSearchAnimeInterface) {

    fun getGenreAndSearchData(genreAndSearchURL: String) {
        val cf = Cloudflare(genreAndSearchURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToRetrofit(newUrl, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToRetrofit(genreAndSearchURL, cookies)
                }
            }

            override fun onFail() {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed()
            }
        })
    }

    fun getOnlyGenreData(genreURL: String) {
        val cf = Cloudflare(genreURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToRetrofitGenre(newUrl, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToRetrofitGenre(genreURL, cookies)
                }
            }

            override fun onFail() {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed()
            }
        })
    }

    private fun passToRetrofitGenre(genrePageURL: String, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup
                    .connect(genrePageURL)
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute()
            val doc = jsoupResponse.parse()
            val genreResultList = ArrayList<AnimeGenreAndSearchResultModel.AnimeGenreResult>()
            genreResultList.clear()
            val getGenreData = doc.select("a[href^=https://animeindo.to/genres/]")
            for (element in getGenreData) {
                val genreURL = element.attr("href")
                val genreTitle = element.text()
                val genreResult = AnimeGenreAndSearchResultModel().animeGenreResults
                genreResult!!.genreTitle = genreTitle
                genreResult.genreURL = genreURL
                genreResultList.add(genreResult)
            }
            genreAndSearchAnimeInterface.onGetOnlyGenreDataSuccess(genreResultList)
        } catch (e: Exception) {
            e.printStackTrace()
            genreAndSearchAnimeInterface.onGetOnlyGenreDataFailed()
        }

    }

    private fun passToRetrofit(genreAndSearchURL: String, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup
                    .connect(genreAndSearchURL)
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute()
            val doc = jsoupResponse.parse()
            val animeGenreAndSearchResultModelList = ArrayList<AnimeGenreAndSearchResultModel.AnimeSearchResult>()
            val getListData = doc.getElementsByClass("col-6 col-md-4 col-lg-3 col-wd-per5 col-xl-per5 mb40")
            for (element in getListData) {
                val detailURL = element.select("a[href^=https://animeindo.to/anime/]").attr("href")
                var thumbURL = element.getElementsByClass("episode-ratio background-cover").attr("style").substring(element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf("https://"), element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf(")"))
                if (thumbURL.contains("'")) {
                    thumbURL = thumbURL.replace("'", "")
                }
                val animeTitle = element.getElementsByTag("h4").text()
                var animeStatus = ""
                var animeType: String
                if (element.getElementsByClass("text-h6").eachText().size < 2) {
                    animeType = element.getElementsByClass("text-h6").eachText()[0]
                } else {
                    animeStatus = element.getElementsByClass("text-h6").eachText()[0]
                    animeType = element.getElementsByClass("text-h6").eachText()[1]
                }

                val searchResult = AnimeGenreAndSearchResultModel().animeSearchResults
                searchResult!!.animeDetailURL = detailURL
                searchResult.animeThumb = thumbURL
                searchResult.animeTitle = animeTitle
                searchResult.animeStatus = animeStatus
                searchResult.animeType = animeType
                animeGenreAndSearchResultModelList.add(searchResult)
                Log.e("GENRE AND SEARCH", Gson().toJson(animeGenreAndSearchResultModelList))
            }
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataSuccess(animeGenreAndSearchResultModelList)
        } catch (e: Exception) {
            e.printStackTrace()
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed()
        }

    }
}
