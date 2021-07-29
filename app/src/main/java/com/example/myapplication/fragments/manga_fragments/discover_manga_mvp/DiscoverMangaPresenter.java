package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp;

import android.util.Log;

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;

import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class DiscoverMangaPresenter {
    private final DiscoverMangaInterface discoverMangaInterface;

    public DiscoverMangaPresenter(DiscoverMangaInterface discoverMangaInterface) {
        this.discoverMangaInterface = discoverMangaInterface;
    }

    public void getDiscoverOrSearchData(String discoverOrSearchURL, String type) {
        if (type.equalsIgnoreCase("genre")) {
            getFilterComponentData(discoverOrSearchURL);
        } else {
            passToJsoup(discoverOrSearchURL);
        }
    }

    private void getFilterComponentData(String filterAddOnURL) {
        Document doc = JsoupConfig.setInitJsoup(filterAddOnURL);
        if (doc != null) {
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList = new ArrayList<>();
            List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList = new ArrayList<>();
            //getStatusElement
            Elements getStatusData = doc.getElementsByClass("komiklist_dropdown-menu status").select("li");
            //getTypeElement
            Elements getTypeData = doc.getElementsByClass("komiklist_dropdown-menu type").select("li");
            //getSortElement
            Elements getSortData = doc.getElementsByClass("komiklist_dropdown-menu sort_by").select("li");
            //getGenreElement
            Elements getGenreData = doc.getElementsByClass("komiklist_dropdown-menu c4 genrez").select("li");

            //getStatus
            for (Element element : getStatusData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult statusResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getStatusText = element.getElementsByTag("label").text();
                String getStatusUrl = element.select("input").attr("value");
                statusResult.setGenreTitle(getStatusText);
                statusResult.setGenreURL(getStatusUrl);
                statusResultList.add(statusResult);
            }
            discoverMangaInterface.onGetStatusDataSuccess(statusResultList);
            //getType
            for (Element element : getTypeData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult typeResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getTypeText = element.getElementsByTag("label").text();
                String getTypeUrl = element.select("input").attr("value");
                typeResult.setGenreTitle(getTypeText);
                typeResult.setGenreURL(getTypeUrl);
                typeResultList.add(typeResult);
            }
            discoverMangaInterface.onGetTypeDataSuccess(typeResultList);
            //getSort
            for (Element element : getSortData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult sortResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getSortText = element.getElementsByTag("label").text();
                String getSortUrl = element.select("input").attr("value");
                sortResult.setGenreTitle(getSortText);
                sortResult.setGenreURL(getSortUrl);
                sortResultList.add(sortResult);
            }
            discoverMangaInterface.onGetSortDataSuccess(sortResultList);
            //getGenre
            for (Element element : getGenreData) {
                AnimeGenreAndSearchResultModel.AnimeGenreResult genreResult = new AnimeGenreAndSearchResultModel().new AnimeGenreResult();
                String getGenreText = element.getElementsByTag("label").text();
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

    private void passToJsoup(String discoverOrSearchURL) {
        Document doc = JsoupConfig.setInitJsoup(discoverOrSearchURL);
        if (doc != null) {
            Elements newchaptercon = doc.getElementsByClass("list-update_item");
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

                String mangaTitle = el.getElementsByClass("title").text();
                String chapterRating = el.getElementsByClass("numscore").text();
                String chapterUrl = el.getElementsByClass("chapter").attr("href");
                String mangaUrl = el.select("a[href^=https://komikcast.com/komik/]").attr("href");
                String chapterText = el.getElementsByClass("chapter").text();
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
            discoverMangaInterface.onGetDiscoverMangaDataSuccess(mangaNewReleaseResultModelList);
        } else {
            discoverMangaInterface.onGetDiscoverMangaDataFailed();
        }

    }
}
