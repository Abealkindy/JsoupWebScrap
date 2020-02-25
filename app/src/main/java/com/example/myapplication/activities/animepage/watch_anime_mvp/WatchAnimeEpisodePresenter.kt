package com.example.myapplication.activities.animepage.watch_anime_mvp

import android.util.Log
import com.example.myapplication.networks.RetrofitConfig
import com.zhkrb.cloudflare_scrape_android.Cloudflare

import java.net.HttpCookie

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WatchAnimeEpisodePresenter(private val watchAnimeEpisodeInterface: WatchAnimeEpisodeInterface) {

    fun getEpisodeToWatchData(episodeURL: String) {
        val cf = Cloudflare(episodeURL)
        cf.user_agent = "Mozilla/5.0"
        cf.getCookies(object : Cloudflare.cfCallback {
            override fun onSuccess(cookieList: List<HttpCookie>, hasNewUrl: Boolean, newUrl: String) {
                Log.e("getNewURL?", hasNewUrl.toString())
                if (hasNewUrl) {
                    passToRetrofit(newUrl)
                    Log.e("NEWURL", newUrl)
                } else {
                    passToRetrofit(episodeURL)
                }
            }

            override fun onFail() {
                watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataFailed()
            }
        })
    }

    private fun passToRetrofit(episodeURL: String) {
        val apiEndPointService = RetrofitConfig.initAnimeRetrofit
        apiEndPointService.getWatchAnimeData(episodeURL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(result: String) {
                        watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataSuccess(result)
                    }

                    override fun onError(e: Throwable) {
                        watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataFailed()
                    }

                    override fun onComplete() {

                    }
                })
    }
}
