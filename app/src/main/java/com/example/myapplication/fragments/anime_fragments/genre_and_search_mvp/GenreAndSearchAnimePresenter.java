package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp;

import android.util.Log;

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
import com.google.gson.Gson;
import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToRetrofit(newUrl, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofit(genreAndSearchURL, cookies);
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
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToRetrofitGenre(newUrl, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofitGenre(genreURL, cookies);
                }
            }

            @Override
            public void onFail() {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
            }
        });
    }

    private void passToRetrofitGenre(String genrePageURL, Map<String, String> cookies) {
        try {
            Connection.Response jsoupResponse = Jsoup
                    .connect(genrePageURL)
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute();
            Document doc = jsoupResponse.parse();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList = new ArrayList<>();
            genreResultList.clear();
            Elements getGenreData = doc.select("a[href^=https://animeindo.to/genres/]");
            for (Element element : getGenreData) {
                String genreURL = element.attr("href");
                String genreTitle = element.text();
                AnimeGenreAndSearchResultModel.AnimeGenreResult genreResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                genreResult.setGenreTitle(genreTitle);
                genreResult.setGenreURL(genreURL);
                genreResultList.add(genreResult);
            }
            genreAndSearchAnimeInterface.onGetOnlyGenreDataSuccess(genreResultList);
        } catch (Exception e) {
            e.printStackTrace();
            genreAndSearchAnimeInterface.onGetOnlyGenreDataFailed();
        }
    }

    private void passToRetrofit(String genreAndSearchURL, Map<String, String> cookies) {
        try {
            Connection.Response jsoupResponse = Jsoup
                    .connect(genreAndSearchURL)
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute();
            Document doc = jsoupResponse.parse();
            List<AnimeGenreAndSearchResultModel.AnimeSearchResult> animeGenreAndSearchResultModelList = new ArrayList<>();
            Elements getListData = doc.getElementsByClass("col-6 col-md-4 col-lg-3 col-wd-per5 col-xl-per5 mb40");
            for (Element element : getListData) {
                String detailURL = element.select("a[href^=https://animeindo.to/anime/]").attr("href");
                String thumbURL = element.getElementsByClass("episode-ratio background-cover").attr("style").substring(element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf("https://"), element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf(")"));
                if (thumbURL.contains("'")) {
                    thumbURL = thumbURL.replace("'", "");
                }
                String animeTitle = element.getElementsByTag("h4").text();
                String animeStatus = "", animeType = "";
                if (element.getElementsByClass("text-h6").eachText().size() < 2) {
                    animeType = element.getElementsByClass("text-h6").eachText().get(0);
                } else {
                    animeStatus = element.getElementsByClass("text-h6").eachText().get(0);
                    animeType = element.getElementsByClass("text-h6").eachText().get(1);
                }

                AnimeGenreAndSearchResultModel.AnimeSearchResult searchResult = new AnimeGenreAndSearchResultModel().new AnimeSearchResult();
                searchResult.setAnimeDetailURL(detailURL);
                searchResult.setAnimeThumb(thumbURL);
                searchResult.setAnimeTitle(animeTitle);
                searchResult.setAnimeStatus(animeStatus);
                searchResult.setAnimeType(animeType);
                animeGenreAndSearchResultModelList.add(searchResult);
                Log.e("GENRE AND SEARCH", new Gson().toJson(animeGenreAndSearchResultModelList));
            }
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataSuccess(animeGenreAndSearchResultModelList);
        } catch (Exception e) {
            e.printStackTrace();
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
        }
    }
}
