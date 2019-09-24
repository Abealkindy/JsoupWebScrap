package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityWatchAnimeEpisodeBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WatchAnimeEpisodeActivity extends AppCompatActivity {

    private ActivityWatchAnimeEpisodeBinding animeEpisodeBinding;
    private VideoStreamResultModel videoStreamResultModel = new VideoStreamResultModel();
    String episodeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeEpisodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_anime_episode);
        try {
            initUI();
            initEvent();
        } catch (Exception e) {
            Log.e("error!", e.getLocalizedMessage() + " " + e.getMessage());
        }
    }

    private void initEvent() {
        animeEpisodeBinding.buttonPreviousEpisode.setOnClickListener(v -> {
            String episodeURL = videoStreamResultModel.getPreviousEpisodeAnimeURL();
            if (!episodeURL.startsWith("https://animeindo.to/") | episodeURL.isEmpty() | episodeURL == null) {
                Toast.makeText(this, "It's already di ujung!", Toast.LENGTH_SHORT).show();
            } else {
                getAnimeWatchData(episodeURL);
            }
            Log.e("prevURL", episodeURL);
        });
        animeEpisodeBinding.buttonNextEpisode.setOnClickListener(v -> {
            String episodeURL = videoStreamResultModel.getNextEpisodeAnimeURL();
            if (episodeURL.isEmpty() | episodeURL == null) {
                Toast.makeText(this, "It's already di ujung!", Toast.LENGTH_SHORT).show();
            } else {
                if (!episodeURL.startsWith("https://animeindo.to/")) {
                    Toast.makeText(this, "It's already di ujung!", Toast.LENGTH_SHORT).show();
                } else {
                    getAnimeWatchData(episodeURL);
                }
            }
            Log.e("nextURL", episodeURL);
        });
        animeEpisodeBinding.cardNextToDetail.setOnClickListener(v -> getAnimeDetails());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        animeEpisodeBinding.webViewWatchAnime.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        animeEpisodeBinding.webViewWatchAnime.getSettings().setDomStorageEnabled(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setJavaScriptEnabled(true);
        animeEpisodeBinding.webViewWatchAnime.setWebChromeClient(new WebChromeClient());
        String episodeURL = getIntent().getStringExtra("animeEpisodeToWatch");
        String episodeThumb = getIntent().getStringExtra("animeEpisodeThumb");
        String episodeTitle = getIntent().getStringExtra("animeEpisodeTitle");
        if (episodeURL != null && episodeThumb != null && episodeTitle != null) {
            getAnimeWatchData(episodeURL);
            Picasso.get().load(episodeThumb).into(animeEpisodeBinding.imageCoverAnime);
            animeEpisodeBinding.textTitleEpisode.setText(episodeTitle);
            Log.e("nowURL", episodeURL);
            Log.e("nowThumbURL", episodeThumb);
            Log.e("nowTitle", episodeTitle);
        } else {
            Log.e("nowURL", "gak ada");
        }
    }

    private void getAnimeWatchData(String animeEpisodeToWatch) {
        String afterCut = animeEpisodeToWatch.substring(21);
        if (afterCut.endsWith("tamat/")) {
            episodeNumber = afterCut.substring(afterCut.length() - 9, afterCut.length() - 7);
        } else {
            episodeNumber = afterCut.substring(afterCut.length() - 3, afterCut.length() - 1);
        }
        Log.e("episodeNumber", episodeNumber);
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitRetrofit();
        apiEndPointService.getWatchAnimeData(afterCut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        parseHtmlToViewableContent(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(WatchAnimeEpisodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Toast.makeText(this, afterCut, Toast.LENGTH_SHORT).show();
    }

    private void parseHtmlToViewableContent(String result) {
        Document doc = Jsoup.parse(result);

        //Previous URL settings
        Elements getElementsPreviousEpisode = doc.select("a[href~=episode|movie|ova|ona]");
        if (getElementsPreviousEpisode.isEmpty()) {
            Log.e("element value", "null");
        } else {
            for (int position = 0; position < getElementsPreviousEpisode.size(); position++) {
                if (getElementsPreviousEpisode.size() < 2) {
                    Log.e("getAllEpisodeOrMovie", getElementsPreviousEpisode.get(position).absUrl("href"));
                    Element element = getElementsPreviousEpisode.get(0);
                    if (element == null) {
                        Log.e("element value", "null");
                    } else {
                        String previousEpisodeURL = element.absUrl("href");
                        if (!previousEpisodeURL.startsWith("https://animeindo.to/") || previousEpisodeURL.isEmpty() || previousEpisodeURL == null || previousEpisodeURL.startsWith("https://animeindo.to/anime/")) {
                            Log.e("element value", "null");
                        } else {
                            videoStreamResultModel.setPreviousEpisodeAnimeURL(previousEpisodeURL);
                            String getPrevEpisodeNumber;
                            if (videoStreamResultModel.getPreviousEpisodeAnimeURL().endsWith("tamat/")) {
                                getPrevEpisodeNumber = videoStreamResultModel.getPreviousEpisodeAnimeURL().substring(videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 9, videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 7);
                            } else {
                                getPrevEpisodeNumber = videoStreamResultModel.getPreviousEpisodeAnimeURL().substring(videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 3, videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 1);
                            }
                            if (Integer.parseInt(getPrevEpisodeNumber) >= Integer.parseInt(episodeNumber)) {
                                animeEpisodeBinding.buttonPreviousEpisode.setVisibility(View.GONE);
                            } else {
                                animeEpisodeBinding.buttonPreviousEpisode.setVisibility(View.VISIBLE);
                            }
                            Log.e("prevEps", getPrevEpisodeNumber);
                            Log.e("nowEps", episodeNumber);
                            Log.e("nowPrevURL", previousEpisodeURL);
                        }
                    }
                } else {
                    Log.e("getAllEpisodeOrMovie", getElementsPreviousEpisode.get(position).absUrl("href"));
                    Element element = getElementsPreviousEpisode.get(0);
                    if (element == null) {
                        Log.e("element value", "null");
                    } else {
                        String previousEpisodeURL = element.absUrl("href");
                        if (!previousEpisodeURL.startsWith("https://animeindo.to/") || previousEpisodeURL.isEmpty() || previousEpisodeURL == null) {
                            Log.e("element value", "null");
                        } else {
                            if (previousEpisodeURL.startsWith("https://animeindo.to/anime/")) {
                                Log.e("element value", "null");
                            } else {
                                videoStreamResultModel.setPreviousEpisodeAnimeURL(previousEpisodeURL);
                                String getPrevEpisodeNumber;
                                if (videoStreamResultModel.getPreviousEpisodeAnimeURL().endsWith("tamat/")) {
                                    getPrevEpisodeNumber = videoStreamResultModel.getPreviousEpisodeAnimeURL().substring(videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 9, videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 7);
                                } else {
                                    getPrevEpisodeNumber = videoStreamResultModel.getPreviousEpisodeAnimeURL().substring(videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 3, videoStreamResultModel.getPreviousEpisodeAnimeURL().length() - 1);
                                }
                                if (Integer.parseInt(getPrevEpisodeNumber) >= Integer.parseInt(episodeNumber)) {
                                    animeEpisodeBinding.buttonPreviousEpisode.setVisibility(View.GONE);
                                } else {
                                    Log.e("lolos?", "Ya");
                                    animeEpisodeBinding.buttonPreviousEpisode.setVisibility(View.VISIBLE);
                                }
                                Log.e("nowPrevURL", previousEpisodeURL);
                            }
                        }
                    }
                }
            }
        }

        //Next URL settings
        Elements getElementsNextEpisode = doc.select("a[href~=episode|movie|ova|ona]");
        if (getElementsNextEpisode.isEmpty()) {
            Log.e("nextElementsNull?", "Ya");
        } else {
            Log.e("nextElementsNull?", "Gak");
            for (int position = 0; position < getElementsNextEpisode.size(); position++) {
                if (getElementsNextEpisode.size() < 2) {
                    Log.e("nextElementGakAda2?", "Ya");
                    Element element = getElementsNextEpisode.get(0);
                    if (element == null) {
                        Log.e("nextElementGakAdaIndex?", "Ya");
                    } else {
                        Log.e("nextElementGakAdaIndex?", "Gak");
                        String nextEpisodeURL = element.absUrl("href");
                        if (!nextEpisodeURL.startsWith("https://animeindo.to/") | nextEpisodeURL.isEmpty() | nextEpisodeURL == null) {
                            Log.e("nextElementError?", "Ya");
                        } else {
                            Log.e("nextElementError?", "Gak");
                            String getNextEpisodeNumber;
                            videoStreamResultModel.setNextEpisodeAnimeURL(nextEpisodeURL);
                            if (videoStreamResultModel.getNextEpisodeAnimeURL().endsWith("tamat/")) {
                                getNextEpisodeNumber = videoStreamResultModel.getNextEpisodeAnimeURL().substring(videoStreamResultModel.getNextEpisodeAnimeURL().length() - 9, videoStreamResultModel.getNextEpisodeAnimeURL().length() - 7);
                            } else {
                                getNextEpisodeNumber = videoStreamResultModel.getNextEpisodeAnimeURL().substring(videoStreamResultModel.getNextEpisodeAnimeURL().length() - 3, videoStreamResultModel.getNextEpisodeAnimeURL().length() - 1);
                            }
                            Log.e("nowEps", episodeNumber);
                            if (Integer.parseInt(getNextEpisodeNumber) <= Integer.parseInt(episodeNumber)) {
                                animeEpisodeBinding.buttonNextEpisode.setVisibility(View.GONE);
                            } else {
                                animeEpisodeBinding.buttonNextEpisode.setVisibility(View.VISIBLE);
                            }
                            Log.e("nowNextURL", nextEpisodeURL);
                        }
                    }
                } else if (getElementsNextEpisode.size() == 3) {
                    Log.e("nextElementGakAda2?", "Ya");
                    Element element = getElementsNextEpisode.get(2);
                    if (element == null) {
                        Log.e("nextElementGakAdaIndex?", "Ya");
                    } else {
                        Log.e("nextElementGakAdaIndex?", "Gak");
                        String nextEpisodeURL = element.absUrl("href");
                        if (!nextEpisodeURL.startsWith("https://animeindo.to/") || nextEpisodeURL.isEmpty() || nextEpisodeURL == null) {
                            Log.e("nextElementError?", "Ya");
                        } else {
                            Log.e("nextElementError?", "Gak");
                            String getNextEpisodeNumber;
                            videoStreamResultModel.setNextEpisodeAnimeURL(nextEpisodeURL);
                            if (videoStreamResultModel.getNextEpisodeAnimeURL().endsWith("tamat/")) {
                                getNextEpisodeNumber = videoStreamResultModel.getNextEpisodeAnimeURL().substring(videoStreamResultModel.getNextEpisodeAnimeURL().length() - 9, videoStreamResultModel.getNextEpisodeAnimeURL().length() - 7);
                            } else {
                                getNextEpisodeNumber = videoStreamResultModel.getNextEpisodeAnimeURL().substring(videoStreamResultModel.getNextEpisodeAnimeURL().length() - 3, videoStreamResultModel.getNextEpisodeAnimeURL().length() - 1);
                            }
                            Log.e("nowEps", episodeNumber);
                            if (Integer.parseInt(getNextEpisodeNumber) <= Integer.parseInt(episodeNumber)) {
                                animeEpisodeBinding.buttonNextEpisode.setVisibility(View.GONE);
                            } else {
                                animeEpisodeBinding.buttonNextEpisode.setVisibility(View.VISIBLE);
                            }
                            Log.e("nowNextURL", nextEpisodeURL);
                        }
                    }
                } else {
                    Log.e("nextElementGakAda2?", "Gak");
                    Element element = getElementsNextEpisode.get(1);
                    if (element == null) {
                        Log.e("nextElementGakAdaIndex?", "Ya");
                    } else {
                        Log.e("nextElementGakAdaIndex?", "Gak");
                        String nextEpisodeURL = element.absUrl("href");
                        if (!nextEpisodeURL.startsWith("https://animeindo.to/") | nextEpisodeURL.isEmpty() | nextEpisodeURL == null) {
                            Log.e("nextElementError?", "Ya");
                            animeEpisodeBinding.buttonNextEpisode.setVisibility(View.GONE);
                        } else {
                            animeEpisodeBinding.buttonNextEpisode.setVisibility(View.VISIBLE);
                            Log.e("nextElementError?", "Gak");
                            videoStreamResultModel.setNextEpisodeAnimeURL(nextEpisodeURL);
                            Log.e("nowNextURL", nextEpisodeURL);
                        }
                    }
                }

            }
        }

        //Anime Details URL settings
        Elements getElementsAnimeDetails = doc.select("a[href^=https://animeindo.to/anime/]");
        if (getElementsAnimeDetails.isEmpty()) {
            Log.e("VideoDetailNull?", "Ya");
        } else {
            Log.e("VideodetailNull?", "Gak");
            for (Element element : getElementsAnimeDetails) {
                if (element == null) {
                    Log.e("VideoelementDetailNull?", "Ya");
                } else {
                    Log.e("VideoelementDetailNull?", "Gak");
                    String animeDetailsURL = element.absUrl("href");
                    if (!animeDetailsURL.startsWith("https://animeindo.to/") | animeDetailsURL.isEmpty() | animeDetailsURL == null) {
                        Log.e("VideoresultURLError?", "Ya");
                    } else {
                        Log.e("VideoresultURLError?", "Gak");
                        videoStreamResultModel.setAllEpisodeAnimeURL(animeDetailsURL);
                        Log.e("nowAnimeURL", animeDetailsURL);
                    }

                }
            }
        }

        //Anime videos URL settings
        Elements getVideoEmbedURL = doc.getElementsByClass("videoembed toogletheater");
        for (Element element : getVideoEmbedURL) {
            String getURLFromElement = element.getElementsByTag("iframe").attr("src");
            String animeVideoEmbedURL;
            if (getURLFromElement.startsWith("https:")) {
                animeVideoEmbedURL = getURLFromElement;
            } else {
                if (!getURLFromElement.startsWith("http:")) {
                    animeVideoEmbedURL = "http:" + getURLFromElement;
                } else {
                    animeVideoEmbedURL = getURLFromElement;
                }
            }
            String animeStreamURL = "<html><body><iframe width=\"100%\" height=\"235\" src=\"" + animeVideoEmbedURL + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            videoStreamResultModel.setVideoUrl(animeStreamURL);
            Log.e("nowVideoURL", animeVideoEmbedURL);
        }

        //Episode title
        Elements getEpisodeTitle = doc.getElementsByClass("epnav");
        for (Element element : getEpisodeTitle) {
            String getTitleFromElement = element.getElementsByTag("h3").text();
            videoStreamResultModel.setEpisodeTitle(getTitleFromElement);
            Log.e("episodeTitle", getTitleFromElement);
            animeEpisodeBinding.textTitleEpisode.setText(getTitleFromElement);
        }
        Log.e("allData", new Gson().toJson(videoStreamResultModel));
        animeEpisodeBinding.webViewWatchAnime.loadData(videoStreamResultModel.getVideoUrl(), "text/html", "utf-8");
    }

    private void getAnimeDetails() {
        if (videoStreamResultModel.getAllEpisodeAnimeURL().isEmpty()) {
            Toast.makeText(this, "Kosong!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, videoStreamResultModel.getAllEpisodeAnimeURL(), Toast.LENGTH_SHORT).show();
        }
    }

}
