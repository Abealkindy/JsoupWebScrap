package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp;

import android.util.Log;

import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import java.net.HttpCookie;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnimeNewReleasesPresenter {
    private AnimeNewReleasesInterface newReleasesInterface;

    public AnimeNewReleasesPresenter(AnimeNewReleasesInterface newReleasesInterface) {
        this.newReleasesInterface = newReleasesInterface;
    }

    public void getNewReleasesAnimeData(int pageCount, String newReleasesURL, String hitStatus) {
        Cloudflare cf = new Cloudflare(newReleasesURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                if (hasNewUrl) {
                    passToRetrofit(pageCount, newUrl, hitStatus);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofit(pageCount, newReleasesURL, hitStatus);
                }
            }

            @Override
            public void onFail() {
                newReleasesInterface.onGetNewReleasesDataFailed();
            }
        });
    }

    private void passToRetrofit(int pageCount, String newUrl, String hitStatus) {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getNewReleaseAnimeData(newUrl + "/page/" + pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        newReleasesInterface.onGetNewReleasesDataSuccess(result, hitStatus);
                    }

                    @Override
                    public void onError(Throwable e) {
                        newReleasesInterface.onGetNewReleasesDataFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
