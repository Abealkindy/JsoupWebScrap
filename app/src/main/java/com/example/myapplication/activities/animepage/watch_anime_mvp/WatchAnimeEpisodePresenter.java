package com.example.myapplication.activities.animepage.watch_anime_mvp;

import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import java.net.HttpCookie;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WatchAnimeEpisodePresenter {
    private WatchAnimeEpisodeInterface watchAnimeEpisodeInterface;

    public WatchAnimeEpisodePresenter(WatchAnimeEpisodeInterface watchAnimeEpisodeInterface) {
        this.watchAnimeEpisodeInterface = watchAnimeEpisodeInterface;
    }

    public void getEpisodeToWatchData(String episodeURL) {
        Cloudflare cf = new Cloudflare(episodeURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                if (hasNewUrl) {
                    passToRetrofit(newUrl);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofit(episodeURL);
                }
            }

            @Override
            public void onFail() {
                watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataFailed();
            }
        });
    }

    private void passToRetrofit(String episodeURL) {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getWatchAnimeData(episodeURL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
