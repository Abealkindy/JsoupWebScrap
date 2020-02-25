package com.example.myapplication.activities.mangapage.manga_detail_mvp

import android.util.Log

import com.example.myapplication.models.mangamodels.DetailMangaModel
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup
import org.jsoup.internal.StringUtil

import java.io.IOException
import java.net.HttpCookie
import java.util.ArrayList

class MangaDetailPresenter(private val mangaDetailInterface: MangaDetailInterface) {
    private val detailMangaModel = DetailMangaModel()

    fun getDetailMangaData(detailPageURL: String) {
        val cf = Cloudflare(detailPageURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToJsoup(detailPageURL, cookies)
                }
            }

            override fun onFail() {
                mangaDetailInterface.onGetDetailDataFailed()
            }
        })
    }

    private fun passToJsoup(newUrl: String, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup.connect(newUrl).userAgent("Mozilla/5.0").cookies(cookies).execute()
            val document = jsoupResponse.parse()

            //get title
            val getTitle = document.select("h1[itemprop=headline]")
            detailMangaModel.mangaTitle = getTitle.text().substring(0, getTitle.text().indexOf(" Bahasa Indonesia"))

            //get thumb
            val getThumb = document.getElementsByTag("img")
            var mangaThumbnailBackground = getThumb.eachAttr("src")[1]
            if (StringUtil.isBlank(mangaThumbnailBackground)) {
                mangaThumbnailBackground = getThumb.eachAttr("data-src")[1]
            }
            if (!mangaThumbnailBackground.contains("https")) {
                mangaThumbnailBackground = "https:$mangaThumbnailBackground"
            } else if (!mangaThumbnailBackground.contains("http")) {
                mangaThumbnailBackground = "http:$mangaThumbnailBackground"
            }
            detailMangaModel.mangaThumb = mangaThumbnailBackground

            //get Synopsis
            val getSynopsis = document.getElementsByTag("p")
            detailMangaModel.mangaSynopsis = getSynopsis.text()

            //get All chapter data
            val detailAllChapterDatasList = ArrayList<DetailMangaModel.DetailAllChapterDatas>()
            val getAllMangaChapters = document.getElementsByTag("li")
            for (element in getAllMangaChapters) {
                val allChapterDatas = DetailMangaModel().DetailAllChapterDatas()
                val chapterReleaseTime = element.getElementsByClass("rightoff").text()
                val chapterTitle = element.select("a[href^=https://komikcast.com/chapter/]").text()
                val chapterURL = element.select("a[href^=https://komikcast.com/chapter/]").attr("href")
                allChapterDatas.chapterReleaseTime = chapterReleaseTime
                allChapterDatas.chapterTitle = chapterTitle
                allChapterDatas.chapterURL = chapterURL
                detailAllChapterDatasList.add(allChapterDatas)
            }
            val afterCut = ArrayList(detailAllChapterDatasList.subList(7, detailAllChapterDatasList.size - 5))


            //get genre data
            val genresList = ArrayList<DetailMangaModel.DetailMangaGenres>()
            val getGenres = document.select("a[rel=tag]")
            for (element in getGenres) {
                val genreTitle = element.text()
                val genreURL = element.attr("href")
                val mangaGenres = DetailMangaModel().DetailMangaGenres()
                mangaGenres.genreTitle = genreTitle
                mangaGenres.genreURL = genreURL
                genresList.add(mangaGenres)
            }
            val genreCut = ArrayList(genresList.subList(0, genresList.size - 1))

            //get Updated on
            val getLatestUpdate = document.select("time[itemprop=dateModified]")
            detailMangaModel.lastMangaUpdateDate = getLatestUpdate.text()

            //get Other name
            val getOtherName = document.getElementsByClass("alter")
            detailMangaModel.otherNames = getOtherName.text()

            //get Author and others
            val getAuthor = document.getElementsByTag("span")

            for (position in 0 until getAuthor.eachText().size) {

                if (getAuthor.eachText()[position].contains("Author")) {
                    if (getAuthor.eachText()[position].length < 8) {
                        detailMangaModel.mangaAuthor = null
                    } else {
                        detailMangaModel.mangaAuthor = getAuthor.eachText()[position].substring(getAuthor.eachText()[position].indexOf("Author:") + 7)
                    }
                }

                if (getAuthor.eachText()[position].contains("Released")) {
                    if (getAuthor.eachText()[position].length < 10) {
                        detailMangaModel.firstUpdateYear = null
                    } else {
                        detailMangaModel.firstUpdateYear = getAuthor.eachText()[position].substring(getAuthor.eachText()[position].indexOf("Released:") + 9)
                    }
                }

                if (getAuthor.eachText()[position].contains("Total Chapter")) {
                    if (getAuthor.eachText()[position].length < 15) {
                        detailMangaModel.totalMangaChapter = null
                    } else {
                        detailMangaModel.totalMangaChapter = getAuthor.eachText()[position].substring(getAuthor.eachText()[position].indexOf("Total Chapter:") + 14)
                    }
                }

            }

            val getStatus = getAuthor.eachText()[13].substring(8)
            detailMangaModel.mangaStatus = getStatus

            detailMangaModel.mangaType = getAuthor.eachText()[16].substring(6)

            val getRating = document.getElementsByClass("rating")
            detailMangaModel.mangaRating = getRating.eachText()[0].substring(7, getRating.eachText()[0].length - 10)
            //store data from JSOUP
            mangaDetailInterface.onGetGenreSuccess(genreCut)
            mangaDetailInterface.onGetAllChapterSuccess(afterCut)
            mangaDetailInterface.onGetDetailDataSuccess(detailMangaModel)
        } catch (e: IOException) {
            e.printStackTrace()
            mangaDetailInterface.onGetAllChapterFailed()
            mangaDetailInterface.onGetGenreFailed()
            mangaDetailInterface.onGetDetailDataFailed()
        }

    }
}
