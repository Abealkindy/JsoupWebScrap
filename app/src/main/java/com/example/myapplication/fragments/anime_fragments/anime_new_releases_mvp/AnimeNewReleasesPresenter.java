package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp;

import android.util.Log;

import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel;
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

public class AnimeNewReleasesPresenter {
    private AnimeNewReleasesInterface newReleasesInterface;

    public AnimeNewReleasesPresenter(AnimeNewReleasesInterface newReleasesInterface) {
        this.newReleasesInterface = newReleasesInterface;
    }

    public void getNewReleasesAnimeData(int pageCount, String newReleasesURL, String hitStatus) {
        Cloudflare cf = new Cloudflare(newReleasesURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToRetrofit(pageCount, newUrl, hitStatus, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToRetrofit(pageCount, newReleasesURL, hitStatus, cookies);
                }
            }

            @Override
            public void onFail() {
                newReleasesInterface.onGetNewReleasesDataFailed();
            }
        });
    }

    private void passToRetrofit(int pageCount, String newUrl, String hitStatus, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(newUrl + "/page/" + pageCount, cookies);
        if (doc != null) {
            Elements newepisodecon = doc.getElementsByClass("col-6 col-sm-4 col-md-3 col-xl-per5 mb40");
            List<AnimeNewReleaseResultModel> animeNewReleaseResultModelList = new ArrayList<>();

            for (Element el : newepisodecon) {
                String animeThumbnailBackground = el.getElementsByClass("episode-ratio background-cover").attr("style");
                if (animeThumbnailBackground.contains("'")) {
                    animeThumbnailBackground = animeThumbnailBackground.replace("'", "");
                }
                String thumbnailCut = animeThumbnailBackground.substring(animeThumbnailBackground.indexOf("https://"), animeThumbnailBackground.indexOf(")"));
                String animeEpisode = el.getElementsByTag("h3").text();
                String animeEpisodeNumber = el.getElementsByClass("episode-number").text();
                List<String> animeStatusAndType = el.getElementsByClass("text-h6").eachText();
                String animeEpisodeStatus = "", animeEpisodeType;
                if (animeStatusAndType.size() < 2) {
                    animeEpisodeType = el.getElementsByClass("text-h6").eachText().get(0);
                } else {
                    animeEpisodeStatus = el.getElementsByClass("text-h6").eachText().get(0);
                    animeEpisodeType = el.getElementsByClass("text-h6").eachText().get(1);
                }
                String epsiodeURL = el.getElementsByTag("a").attr("href");

                AnimeNewReleaseResultModel animeNewReleaseResultModel = new AnimeNewReleaseResultModel();
                animeNewReleaseResultModel.setAnimeEpisode(animeEpisode);
                animeNewReleaseResultModel.setEpisodeThumb(thumbnailCut);
                animeNewReleaseResultModel.setEpisodeURL(epsiodeURL);
                animeNewReleaseResultModel.setAnimeEpisodeNumber(animeEpisodeNumber);
                animeNewReleaseResultModel.setAnimeEpisodeType(animeEpisodeType);
                animeNewReleaseResultModel.setAnimeEpisodeStatus(animeEpisodeStatus);
                animeNewReleaseResultModelList.add(animeNewReleaseResultModel);
            }
            List<AnimeNewReleaseResultModel> animeNewReleaseResultModelListAfterCut = new ArrayList<>(animeNewReleaseResultModelList.subList(6, animeNewReleaseResultModelList.size() - 1));
            Log.e("resultBeforeCut", new Gson().toJson(animeNewReleaseResultModelList));
            Log.e("resultAfterCut", new Gson().toJson(animeNewReleaseResultModelListAfterCut));
            newReleasesInterface.onGetNewReleasesDataSuccess(animeNewReleaseResultModelListAfterCut, hitStatus);
        } else {
            newReleasesInterface.onGetNewReleasesDataFailed();
        }
    }
}
