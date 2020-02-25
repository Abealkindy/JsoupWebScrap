package com.example.myapplication.activities.animepage.anime_detail_mvp

import android.util.Log

import com.example.myapplication.models.mangamodels.DetailMangaModel
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup

import java.io.IOException
import java.net.HttpCookie
import java.util.ArrayList

class AnimeDetailPresenter(private val detailInterface: AnimeDetailInterface) {
    private val detailMangaModel = DetailMangaModel()

    fun getAnimeDetailContent(getAnimeDetailURL: String) {
        val cf = Cloudflare(getAnimeDetailURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToJsoup(getAnimeDetailURL, cookies)
                }
            }

            override fun onFail() {
                detailInterface.onGetDetailDataFailed()
            }
        })

    }

    private fun passToJsoup(newUrl: String, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup.connect(newUrl).userAgent("Mozilla/5.0").cookies(cookies).execute()
            val document = jsoupResponse.parse()

            //get synopsis
            val getSynopsis = document.getElementsByTag("p")
            if (getSynopsis.eachText().size < 2) {
                detailMangaModel.mangaSynopsis = null
            } else {
                detailMangaModel.mangaSynopsis = getSynopsis.eachText()[0]
            }

            //get Other name
            val getOtherName = document.getElementsByClass("text-h3")
            if (getOtherName.eachText().size < 7) {
                detailMangaModel.otherNames = null
                detailMangaModel.totalMangaChapter = getOtherName.eachText()[1].substring(0, getOtherName.eachText()[1].length - 8)
                detailMangaModel.lastMangaUpdateDate = getOtherName.eachText()[2].replace(" per", "/").replace(" episode", "episode")
                detailMangaModel.firstUpdateYear = getOtherName.eachText()[3].substring(12)
                if (getOtherName.eachText()[4].length < 7) {
                    detailMangaModel.mangaAuthor = null
                } else {
                    detailMangaModel.mangaAuthor = getOtherName.eachText()[4].substring(7)
                }
            } else {
                if (getOtherName.eachText()[0] == null) {
                    detailMangaModel.otherNames = null
                } else {
                    detailMangaModel.otherNames = getOtherName.eachText()[0]
                }
                detailMangaModel.totalMangaChapter = getOtherName.eachText()[3].substring(0, getOtherName.eachText()[3].length - 8)
                detailMangaModel.lastMangaUpdateDate = getOtherName.eachText()[4].replace(" per", "/").replace(" episode", "episode")
                detailMangaModel.firstUpdateYear = getOtherName.eachText()[5].substring(12)
                if (getOtherName.eachText()[6].length < 7) {
                    detailMangaModel.mangaAuthor = "-"
                } else {
                    detailMangaModel.mangaAuthor = getOtherName.eachText()[6].substring(7)
                }
            }

            //get Genre
            val getGenre = document.getElementsByTag("li")
            val detailGenresList = ArrayList<DetailMangaModel.DetailMangaGenres>()
            for (element in getGenre) {
                val detailGenres = DetailMangaModel.DetailMangaGenres()
                val getGenreURL = element.select("a[href^=https://animeindo.to/genres/]").attr("href")
                val getGenreTitle = element.select("a[href^=https://animeindo.to/genres/]").text()
                detailGenres.genreURL = getGenreURL
                detailGenres.genreTitle = getGenreTitle
                detailGenresList.add(detailGenres)
            }
            val detailGenresListCut = ArrayList(detailGenresList.subList(10, detailGenresList.size - 3))

            //get All episodes
            val getAllEpisodes = document.getElementsByClass("col-12 col-sm-6 mb10")
            val allEpisodeDatasList = ArrayList<DetailMangaModel.DetailAllChapterDatas>()
            for (element in getAllEpisodes) {
                val allEpisodeDatas = DetailMangaModel.DetailAllChapterDatas()
                val episodeURL = element.getElementsByTag("a").attr("href")
                var episodeTitle = element.getElementsByTag("a").text()
                allEpisodeDatas.chapterURL = episodeURL
                if (episodeTitle.contains("Episode")) {
                    episodeTitle = episodeTitle.substring(episodeTitle.indexOf("Episode"))
                } else {
                    Log.e("CUT?", "BIG NO!")
                }
                allEpisodeDatas.chapterTitle = episodeTitle
                allEpisodeDatasList.add(allEpisodeDatas)
            }

            //get Rating
            val getRatingAnime = document.getElementsByClass("series-rating")
            val animeDetailRating = getRatingAnime.attr("style").substring(17, getRatingAnime.attr("style").indexOf("%"))
            detailMangaModel.mangaRating = animeDetailRating

            //store data from JSOUP
            detailInterface.onGetGenreSuccess(detailGenresListCut)
            detailInterface.onGetAllEpisodeSuccess(allEpisodeDatasList)
            detailInterface.onGetDetailDataSuccess(detailMangaModel)
        } catch (e: IOException) {
            e.printStackTrace()
            detailInterface.onGetAllEpisodeFailed()
            detailInterface.onGetGenreFailed()
            detailInterface.onGetDetailDataFailed()
        }

    }

}
