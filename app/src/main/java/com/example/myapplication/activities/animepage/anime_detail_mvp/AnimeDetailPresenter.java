package com.example.myapplication.activities.animepage.anime_detail_mvp;

import android.util.Log;

import com.example.myapplication.models.mangamodels.DetailMangaModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;
import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimeDetailPresenter {
    private AnimeDetailInterface detailInterface;
    private DetailMangaModel detailMangaModel = new DetailMangaModel();

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
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToJsoup(getAnimeDetailURL, cookies);
                }
            }

            @Override
            public void onFail() {
                detailInterface.onGetDetailDataFailed();
            }
        });

    }

    private void passToJsoup(String newUrl, Map<String, String> cookies) {
        Document document = JsoupConfig.setInitJsoup(newUrl, cookies);
        if (document != null) {
            //get synopsis
            Elements getSynopsis = document.getElementsByTag("p");
            if (getSynopsis.eachText().size() < 2) {
                detailMangaModel.setMangaSynopsis(null);
            } else {
                detailMangaModel.setMangaSynopsis(getSynopsis.eachText().get(0));
            }

            //get Other name
            Elements getOtherName = document.getElementsByClass("text-h3");
            if (getOtherName.eachText().size() < 7) {
                detailMangaModel.setOtherNames(null);
                detailMangaModel.setTotalMangaChapter(getOtherName.eachText().get(1).substring(0, getOtherName.eachText().get(1).length() - 8));
                detailMangaModel.setLastMangaUpdateDate(getOtherName.eachText().get(2).replace(" per", "/").replace(" episode", "episode"));
                detailMangaModel.setFirstUpdateYear(getOtherName.eachText().get(3).substring(12));
                if (getOtherName.eachText().get(4).length() < 7) {
                    detailMangaModel.setMangaAuthor(null);
                } else {
                    detailMangaModel.setMangaAuthor(getOtherName.eachText().get(4).substring(7));
                }
            } else {
                if (getOtherName.eachText().get(0) == null) {
                    detailMangaModel.setOtherNames(null);
                } else {
                    detailMangaModel.setOtherNames(getOtherName.eachText().get(0));
                }
                detailMangaModel.setTotalMangaChapter(getOtherName.eachText().get(3).substring(0, getOtherName.eachText().get(3).length() - 8));
                detailMangaModel.setLastMangaUpdateDate(getOtherName.eachText().get(4).replace(" per", "/").replace(" episode", "episode"));
                detailMangaModel.setFirstUpdateYear(getOtherName.eachText().get(5).substring(12));
                if (getOtherName.eachText().get(6).length() < 7) {
                    detailMangaModel.setMangaAuthor("-");
                } else {
                    detailMangaModel.setMangaAuthor(getOtherName.eachText().get(6).substring(7));
                }
            }

            //get Genre
            Elements getGenre = document.getElementsByTag("li");
            List<DetailMangaModel.DetailMangaGenres> detailGenresList = new ArrayList<>();
            for (Element element : getGenre) {
                DetailMangaModel.DetailMangaGenres detailGenres = new DetailMangaModel().new DetailMangaGenres();
                String getGenreURL = element.select("a[href^=https://animeindo.co/genres/]").attr("href");
                String getGenreTitle = element.select("a[href^=https://animeindo.co/genres/]").text();
                detailGenres.setGenreURL(getGenreURL);
                detailGenres.setGenreTitle(getGenreTitle);
                detailGenresList.add(detailGenres);
            }
            List<DetailMangaModel.DetailMangaGenres> detailGenresListCut = new ArrayList<>(detailGenresList.subList(10, detailGenresList.size() - 3));

            //get All episodes
            Elements getAllEpisodes = document.getElementsByClass("col-12 col-sm-6 mb10");
            List<DetailMangaModel.DetailAllChapterDatas> allEpisodeDatasList = new ArrayList<>();
            for (Element element : getAllEpisodes) {
                DetailMangaModel.DetailAllChapterDatas allEpisodeDatas = new DetailMangaModel().new DetailAllChapterDatas();
                String episodeURL = element.getElementsByTag("a").attr("href");
                String episodeTitle = element.getElementsByTag("a").text();
                allEpisodeDatas.setChapterURL(episodeURL);
                if (episodeTitle.contains("Episode")) {
                    episodeTitle = episodeTitle.substring(episodeTitle.indexOf("Episode"));
                } else {
                    Log.e("CUT?", "BIG NO!");
                }
                allEpisodeDatas.setChapterTitle(episodeTitle);
                allEpisodeDatasList.add(allEpisodeDatas);
            }
            //get Rating
            Elements getRatingAnime = document.getElementsByClass("series-rating");
            String animeDetailRating = getRatingAnime.attr("style").substring(17, getRatingAnime.attr("style").indexOf("%"));
            detailMangaModel.setMangaRating(animeDetailRating);

            //store data from JSOUP
            detailInterface.onGetGenreSuccess(detailGenresListCut);
            detailInterface.onGetAllEpisodeSuccess(allEpisodeDatasList);
            detailInterface.onGetDetailDataSuccess(detailMangaModel);
        } else {
            detailInterface.onGetAllEpisodeFailed();
            detailInterface.onGetGenreFailed();
            detailInterface.onGetDetailDataFailed();
        }
    }

}
