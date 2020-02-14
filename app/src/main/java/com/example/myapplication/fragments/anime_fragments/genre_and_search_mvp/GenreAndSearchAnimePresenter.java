package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp;

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

public class GenreAndSearchAnimePresenter {
    private GenreAndSearchAnimeInterface genreAndSearchAnimeInterface;

    public GenreAndSearchAnimePresenter(GenreAndSearchAnimeInterface genreAndSearchAnimeInterface) {
        this.genreAndSearchAnimeInterface = genreAndSearchAnimeInterface;
    }

    public void getGenreAndSearchData(String genreAndSearchURL) {
        Cloudflare cf = new Cloudflare(genreAndSearchURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                if (hasNewUrl) {
                    passToRetrofit(newUrl);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofit(genreAndSearchURL);
                }
            }

            @Override
            public void onFail() {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
            }
        });
    }

    public void getOnlyGenreData(String genreURL) {
        Cloudflare cf = new Cloudflare(genreURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                if (hasNewUrl) {
                    passToRetrofitGenre(newUrl);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofitGenre(genreURL);
                }
            }

            @Override
            public void onFail() {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
            }
        });
    }

    private void passToRetrofitGenre(String genreURL) {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getSearchAnimeData(genreURL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        genreAndSearchAnimeInterface.onGetOnlyGenreDataSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        genreAndSearchAnimeInterface.onGetOnlyGenreDataFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void passToRetrofit(String genreAndSearchURL) {
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getGenreAnimeData(genreAndSearchURL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        genreAndSearchAnimeInterface.onGetSearchAndGenreDataSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
