package com.example.myapplication.activities.animepage.watch_anime_mvp

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import com.example.myapplication.activities.animepage.anime_detail_mvp.AnimeDetailActivity
import com.example.myapplication.R
import com.example.myapplication.models.animemodels.VideoStreamResultModel
import com.example.myapplication.databinding.ActivityWatchAnimeEpisodeBinding
import com.google.gson.Gson

import org.jsoup.Jsoup
import java.util.ArrayList

class WatchAnimeEpisodeActivity : AppCompatActivity(), WatchAnimeEpisodeInterface {

    private var animeEpisodeBinding: ActivityWatchAnimeEpisodeBinding? = null
    private val videoStreamResultModel = VideoStreamResultModel()
    private var progressDialog: ProgressDialog? = null
    private var nowEpisodeNumber: String? = null
    private var nextEpisodeNumber: String? = null
    private var previousEpisodeNumber: String? = null
    private var nextURL: String? = null
    private var prevURL: String? = null
    private var episodeTitle: String? = null
    private val episodePresenter = WatchAnimeEpisodePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animeEpisodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_anime_episode)
        initUI()
        initEvent()
    }

    private fun initEvent() {
        animeEpisodeBinding!!.animeInfoButton.setOnClickListener {
            if (videoStreamResultModel.animeDetailURL == null || videoStreamResultModel.episodeTitle == null) {
                Log.e("URL NULL", "YES")
            } else {
                val intent = Intent(this@WatchAnimeEpisodeActivity, AnimeDetailActivity::class.java)
                intent.putExtra("animeDetailURL", videoStreamResultModel.animeDetailURL)
                intent.putExtra("animeDetailTitle", videoStreamResultModel.episodeTitle)
                intent.putExtra("animeDetailType", videoStreamResultModel.animeType)
                intent.putExtra("animeDetailStatus", videoStreamResultModel.animeStatus)
                intent.putExtra("animeDetailThumb", videoStreamResultModel.animeThumb)
                startActivity(intent)
                finish()
            }
        }
        animeEpisodeBinding!!.prevEpisodeButton.setOnClickListener { getAnimeWatchData(prevURL!!) }
        animeEpisodeBinding!!.nextEpisodeButton.setOnClickListener { getAnimeWatchData(nextURL!!) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initUI() {
        progressDialog = ProgressDialog(this@WatchAnimeEpisodeActivity)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Be patient please onii-chan, it just take less than a minute :3")
        animeEpisodeBinding!!.webViewWatchAnime.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webView: WebView, request: WebResourceRequest): Boolean {
                webView.loadUrl(request.url.toString())
                return true
            }
        }
        animeEpisodeBinding!!.webViewWatchAnime.settings.javaScriptEnabled = true
        animeEpisodeBinding!!.webViewWatchAnime.webChromeClient = WebChromeClient()
        val episodeURL = intent.getStringExtra("animeEpisodeToWatch")
        episodeTitle = intent.getStringExtra("animeEpisodeTitle")
        val episodeType = intent.getStringExtra("animeEpisodeType")
        val episodeStatus = intent.getStringExtra("animeEpisodeStatus")
        val episodeThumb = intent.getStringExtra("animeEpisodeThumb")
        if (episodeURL != null && episodeTitle != null && episodeType != null) {
            if (episodeType.equals(resources.getString(R.string.series_string), ignoreCase = true) || episodeType.contains(resources.getString(R.string.series_string))) {
                animeEpisodeBinding!!.linearAbove.setBackgroundColor(resources.getColor(R.color.blue_series_color))
                animeEpisodeBinding!!.linearBelow.setBackgroundColor(resources.getColor(R.color.blue_series_color))
            } else if (episodeType.equals(resources.getString(R.string.ona_string), ignoreCase = true) || episodeType.contains(resources.getString(R.string.ona_string))) {
                animeEpisodeBinding!!.linearAbove.setBackgroundColor(resources.getColor(R.color.purple_series_color))
                animeEpisodeBinding!!.linearBelow.setBackgroundColor(resources.getColor(R.color.purple_series_color))
            } else if (episodeType.equals(resources.getString(R.string.movie_string), ignoreCase = true) || episodeType.contains(resources.getString(R.string.movie_string_lower))) {
                animeEpisodeBinding!!.linearAbove.setBackgroundColor(resources.getColor(R.color.green_series_color))
                animeEpisodeBinding!!.linearBelow.setBackgroundColor(resources.getColor(R.color.green_series_color))
                animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
                animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
            } else if (episodeType.equals(resources.getString(R.string.la_string), ignoreCase = true) || episodeType.contains(resources.getString(R.string.la_string))) {
                animeEpisodeBinding!!.linearAbove.setBackgroundColor(resources.getColor(R.color.red_series_color))
                animeEpisodeBinding!!.linearBelow.setBackgroundColor(resources.getColor(R.color.red_series_color))
            } else if (episodeType.equals(resources.getString(R.string.special_string), ignoreCase = true) || episodeType.contains(resources.getString(R.string.special_string_lower))) {
                animeEpisodeBinding!!.linearAbove.setBackgroundColor(resources.getColor(R.color.orange_series_color))
                animeEpisodeBinding!!.linearBelow.setBackgroundColor(resources.getColor(R.color.orange_series_color))
            } else if (episodeType.equals(resources.getString(R.string.ova_string), ignoreCase = true) || episodeType.contains(resources.getString(R.string.ova_string))) {
                animeEpisodeBinding!!.linearAbove.setBackgroundColor(resources.getColor(R.color.pink_series_color))
                animeEpisodeBinding!!.linearBelow.setBackgroundColor(resources.getColor(R.color.pink_series_color))
            }
            animeEpisodeBinding!!.textAnimeTitleWatch.text = episodeTitle
            videoStreamResultModel.episodeTitle = episodeTitle
            videoStreamResultModel.animeType = episodeType
            videoStreamResultModel.animeStatus = episodeStatus
            videoStreamResultModel.animeThumb = episodeThumb
            getAnimeWatchData(episodeURL)
        } else {
            Log.e("nowURL", "gak ada")
        }
    }

    private fun getAnimeWatchData(animeEpisodeToWatch: String) {
        progressDialog!!.show()
        val afterCut = animeEpisodeToWatch.substring(21)
        nowEpisodeNumber = afterCut.substring(afterCut.indexOf("episode-") + 8)
        if (nowEpisodeNumber!!.contains("-")) {
            nowEpisodeNumber!!.replace("-", ".")
        }
        episodePresenter.getEpisodeToWatchData(animeEpisodeToWatch)
    }

    private fun parseHtmlToViewableContent(result: String) {
        val doc = Jsoup.parse(result)

        //Anime Details URL settings
        val getElementsAnimeDetails = doc.select("a[href^=https://animeindo.to/anime/]")
        if (getElementsAnimeDetails.isEmpty()) {
            Log.e("VideoDetailNull?", "Ya")
        } else {
            val animeDetailsURL = getElementsAnimeDetails.attr("href")
            if (!animeDetailsURL.startsWith("https://animeindo.to/")) {
                Log.e("VideoresultURLError?", "Ya")
                videoStreamResultModel.animeDetailURL = null
            } else {
                videoStreamResultModel.animeDetailURL = animeDetailsURL
            }
        }
        episodeTitle = doc.getElementsByTag("h1").text()
        if (episodeTitle != null) {
            if (episodeTitle!!.contains("Subtitle")) {
                episodeTitle = episodeTitle!!.substring(0, episodeTitle!!.length - 19)
            } else {
                Log.e("CUT?", "OF COURSE NO!")
            }
        }
        animeEpisodeBinding!!.textAnimeTitleWatch.text = episodeTitle
        //get next and prev URL
        val getElementsNextAndPrevEpisode = doc.select("a[href^=https://animeindo.to/]")
        val nextAndPrevURL = ArrayList<String>()
        nextAndPrevURL.clear()
        for (position in getElementsNextAndPrevEpisode.indices) {
            val element = getElementsNextAndPrevEpisode[position]
            val nextandprevurlsingle = element.absUrl("href")
            if (!nextandprevurlsingle.startsWith("https://animeindo.to/anime/") && nextandprevurlsingle.contains("episode")) {
                nextAndPrevURL.add(nextandprevurlsingle)
            }
        }
        Log.e("nextAndPrevURLstring", Gson().toJson(nextAndPrevURL))
        val nextOrPrevURL: String
        val nextOrPrevEpisodeNumber: String
        if (nextAndPrevURL.isEmpty()) {
            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
        } else {
            if (nextAndPrevURL.size < 2) {
                nextOrPrevURL = nextAndPrevURL[0]
                nextOrPrevEpisodeNumber = nextOrPrevURL.substring(nextOrPrevURL.indexOf("episode-") + 8)
                if (nextOrPrevEpisodeNumber.contains("-") || nowEpisodeNumber!!.contains("-")) {
                    val nextOrPrevEpisodeNumberCut = nextOrPrevEpisodeNumber.replace("-", ".")
                    val nowEpisodeNumberCut = nowEpisodeNumber!!.replace("-", ".")
                    if (nowEpisodeNumberCut.endsWith(".tamat/")) {
                        val substring = nowEpisodeNumberCut.substring(0, nowEpisodeNumberCut.length - 6)
                        if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumberCut) < java.lang.Double.parseDouble(substring)) {
                            prevURL = nextAndPrevURL[0]
                            nextURL = null
                            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.VISIBLE
                            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
                        } else if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumberCut) > java.lang.Double.parseDouble(substring)) {
                            prevURL = null
                            nextURL = nextAndPrevURL[0]
                            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
                            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.VISIBLE
                        }
                    } else if (nowEpisodeNumberCut.endsWith("/")) {
                        if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumberCut) < java.lang.Double.parseDouble(nowEpisodeNumberCut.substring(0, nowEpisodeNumberCut.length - 1))) {
                            prevURL = nextAndPrevURL[0]
                            nextURL = null
                            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.VISIBLE
                            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
                        } else if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumberCut) > java.lang.Double.parseDouble(nowEpisodeNumberCut.substring(0, nowEpisodeNumberCut.length - 1))) {
                            prevURL = null
                            nextURL = nextAndPrevURL[0]
                            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
                            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.VISIBLE
                        }
                    } else {
                        if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumberCut) < java.lang.Double.parseDouble(nowEpisodeNumberCut)) {
                            prevURL = nextAndPrevURL[0]
                            nextURL = null
                            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.VISIBLE
                            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
                        } else if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumberCut) > java.lang.Double.parseDouble(nowEpisodeNumberCut)) {
                            prevURL = null
                            nextURL = nextAndPrevURL[0]
                            animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
                            animeEpisodeBinding!!.nextEpisodeButton.visibility = View.VISIBLE
                        }
                    }
                } else {
                    if (!nowEpisodeNumber!!.endsWith("special/") && !nowEpisodeNumber!!.endsWith("movie/") && !nowEpisodeNumber!!.endsWith("ona/") && !nowEpisodeNumber!!.endsWith("ova/")) {
                        if (nowEpisodeNumber!!.endsWith("/")) {
                            Log.e("WITH SLASH?", "yes")
                            Log.e("URL1", nextOrPrevEpisodeNumber)
                            Log.e("URL2", nowEpisodeNumber!!)
                            if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumber.substring(0, nextOrPrevEpisodeNumber.length - 1)) < java.lang.Double.parseDouble(nowEpisodeNumber!!.substring(0, nowEpisodeNumber!!.length - 1))) {
                                prevURL = nextAndPrevURL[0]
                                nextURL = null
                                animeEpisodeBinding!!.prevEpisodeButton.visibility = View.VISIBLE
                                animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
                            } else if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumber.substring(0, nextOrPrevEpisodeNumber.length - 1)) > java.lang.Double.parseDouble(nowEpisodeNumber!!.substring(0, nowEpisodeNumber!!.length - 1))) {
                                prevURL = null
                                nextURL = nextAndPrevURL[0]
                                animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
                                animeEpisodeBinding!!.nextEpisodeButton.visibility = View.VISIBLE
                            }
                        } else {
                            Log.e("WITHOUT SLASH?", "YES")
                            if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumber) < java.lang.Double.parseDouble(nowEpisodeNumber!!)) {
                                prevURL = nextAndPrevURL[0]
                                nextURL = null
                                animeEpisodeBinding!!.prevEpisodeButton.visibility = View.VISIBLE
                                animeEpisodeBinding!!.nextEpisodeButton.visibility = View.GONE
                            } else if (java.lang.Double.parseDouble(nextOrPrevEpisodeNumber) > java.lang.Double.parseDouble(nowEpisodeNumber!!)) {
                                prevURL = null
                                nextURL = nextAndPrevURL[0]
                                animeEpisodeBinding!!.prevEpisodeButton.visibility = View.GONE
                                animeEpisodeBinding!!.nextEpisodeButton.visibility = View.VISIBLE
                            }
                        }

                    }
                }

            } else if (nextAndPrevURL.size > 2) {
                prevURL = nextAndPrevURL[0]
                nextURL = nextAndPrevURL[1]
                previousEpisodeNumber = prevURL!!.substring(prevURL!!.indexOf("episode-") + 8)
                nextEpisodeNumber = nextURL!!.substring(nextURL!!.indexOf("episode-") + 8)
                animeEpisodeBinding!!.prevEpisodeButton.visibility = View.VISIBLE
                animeEpisodeBinding!!.nextEpisodeButton.visibility = View.VISIBLE
            }
        }


        //Anime videos URL settings
        val getVideoEmbedURL = doc.select("iframe[allowfullscreen=allowfullscreen]")
        val getURLFromElementSrc = getVideoEmbedURL.attr("src")
        val getURLFromElementLazy = getVideoEmbedURL.attr("data-lazy-src")
        val getURLFromElementDataSrc = getVideoEmbedURL.attr("data-src")
        Log.e("VID URL 1", getURLFromElementSrc)
        Log.e("VID URL 2", getURLFromElementLazy)
        Log.e("VID URL 3", getURLFromElementDataSrc)
        Log.e("VID URL 4", getVideoEmbedURL.toString())
        val animeVideoEmbedURL: String
        if (!getURLFromElementSrc.startsWith("//")) {
            animeVideoEmbedURL = if (getURLFromElementLazy.startsWith("https:")) {
                getURLFromElementLazy
            } else {
                if (!getURLFromElementLazy.startsWith("http:")) {
                    "http:$getURLFromElementLazy"
                } else {
                    getURLFromElementLazy
                }
            }
            val animeStreamURL3 = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"$animeVideoEmbedURL\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>"
            videoStreamResultModel.videoUrl = animeStreamURL3
            Log.e("allData", Gson().toJson(videoStreamResultModel))
            animeEpisodeBinding!!.webViewWatchAnime.loadData(videoStreamResultModel.videoUrl, "text/html", "utf-8")
        } else if (!getURLFromElementLazy.startsWith("//")) {
            animeVideoEmbedURL = if (getURLFromElementSrc.startsWith("https:")) {
                getURLFromElementSrc
            } else {
                if (!getURLFromElementSrc.startsWith("http:")) {
                    "http:$getURLFromElementSrc"
                } else {
                    getURLFromElementSrc
                }
            }
            val animeStreamURL = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"$animeVideoEmbedURL\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>"
            videoStreamResultModel.videoUrl = animeStreamURL
            Log.e("allData", Gson().toJson(videoStreamResultModel))
            animeEpisodeBinding!!.webViewWatchAnime.loadData(videoStreamResultModel.videoUrl, "text/html", "utf-8")
        } else {
            animeVideoEmbedURL = if (getURLFromElementDataSrc.startsWith("https:")) {
                getURLFromElementDataSrc
            } else {
                if (!getURLFromElementDataSrc.startsWith("http:")) {
                    "http:$getURLFromElementDataSrc"
                } else {
                    getURLFromElementDataSrc
                }
            }
            val animeStreamURL2 = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"$animeVideoEmbedURL\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>"
            videoStreamResultModel.videoUrl = animeStreamURL2
            Log.e("allData", Gson().toJson(videoStreamResultModel))
            animeEpisodeBinding!!.webViewWatchAnime.loadData(videoStreamResultModel.videoUrl, "text/html", "utf-8")
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onGetWatchAnimeEpisodeDataSuccess(watchHTMLResult: String) {
        progressDialog!!.dismiss()
        parseHtmlToViewableContent(watchHTMLResult)
    }

    override fun onGetWatchAnimeEpisodeDataFailed() {
        progressDialog!!.dismiss()
        Toast.makeText(this@WatchAnimeEpisodeActivity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show()
    }
}
