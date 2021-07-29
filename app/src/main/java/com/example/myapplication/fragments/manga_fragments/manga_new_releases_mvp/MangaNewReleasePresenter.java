package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp;

import android.util.Log;

import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;

import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MangaNewReleasePresenter {
    private final MangaNewReleaseInterface newReleaseInterface;

    public MangaNewReleasePresenter(MangaNewReleaseInterface newReleaseInterface) {
        this.newReleaseInterface = newReleaseInterface;
    }

    public void getNewReleasesMangaData(int pageCount, String newReleasesURL, String hitStatus) {
        passToJsoup(pageCount, newReleasesURL, hitStatus);
    }

    private void passToJsoup(int pageCount, String newReleasesURL, String hitStatus) {
        Document doc;
        if (pageCount > 1) {
            doc = JsoupConfig.setInitJsoup(newReleasesURL + "page/" + pageCount + "/");
        } else {
            doc = JsoupConfig.setInitJsoup(newReleasesURL);
        }
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
                mangaNewReleaseResultModel.setMangaStatus(!StringUtil.isBlank(completedStatusParameter) && completedStatusParameter.equalsIgnoreCase("Completed"));
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
            newReleaseInterface.onGetNewReleasesDataSuccess(mangaNewReleaseResultModelList, hitStatus, null);
        } else {
            newReleaseInterface.onGetNewReleasesDataFailed();
        }
    }

}
