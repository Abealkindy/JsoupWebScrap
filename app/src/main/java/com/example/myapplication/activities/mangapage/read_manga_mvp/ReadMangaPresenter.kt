package com.example.myapplication.activities.mangapage.read_manga_mvp

import android.annotation.SuppressLint
import android.util.Log

import com.example.myapplication.models.mangamodels.ReadMangaModel
import com.example.myapplication.models.mangamodels.ReadMangaModel.AllChapterDatas
import com.google.gson.Gson
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import org.jsoup.Jsoup

import java.io.IOException
import java.net.HttpCookie
import java.util.ArrayList
import java.util.LinkedHashSet

class ReadMangaPresenter(private val readMangaInterface: ReadMangaInterface) {
    private val readMangaModel = ReadMangaModel()
    private val allChapterDatasList = ArrayList<AllChapterDatas>()
    private lateinit var allChapterDatas: List<AllChapterDatas>

    fun getMangaContent(contentURL: String?) {
        val cf = Cloudflare(contentURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                val cookies = Cloudflare.List2Map(cookieList)
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToJsoup(contentURL, cookies)
                }
            }

            override fun onFail() {
                readMangaInterface.onGetMangaContentDataFailed()
            }
        })
    }

    @SuppressLint("LongLogTag")
    private fun passToJsoup(newUrl: String?, cookies: Map<String, String>) {
        try {
            val jsoupResponse = Jsoup.connect(newUrl).userAgent("Mozilla/5.0").cookies(cookies).execute()
            val doc = jsoupResponse.parse()
            //get chapter title
            val getChapterTitle = doc.getElementsByTag("h1")
            var chapterTitle = getChapterTitle.text()
            if (chapterTitle.contains(" Bahasa")) {
                chapterTitle = chapterTitle.substring(0, chapterTitle.length - 17)
            }
            readMangaModel.chapterTitle = chapterTitle

            //get all chapter data
            val getAllChapterDatas = doc.select("option[value^=https://komikcast.com/chapter/]")
            if (allChapterDatasList.isNotEmpty()) {
                allChapterDatasList.clear()
            }
            for (element in getAllChapterDatas) {
                val allChapterTitles = element.getElementsContainingOwnText("Chapter").text()
                val allChapterURLs = element.absUrl("value")
                val chapterDatas = AllChapterDatas()
                chapterDatas.chapterTitle = allChapterTitles
                chapterDatas.chapterUrl = allChapterURLs
                allChapterDatasList.add(chapterDatas)
            }
            allChapterDatas = removeDuplicates(allChapterDatasList)

            //get previous chapter URL
            val getPreviousChapterURL = doc.select("a[rel=prev]")
            if (getPreviousChapterURL == null || getPreviousChapterURL.isEmpty()) {
                readMangaModel.previousMangaURL = null
            } else {
                val prevElement = getPreviousChapterURL[0]
                val previousChapterUrl = prevElement.absUrl("href")
                readMangaModel.previousMangaURL = previousChapterUrl
            }

            //get next chapter URL
            val getNextChapterURL = doc.select("a[rel=next]")
            if (getNextChapterURL == null || getNextChapterURL.isEmpty()) {
                readMangaModel.nextMangaURL = null
            } else {
                val nextElement = getNextChapterURL[0]
                val nextChapButtonterUrl = nextElement.absUrl("href")
                readMangaModel.nextMangaURL = nextChapButtonterUrl
            }

            //get manga image content
            val getMangaImageContentNewerSeries = doc.select("img[src^=https://cdn.komikcast.com/wp-content/]")
            val getMangaImageContentOlderSeries = doc.select("img[src^=https://i0.wp.com/lh3.googleusercontent.com/]")

            val getMangaImageContentOtherSeries = doc.select("img[src^=https://docs.google.com/uc?export=view]")
            val getMangaImageContentOtherAgainSeries = doc.select("img[src^=https://4.bp.blogspot.com/]")
            val getMangaImageContentOther1Series = doc.select("img[src^=https://3.bp.blogspot.com/]")
            val getMangaImageContentOther2Series = doc.select("img[src^=https://1.bp.blogspot.com/]")
            val getMangaImageContentOther3Series = doc.select("img[src^=https://2.bp.blogspot.com/]")
            if (readMangaModel.imageContent != null || readMangaModel.imageContent!!.isNotEmpty()) {
                readMangaModel.imageContent!!.clear()
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty()) && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())) {
                Log.e("getContentWithNewerandgetContentWithOlder", "null")
                for (element in getMangaImageContentOtherSeries!!) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries!!.isEmpty())) {
                for (element in getMangaImageContentOtherAgainSeries!!) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries == null || getMangaImageContentOtherSeries.isEmpty())
                    && (getMangaImageContentOtherAgainSeries!!.isEmpty())) {
                for (element in getMangaImageContentOther1Series!!) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries!!.isEmpty())
                    && (getMangaImageContentOtherAgainSeries!!.isEmpty())
                    && (getMangaImageContentOther1Series!!.isEmpty())) {
                for (element in getMangaImageContentOther2Series!!) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries!!.isEmpty())
                    && (getMangaImageContentOtherAgainSeries!!.isEmpty())
                    && (getMangaImageContentOther1Series!!.isEmpty())
                    && (getMangaImageContentOther2Series!!.isEmpty())) {
                for (element in getMangaImageContentOther3Series) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            }
            if (getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty()) {
                Log.e("getContentWithNewer", "null")
                for (element in getMangaImageContentOlderSeries!!) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            } else {
                Log.e("getContentWithNewer", "success")
                for (element in getMangaImageContentNewerSeries) {
                    val mangaContent = element.absUrl("src")
                    readMangaModel.imageContent!!.add(mangaContent)
                }
            }

            Log.e("mangaChapterContent", Gson().toJson(readMangaModel.imageContent))

            //get manga detail page URL
            val getMangaDetail = doc.select("a[href^=https://komikcast.com/komik/]")
            readMangaModel.mangaDetailURL = getMangaDetail.attr("href")

            //store data from JSOUP
            readMangaInterface.onGetMangaChaptersDataSuccess(allChapterDatas)
            readMangaInterface.onGetMangaContentDataSuccess(readMangaModel)
        } catch (e: IOException) {
            e.printStackTrace()
            readMangaInterface.onGetMangaContentDataFailed()
        }

    }

    private fun removeDuplicates(list: MutableList<AllChapterDatas>): List<AllChapterDatas> {

        // Create a new LinkedHashSet
        // Add the elements to set
        val set = LinkedHashSet(list)

        // Clear the list
        list.clear()

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set)

        // return the list
        return list
    }
}
