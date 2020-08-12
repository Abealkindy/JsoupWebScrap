package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp;

import android.util.Log;

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.networks.CloudFlare;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;

import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dalvik.system.DelegateLastClassLoader;

public class DiscoverMangaPresenter {
    private DiscoverMangaInterface discoverMangaInterface;

    public DiscoverMangaPresenter(DiscoverMangaInterface discoverMangaInterface) {
        this.discoverMangaInterface = discoverMangaInterface;
    }

    public void getDiscoverOrSearchData(String discoverOrSearchURL, String type) {
        CloudFlare cf = new CloudFlare(discoverOrSearchURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new CloudFlare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = CloudFlare.List2Map(cookieList);
                if (hasNewUrl) {
                    if (type.equalsIgnoreCase("genre")) {
                        getFilterComponentData(newUrl, cookies);
                    } else {
                        passToJsoup(newUrl, cookies);
                    }
                    Log.e("NEWURL", newUrl);
                } else {
                    if (type.equalsIgnoreCase("genre")) {
                        getFilterComponentData(discoverOrSearchURL, cookies);
                    } else {
                        passToJsoup(discoverOrSearchURL, cookies);
                    }
                }
            }

            @Override
            public void onFail() {
                discoverMangaInterface.onGetDiscoverMangaDataFailed();
            }
        });
    }

    private void getFilterComponentData(String filterAddOnURL, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(filterAddOnURL, cookies);
        if (doc != null) {
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList = new ArrayList<>();
            genreResultList.clear();
            sortResultList.clear();
            typeResultList.clear();
            statusResultList.clear();
            //getStatusElement
            Elements getStatusData = doc.getElementsByClass("lbx").get(0).getElementsByClass("radiox");
            //getTypeElement
            Elements getTypeData = doc.getElementsByClass("lbx").get(1).getElementsByClass("radiox");
            //getSortElement
            Elements getSortData = doc.getElementsByClass("lbx").get(2).getElementsByClass("radiox");
            //getGenreElement
            Elements getGenreData = doc.getElementsByClass("lbx").get(3).getElementsByClass("genx");

            //getStatus
            for (Element element : getStatusData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult statusResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getStatusText = element.text();
                String getStatusUrl = element.select("input").attr("value");
                statusResult.setGenreTitle(getStatusText);
                statusResult.setGenreURL(getStatusUrl);
                statusResultList.add(statusResult);
            }
            discoverMangaInterface.onGetStatusDataSuccess(statusResultList);
            //getType
            for (Element element : getTypeData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult typeResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getTypeText = element.text();
                String getTypeUrl = element.select("input").attr("value");
                typeResult.setGenreTitle(getTypeText);
                typeResult.setGenreURL(getTypeUrl);
                typeResultList.add(typeResult);
            }
            discoverMangaInterface.onGetTypeDataSuccess(typeResultList);
            //getSort
            for (Element element : getSortData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult sortResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getSortText = element.text();
                String getSortUrl = element.select("input").attr("value");
                sortResult.setGenreTitle(getSortText);
                sortResult.setGenreURL(getSortUrl);
                sortResultList.add(sortResult);
            }
            discoverMangaInterface.onGetSortDataSuccess(sortResultList);
            //getGenre
            for (Element element : getGenreData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult genreResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getGenreText = element.text();
                String getGenreUrl = element.select("input").attr("value");
                genreResult.setGenreTitle(getGenreText);
                genreResult.setGenreURL(getGenreUrl);
                genreResultList.add(genreResult);
            }
            discoverMangaInterface.onGetGenreDataSuccess(genreResultList);
        } else {
            discoverMangaInterface.onGetDiscoverMangaDataFailed();
        }
    }

    private void passToJsoup(String discoverOrSearchURL, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(discoverOrSearchURL, cookies);
        if (doc != null) {
            Elements newchaptercon = doc.getElementsByClass("bs");
            List<DiscoverMangaModel> mangaNewReleaseResultModelList = new ArrayList<>();
            for (Element el : newchaptercon) {
                String mangaType = el.getElementsByAttributeValueContaining("class", "type ").text();
                String mangaThumbnailBackground = el.getElementsByTag("img").attr("data-src");
                if (StringUtil.isBlank(mangaThumbnailBackground)) {
                    mangaThumbnailBackground = el.getElementsByTag("img").attr("src");
                }
                if (!mangaThumbnailBackground.contains("https")) {
                    mangaThumbnailBackground = "https:" + mangaThumbnailBackground;
                } else if (!mangaThumbnailBackground.contains("http")) {
                    mangaThumbnailBackground = "http:" + mangaThumbnailBackground;
                }

                String mangaTitle = el.getElementsByClass("tt").text();
                String chapterRating = el.getElementsByTag("i").text();
                String chapterUrl = el.select("a[href^=https://komikcast.com/chapter/]").attr("href");
                String mangaUrl = el.select("a[href^=https://komikcast.com/komik/]").attr("href");
                String chapterText = el.select("a[href^=https://komikcast.com/chapter/]").text();
                String completedStatusParameter = el.getElementsByClass("status Completed").text();
                DiscoverMangaModel mangaNewReleaseResultModel = new DiscoverMangaModel();
                if (StringUtil.isBlank(completedStatusParameter) || !completedStatusParameter.equalsIgnoreCase("Completed")) {
                    mangaNewReleaseResultModel.setMangaStatus(false);
                } else {
                    mangaNewReleaseResultModel.setMangaStatus(true);
                }
                mangaNewReleaseResultModel.setMangaURL(mangaUrl);
                mangaNewReleaseResultModel.setMangaType(mangaType);
                mangaNewReleaseResultModel.setMangaTitle(mangaTitle);
                mangaNewReleaseResultModel.setMangaThumb(mangaThumbnailBackground);
                mangaNewReleaseResultModel.setMangaRating(chapterRating);
                mangaNewReleaseResultModel.setMangaLatestChapter(chapterUrl);
                mangaNewReleaseResultModel.setMangaLatestChapterText(chapterText);
                mangaNewReleaseResultModelList.add(mangaNewReleaseResultModel);
            }
            Log.e("resultBeforeCutDiscover", new Gson().toJson(mangaNewReleaseResultModelList));
            //store data from JSOUP
            discoverMangaInterface.onGetDiscoverMangaDataSuccess(mangaNewReleaseResultModelList);
        } else {
            discoverMangaInterface.onGetDiscoverMangaDataFailed();
        }

    }
}
