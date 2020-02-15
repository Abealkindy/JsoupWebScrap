package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp;

import android.util.Log;

import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
import com.google.gson.Gson;
import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscoverMangaPresenter {
    private DiscoverMangaInterface discoverMangaInterface;

    public DiscoverMangaPresenter(DiscoverMangaInterface discoverMangaInterface) {
        this.discoverMangaInterface = discoverMangaInterface;
    }

    public void getDiscoverOrSearchData(String discoverOrSearchURL) {
        Cloudflare cf = new Cloudflare(discoverOrSearchURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToJsoup(discoverOrSearchURL, cookies);
                }
            }

            @Override
            public void onFail() {
                discoverMangaInterface.onGetDiscoverMangaDataFailed();
            }
        });
    }

    private void passToJsoup(String discoverOrSearchURL, Map<String, String> cookies) {
        try {
            Connection.Response jsoupResponse = Jsoup
                    .connect(discoverOrSearchURL)
                    .userAgent("Mozilla/5.0")
                    .cookies(cookies)
                    .execute();
            Document doc = jsoupResponse.parse();
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
        } catch (IOException e) {
            e.printStackTrace();
            discoverMangaInterface.onGetDiscoverMangaDataFailed();
        }

    }
}
