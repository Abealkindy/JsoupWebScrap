package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp;

import android.util.Log;

import com.example.myapplication.networks.CloudFlare;
import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenreAndSearchAnimePresenter {
    private GenreAndSearchAnimeInterface genreAndSearchAnimeInterface;

    public GenreAndSearchAnimePresenter(GenreAndSearchAnimeInterface genreAndSearchAnimeInterface) {
        this.genreAndSearchAnimeInterface = genreAndSearchAnimeInterface;
    }

    public void getGenreAndSearchData(String hitStatus, String genreAndSearchURL) {
        Log.e("resultURL", genreAndSearchURL);
        CloudFlare cf = new CloudFlare(genreAndSearchURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new CloudFlare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = CloudFlare.List2Map(cookieList);
                boolean b = hitStatus.equalsIgnoreCase("genrePage") || hitStatus.equalsIgnoreCase("newPage") || hitStatus.equalsIgnoreCase("swipeRefresh") || hitStatus.equalsIgnoreCase("searchRequest") || hitStatus.equalsIgnoreCase("searchScrollRequest");
                if (hasNewUrl) {
                    if (b) {
                        passToRetrofits(newUrl, cookies);
                    } else {
                        passToRetrofit(newUrl, cookies);
                    }
                    Log.e("NEWURL", newUrl);
                } else {
                    if (b) {
                        passToRetrofits(genreAndSearchURL, cookies);
                    } else {
                        passToRetrofit(genreAndSearchURL, cookies);
                    }
                }
            }

            @Override
            public void onFail(String message) {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
            }
        });
    }

    public void getOnlyGenreData(String genreURL) {
        Log.e("genreURL", genreURL);
        CloudFlare cf = new CloudFlare(genreURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new CloudFlare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = CloudFlare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToRetrofitGenre(newUrl, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofitGenre(genreURL, cookies);
                }
            }

            @Override
            public void onFail(String message) {
                genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
            }
        });
    }

    private void passToRetrofitGenre(String genrePageURL, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(genrePageURL, cookies);
        if (doc != null) {
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList = new ArrayList<>();
            genreResultList.clear();
            sortResultList.clear();
            typeResultList.clear();
            statusResultList.clear();
            Elements getGenreData = doc.getElementsByClass("tax_fil");
            Elements getSortData = doc.getElementById("order").getElementsByTag("option");
            Elements getTypeData = doc.getElementById("type").getElementsByTag("option");
            Elements getStatusData = doc.getElementById("status").getElementsByTag("option");
            //get genre data
            for (Element element : getGenreData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult genreResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String genreTitle = element.text();
                genreResult.setGenreTitle(genreTitle);
                genreResult.setGenreURL(genreTitle.toLowerCase().replace(" ", "-").replace(".", ""));
                genreResultList.add(genreResult);
            }
            //get sort data
            for (Element element : getSortData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult sortResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String sortTitle = "", sortValue = "";
                sortTitle = element.text();
                sortValue = element.attr("value");
                sortResult.setGenreTitle(sortTitle);
                sortResult.setGenreURL(sortValue);
                sortResultList.add(sortResult);
            }
            //get type data
            for (Element element : getTypeData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult typeResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String sortTitle = "", sortValue = "";
                sortTitle = element.text();
                sortValue = element.attr("value");
                typeResult.setGenreTitle(sortTitle);
                typeResult.setGenreURL(sortValue);
                typeResultList.add(typeResult);
            }
            //get status data
            for (Element element : getStatusData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult statusResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String sortTitle = "", sortValue = "";
                sortTitle = element.text();
                sortValue = element.attr("value").replace(" ", "+");
                statusResult.setGenreTitle(sortTitle);
                statusResult.setGenreURL(sortValue);
                statusResultList.add(statusResult);
            }
            Log.e("genreResult", new Gson().toJson(genreResultList));
            Log.e("statusResult", new Gson().toJson(statusResultList));
            Log.e("typeResult", new Gson().toJson(typeResultList));
            Log.e("sortResult", new Gson().toJson(sortResultList));
            genreAndSearchAnimeInterface.onGetOnlyGenreDataSuccess(genreResultList);
            genreAndSearchAnimeInterface.onGetOnlySortDataSuccess(sortResultList);
            genreAndSearchAnimeInterface.onGetOnlyStatusDataSuccess(statusResultList);
            genreAndSearchAnimeInterface.onGetOnlyTypeDataSuccess(typeResultList);
        } else {
            genreAndSearchAnimeInterface.onGetOnlyGenreDataFailed();
        }
    }

    private void passToRetrofit(String genreAndSearchURL, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(genreAndSearchURL, cookies);
        if (doc != null) {
            List<AnimeGenreAndSearchResultModel.AnimeSearchResult> animeGenreAndSearchResultModelList = new ArrayList<>();
            Elements getListData = doc.getElementsByClass("col-6 col-sm-4 col-md-3 col-lg-3 mb40");
            for (Element element : getListData) {
                String detailURL = element.select("a[href^=https://animeindo.cc/anime/]").attr("href");
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
            }
            Log.e("GENRE AND SEARCH", new Gson().toJson(animeGenreAndSearchResultModelList));
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataSuccess(animeGenreAndSearchResultModelList);
        } else {
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
        }
    }

    private void passToRetrofits(String genreAndSearchURL, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(genreAndSearchURL, cookies);
        if (doc != null) {
            List<AnimeGenreAndSearchResultModel.AnimeSearchResultNew> animeGenreAndSearchResultModelList = new ArrayList<>();
            Elements getListData = doc.getElementsByClass("col-6 col-sm-4 col-md-3 col-lg-3 col-xl-per5 mb40");
            for (Element element : getListData) {
                String detailURL = element.select("a[href^=https://animeindo.cc/anime/]").attr("href");
                String thumbURL = element.getElementsByClass("episode-ratio background-cover").attr("style").substring(element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf("https://"), element.getElementsByClass("episode-ratio background-cover").attr("style").indexOf(")"));
                if (thumbURL.contains("'")) {
                    thumbURL = thumbURL.replace("'", "");
                }
                String animeTitle = element.getElementsByTag("h4").text();
                String animeGenre = "", animeRating = "";
                animeGenre = element.getElementsByClass("text-h6").text();
                Elements getRating = element.getElementsByClass("series-rating");
                animeRating = getRating.attr("style").substring(17, getRating.attr("style").indexOf("%"));
                animeRating = String.valueOf(Float.parseFloat(animeRating) / 10 / 2);
                AnimeGenreAndSearchResultModel.AnimeSearchResultNew searchResult = new AnimeGenreAndSearchResultModel().new AnimeSearchResultNew();
                searchResult.setAnimeDetailURL(detailURL);
                searchResult.setAnimeThumb(thumbURL);
                searchResult.setAnimeTitle(animeTitle);
                searchResult.setAnimeGenre(animeGenre);
                searchResult.setAnimeRating(animeRating);
                animeGenreAndSearchResultModelList.add(searchResult);
            }
            Log.e("GENRE AND SEARCH NEW", new Gson().toJson(animeGenreAndSearchResultModelList));
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataSuccessNew(animeGenreAndSearchResultModelList);
        } else {
            genreAndSearchAnimeInterface.onGetSearchAndGenreDataFailed();
        }
    }
}
