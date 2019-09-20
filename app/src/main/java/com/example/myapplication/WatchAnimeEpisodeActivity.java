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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeEpisodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_anime_episode);
        initUI();
        initEvent();
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
            if (!episodeURL.startsWith("https://animeindo.to/") | episodeURL.isEmpty() | episodeURL == null) {
                Toast.makeText(this, "It's already di ujung!", Toast.LENGTH_SHORT).show();
            } else {
                getAnimeWatchData(episodeURL);
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
        animeEpisodeBinding.webViewWatchAnime.getSettings().setJavaScriptEnabled(true);
        animeEpisodeBinding.webViewWatchAnime.setWebChromeClient(new WebChromeClient());
        String episodeURL = getIntent().getStringExtra("animeEpisodeToWatch");
        if (episodeURL != null) {
            getAnimeWatchData(episodeURL);
            Log.e("nowURL", episodeURL);
        } else {
            Log.e("nowURL", "gak ada");
        }
    }

    private void getAnimeWatchData(String animeEpisodeToWatch) {
        String afterCut = animeEpisodeToWatch.substring(21);
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        Toast.makeText(this, afterCut, Toast.LENGTH_SHORT).show();
    }

    private void parseHtmlToViewableContent(String result) {
        Document doc = Jsoup.parse(result);

        Elements getElementsPreviousEpisode = doc.select("a[href~=episode|movie]");
        for (int position = 0; position < getElementsPreviousEpisode.size(); position++) {
            Log.e("getAllEpisodeOrMovie", getElementsPreviousEpisode.get(position).absUrl("href"));
            Element element = getElementsPreviousEpisode.get(0);
            if (element == null) {
                Toast.makeText(this, "It's already di ujung!", Toast.LENGTH_SHORT).show();
            } else {
                String previousEpisodeURL = element.absUrl("href");
                if (!previousEpisodeURL.startsWith("https://animeindo.to/") | previousEpisodeURL.isEmpty() | previousEpisodeURL == null) {
                    Toast.makeText(this, "It's already di prev ujung!", Toast.LENGTH_SHORT).show();
                } else {
                    videoStreamResultModel.setPreviousEpisodeAnimeURL(previousEpisodeURL);
                    Log.e("nowPrevURL", previousEpisodeURL);
                }
            }
        }

        Elements getElementsNextEpisode = doc.select("a[href~=episode|movie]");
        for (int position = 0; position < getElementsNextEpisode.size(); position++) {
            Log.e("getAllEpisodeOrMovie", getElementsNextEpisode.get(position).absUrl("href"));
            Element element = getElementsNextEpisode.get(1);
            if (element == null) {
                Toast.makeText(this, "It's already di ujung!", Toast.LENGTH_SHORT).show();
            } else {
                String nextEpisodeURL = element.absUrl("href");
                if (!nextEpisodeURL.startsWith("https://animeindo.to/") | nextEpisodeURL.isEmpty() | nextEpisodeURL == null) {
                    Toast.makeText(this, "It's already di next ujung!", Toast.LENGTH_SHORT).show();
                } else {
                    videoStreamResultModel.setNextEpisodeAnimeURL(nextEpisodeURL);
                    Log.e("nowNextURL", nextEpisodeURL);
                }
            }
        }

        Elements getElementsAnimeDetails = doc.select("a[href^=https://animeindo.to/anime/]");
        for (Element element : getElementsAnimeDetails) {
            String animeDetailsURL = element.absUrl("href");
            if (element == null | !animeDetailsURL.startsWith("https://animeindo.to/") | animeDetailsURL.isEmpty() | animeDetailsURL == null) {
                    Toast.makeText(this, "It's already di next ujung!", Toast.LENGTH_SHORT).show();
            } else {
                    videoStreamResultModel.setAllEpisodeAnimeURL(animeDetailsURL);
                    Log.e("nowAnimeURL", animeDetailsURL);
            }    
        }
        Elements getVideoEmbedURL = doc.getElementsByClass("videoembed toogletheater");
        for (Element element : getVideoEmbedURL) {
            String animeVideoEmbedURL = "http:" + element.getElementsByTag("iframe").attr("src");

            String animeStreamURL = "<html><body><iframe width=\"305\" height=\"230\" src=\"" + animeVideoEmbedURL + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            videoStreamResultModel.setVideoUrl(animeStreamURL);
            Log.e("nowVideoURL", animeVideoEmbedURL);
        }
        Log.e("allData", new Gson().toJson(videoStreamResultModel));
        animeEpisodeBinding.webViewWatchAnime.loadData(videoStreamResultModel.getVideoUrl(), "text/html", "utf-8");
    }

    private void getAnimeDetails() {

    }

}
