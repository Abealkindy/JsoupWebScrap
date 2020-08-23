package com.example.myapplication.activities.animepage.watch_anime_mvp;

import android.util.Log;

import com.example.myapplication.models.animemodels.VideoStreamResultModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatchAnimeEpisodePresenter {
    private WatchAnimeEpisodeInterface watchAnimeEpisodeInterface;

    public WatchAnimeEpisodePresenter(WatchAnimeEpisodeInterface watchAnimeEpisodeInterface) {
        this.watchAnimeEpisodeInterface = watchAnimeEpisodeInterface;
    }

    public void getEpisodeToWatchData(String nowEpisodeNumber, String htmlPage) {
        Log.e("epsNum", nowEpisodeNumber);
        Document document = JsoupConfig.setHtmlParseJsoup(htmlPage);
        String prevURL = "", nextURL = "";
        VideoStreamResultModel videoStreamResultModel = new VideoStreamResultModel();
        if (document != null) {
            List<Element> getAllEpisode = document.getElementsByTag("li").subList(15, document.getElementsByTag("li").size() - 3);
            List<String> urlString = new ArrayList<>();
            List<String> textString = new ArrayList<>();
            for (Element element : getAllEpisode) {
                String episodeURL = element.select("a[href^=https://animeindo.cc/]").attr("href");
                urlString.add(episodeURL);
                String episodeText = element.select("a[href^=https://animeindo.cc/]").text();
                if (episodeURL.contains(nowEpisodeNumber)) {
                    if (episodeURL.contains(episodeText)) {
                        episodeText = episodeText + " Now Watching";
                    }
                }
                textString.add(episodeText);
            }
            Log.e("all episode list ", "\n" + getAllEpisode);
            Log.e("all episode list ", new Gson().toJson(urlString));
//            Log.e("last ", "" + urlString.get(urlString.size() - 1).contains(nowEpisodeNumber));
            Log.e("all episode list ", new Gson().toJson(textString));
            //Anime Details URL settings
            Elements getElementsAnimeDetails = document.select("a[href^=https://animeindo.cc/anime/]");
            if (getElementsAnimeDetails.isEmpty()) {
                Log.e("VideoDetailNull?", "Ya");
            } else {
                String animeDetailsURL = getElementsAnimeDetails.attr("href");
                if (!animeDetailsURL.startsWith("https://animeindo.cc/")) {
                    Log.e("VideoresultURLError?", "Ya");
                    videoStreamResultModel.setAnimeDetailURL(null);
                } else {
                    videoStreamResultModel.setAnimeDetailURL(animeDetailsURL);
                }
            }
            String episodeTitle = document.getElementsByTag("h1").text();
            if (episodeTitle != null) {
                if (episodeTitle.contains("Subtitle")) {
                    if (episodeTitle.contains("(TAMAT)")) {
                        episodeTitle = episodeTitle.substring(0, episodeTitle.length() - 26);
                    } else if (episodeTitle.contains("Tamat")) {
                        episodeTitle = episodeTitle.substring(0, episodeTitle.length() - 25);
                    } else {
                        episodeTitle = episodeTitle.substring(0, episodeTitle.length() - 19);
                    }
                } else {
                    Log.e("CUT?", "OF COURSE NO!");
                }
            }
            videoStreamResultModel.setEpisodeTitle(episodeTitle);
            //get next and prev URL
            Elements getElementsNextAndPrevEpisode = document.getElementsByClass("btn-ep-nav").select("a[href^=https://animeindo.cc/]");
            List<String> nextAndPrevURL = new ArrayList<>();
            if (nextAndPrevURL != null) {
                nextAndPrevURL.clear();
            }
            for (int position = 0; position < getElementsNextAndPrevEpisode.size(); position++) {
                Element element = getElementsNextAndPrevEpisode.get(position);
                String nextandprevurlsingle = element.absUrl("href");
                if (!nextandprevurlsingle.startsWith("https://animeindo.cc/anime/") && nextandprevurlsingle.contains("episode")) {
                    nextAndPrevURL.add(nextandprevurlsingle);
                }
            }
            Log.e("nextAndPrevURLstring", new Gson().toJson(nextAndPrevURL));
            String nextOrPrevURL, nextOrPrevEpisodeNumber;

            if (nextAndPrevURL.isEmpty()) {
                prevURL = null;
                nextURL = null;
            } else {
                if (nextAndPrevURL.size() < 2) {
                    nextOrPrevURL = nextAndPrevURL.get(0);
                    List<String> splitList;
                    splitList = Arrays.asList(nextOrPrevURL.split("-"));
                    nextOrPrevEpisodeNumber = splitList.get(splitList.indexOf("episode") + 1);
                    if (Integer.parseInt(nextOrPrevEpisodeNumber) < Integer.parseInt(nowEpisodeNumber)) {
                        prevURL = nextOrPrevURL;
                        nextURL = null;
                    } else {
                        prevURL = null;
                        nextURL = nextOrPrevURL;
                    }
                } else {
                    prevURL = nextAndPrevURL.get(0);
                    nextURL = nextAndPrevURL.get(1);
                }
            }

            videoStreamResultModel.setVideoNextUrl(nextURL);
            videoStreamResultModel.setVideoPrevUrl(prevURL);
            //Anime videos URL settings
            Elements getVideoEmbedURL1 = document.getElementsByClass("playeriframe");
            String getURLFromElementSrc = getVideoEmbedURL1.attr("src");
            String getURLFromElementLazy = getVideoEmbedURL1.attr("data-lazy-src");
            String getURLFromElementDataSrc = getVideoEmbedURL1.attr("data-src");
            if (getURLFromElementSrc.startsWith("//")) {
                getURLFromElementSrc = "https:" + getURLFromElementSrc;
            }
            videoStreamResultModel.setVideoUrl(getURLFromElementSrc);
            Log.e("allData", new Gson().toJson(videoStreamResultModel));
            watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataSuccess(videoStreamResultModel);
        } else {
            watchAnimeEpisodeInterface.onGetWatchAnimeEpisodeDataFailed();
        }

    }

}
