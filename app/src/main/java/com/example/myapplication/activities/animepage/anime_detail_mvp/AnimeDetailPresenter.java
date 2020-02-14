package com.example.myapplication.activities.animepage.anime_detail_mvp;

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

public class AnimeDetailPresenter {
    private AnimeDetailInterface detailInterface;

    public AnimeDetailPresenter(AnimeDetailInterface detailInterface) {
        this.detailInterface = detailInterface;
    }

    public void getAnimeDetailContent(String getAnimeDetailURL) {
        Cloudflare cf = new Cloudflare(getAnimeDetailURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                if (hasNewUrl) {
                    passToRetrofit(newUrl);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofit(getAnimeDetailURL);
                }
            }

            @Override
            public void onFail() {
                detailInterface.onGetDetailDataFailed();
            }
        });

    }

    private void passToRetrofit(String newUrl) {
//        String URLAfterCut = newUrl.substring(21);
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getSearchAnimeData(newUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        detailInterface.onGetDetailDataSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        detailInterface.onGetDetailDataFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
