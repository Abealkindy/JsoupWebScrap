package com.example.myapplication.activities.mangapage.manga_detail_mvp;

import android.util.Log;

import com.example.myapplication.models.mangamodels.DetailMangaModel;
import com.example.myapplication.networks.JsoupConfig;

import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailPresenter {
    private MangaDetailInterface mangaDetailInterface;
    private DetailMangaModel detailMangaModel = new DetailMangaModel();

    public MangaDetailPresenter(MangaDetailInterface mangaDetailInterface) {
        this.mangaDetailInterface = mangaDetailInterface;
    }

    public void getDetailMangaData(String detailPageURL) {
        passToJsoup(detailPageURL);
//        CloudFlare cf = new CloudFlare(detailPageURL);
//        cf.setUser_agent("Mozilla/5.0");
//        cf.getCookies(new CloudFlare.cfCallback() {
//            @Override
//            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
//                Log.e("getNewURL?", String.valueOf(hasNewUrl));
//                Map<String, String> cookies = CloudFlare.List2Map(cookieList);
//                if (hasNewUrl) {
//                    passToJsoup(newUrl, cookies);
//                    Log.e("NEWURL", newUrl);
//                } else {
//                    passToJsoup(detailPageURL, cookies);
//                }
//            }
//
//            @Override
//            public void onFail(String message) {
//                mangaDetailInterface.onGetDetailDataFailed();
//            }
//        });
    }

    private void passToJsoup(String newUrl) {
        Document document = JsoupConfig.setInitJsoup(newUrl, null);
        if (document != null) {
            //get title
            Elements getTitle = document.select("h1[itemprop=headline]");
            detailMangaModel.setMangaTitle(getTitle.text().substring(0, getTitle.text().indexOf(" Bahasa Indonesia")));

            //get thumb
            Elements getThumb = document.getElementsByTag("img");
            String mangaThumbnailBackground = getThumb.eachAttr("src").get(1);
            if (StringUtil.isBlank(mangaThumbnailBackground)) {
                mangaThumbnailBackground = getThumb.eachAttr("data-src").get(1);
            }
            if (!mangaThumbnailBackground.contains("https")) {
                mangaThumbnailBackground = "https:" + mangaThumbnailBackground;
            } else if (!mangaThumbnailBackground.contains("http")) {
                mangaThumbnailBackground = "http:" + mangaThumbnailBackground;
            }
            detailMangaModel.setMangaThumb(mangaThumbnailBackground);

            //get Synopsis
            Elements getSynopsis = document.getElementsByTag("p");
            detailMangaModel.setMangaSynopsis(getSynopsis.text());

            //get All chapter data
            List<DetailMangaModel.DetailAllChapterDatas> detailAllChapterDatasList = new ArrayList<>();
            Elements getAllMangaChapters = document.getElementsByClass("komik_info-chapters-item");
            for (Element element : getAllMangaChapters) {
                DetailMangaModel.DetailAllChapterDatas allChapterDatas = new DetailMangaModel.DetailAllChapterDatas();
                String chapterReleaseTime = element.getElementsByClass("chapter-link-time").text();
                String chapterTitle = element.select("a[href^=https://komikcast.com/chapter/]").text();
                String chapterURL = element.select("a[href^=https://komikcast.com/chapter/]").attr("href");
                if (chapterReleaseTime != null && !chapterReleaseTime.isEmpty() &&
                        chapterTitle != null && !chapterTitle.isEmpty() &&
                        chapterURL != null && !chapterURL.isEmpty()) {
                    allChapterDatas.setChapterReleaseTime(chapterReleaseTime);
                    allChapterDatas.setChapterTitle(chapterTitle);
                    allChapterDatas.setChapterURL(chapterURL);
                    detailAllChapterDatasList.add(allChapterDatas);
                }
            }


            //get genre data
            List<DetailMangaModel.DetailMangaGenres> genresList = new ArrayList<>();
            Elements getGenres = document.select("a[rel=tag]");
            for (Element element : getGenres) {
                String genreTitle = element.text();
                String genreURL = element.attr("href");
                DetailMangaModel.DetailMangaGenres mangaGenres = new DetailMangaModel.DetailMangaGenres();
                if (genreTitle != null && !genreTitle.isEmpty() &&
                        genreURL != null && !genreURL.isEmpty()) {
                    mangaGenres.setGenreTitle(genreTitle);
                    mangaGenres.setGenreURL(genreURL);
                    genresList.add(mangaGenres);
                }

            }

            //get Updated on
            Elements getLatestUpdate = document.select("time[itemprop=dateModified]");
            detailMangaModel.setLastMangaUpdateDate(getLatestUpdate.text());

            //get Other name
            Elements getOtherName = document.getElementsByClass("alter");
            detailMangaModel.setOtherNames(getOtherName.text());

            //get Author and others
            Elements getAuthor = document.getElementsByTag("span");

            for (int position = 0; position < getAuthor.eachText().size(); position++) {

                if (getAuthor.eachText().get(position).contains("Author")) {
                    if (getAuthor.eachText().get(position).length() < 8) {
                        detailMangaModel.setMangaAuthor(null);
                    } else {
                        detailMangaModel.setMangaAuthor(getAuthor.eachText().get(position).substring(getAuthor.eachText().get(position).indexOf("Author:") + 7));
                    }
                }

                if (getAuthor.eachText().get(position).contains("Released")) {
                    if (getAuthor.eachText().get(position).length() < 10) {
                        detailMangaModel.setFirstUpdateYear(null);
                    } else {
                        detailMangaModel.setFirstUpdateYear(getAuthor.eachText().get(position).substring(getAuthor.eachText().get(position).indexOf("Released:") + 9));
                    }
                }

                if (getAuthor.eachText().get(position).contains("Total Chapter")) {
                    if (getAuthor.eachText().get(position).length() < 15) {
                        detailMangaModel.setTotalMangaChapter(null);
                    } else {
                        detailMangaModel.setTotalMangaChapter(getAuthor.eachText().get(position).substring(getAuthor.eachText().get(position).indexOf("Total Chapter:") + 14));
                    }
                }

            }

            String getStatus = getAuthor.eachText().get(13).substring(6);
            detailMangaModel.setMangaStatus(getStatus);

            detailMangaModel.setMangaType(getAuthor.eachText().get(16).substring(6));

            Elements getRating = document.getElementsByClass("data-rating");
            detailMangaModel.setMangaRating(getRating.eachText().get(0).substring(7, getRating.eachText().get(0).length() - 10));
            //store data from JSOUP
            mangaDetailInterface.onGetGenreSuccess(genresList);
            mangaDetailInterface.onGetAllChapterSuccess(detailAllChapterDatasList);
            mangaDetailInterface.onGetDetailDataSuccess(detailMangaModel);
        } else {
            mangaDetailInterface.onGetAllChapterFailed();
            mangaDetailInterface.onGetGenreFailed();
            mangaDetailInterface.onGetDetailDataFailed();
        }
    }
}
