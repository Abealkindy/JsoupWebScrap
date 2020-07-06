package com.example.myapplication.activities.animepage.anime_detail_mvp;

import android.util.Log;

import com.example.myapplication.networks.CloudFlare;
import com.example.myapplication.models.mangamodels.DetailMangaModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;

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
        CloudFlare cf = new CloudFlare(getAnimeDetailURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new CloudFlare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = CloudFlare.List2Map(cookieList);
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
        Log.e("url", newUrl);
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
            String getOtherName = document.getElementsByClass("text-h3 japannm").text();
            if (getOtherName != null && !getOtherName.isEmpty()) {
                detailMangaModel.setOtherNames(getOtherName);
            } else {
                detailMangaModel.setOtherNames(null);
            }

            Elements getPrintilanDetail = document.getElementsByClass("text-h3 block");
            if (getPrintilanDetail.size() > 2) {
                //set total episode
                detailMangaModel.setLastMangaUpdateDate(getPrintilanDetail.eachText().get(1));
                //set duration/episodes
                detailMangaModel.setTotalMangaChapter(getPrintilanDetail.eachText().get(0));
                //set first release
                detailMangaModel.setFirstUpdateYear(getPrintilanDetail.eachText().get(2));
            } else {
                //set total episode
                detailMangaModel.setLastMangaUpdateDate(getPrintilanDetail.eachText().get(1));
                //set duration/episodes
                detailMangaModel.setTotalMangaChapter(getPrintilanDetail.eachText().get(0));
                //set first release
                detailMangaModel.setFirstUpdateYear(null);
            }
            //get Genre
            Elements getGenre = document.select("a[href^=https://animeindo.fun/genre/]");
            List<DetailMangaModel.DetailMangaGenres> detailGenresList = new ArrayList<>();
            for (Element element : getGenre) {
                DetailMangaModel.DetailMangaGenres detailGenres = new DetailMangaModel().new DetailMangaGenres();
                String getGenreURL = element.select("a[href^=https://animeindo.fun/genre/]").attr("href");
                String getGenreTitle = element.select("a[href^=https://animeindo.fun/genre/]").text();
                detailGenres.setGenreURL(getGenreURL);
                detailGenres.setGenreTitle(getGenreTitle);
                detailGenresList.add(detailGenres);
            }
            Log.e("GENREWITHOUTCUT", new Gson().toJson(detailGenresList));

            //get All episodes
            Elements getAllEpisodes = document.getElementsByClass("row episodes text-h4 py-3");
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
            detailInterface.onGetGenreSuccess(detailGenresList);
            detailInterface.onGetAllEpisodeSuccess(allEpisodeDatasList);
            detailInterface.onGetDetailDataSuccess(detailMangaModel);
        } else {
            detailInterface.onGetAllEpisodeFailed();
            detailInterface.onGetGenreFailed();
            detailInterface.onGetDetailDataFailed();
        }
    }

}
