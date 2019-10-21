package com.example.myapplication.activities.animepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.R;
import com.example.myapplication.networks.RetrofitConfig;
import com.example.myapplication.models.animemodels.VideoStreamResultModel;
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
    private ProgressDialog progressDialog;
    private String nowEpisodeNumber, nextEpisodeNumber, previousEpisodeNumber, nextURL, prevURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeEpisodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_anime_episode);
        initUI();
        initEvent();
    }

    private void initEvent() {
        animeEpisodeBinding.animeInfoButton.setOnClickListener(v -> {
            if (videoStreamResultModel.getAnimeDetailURL() == null || videoStreamResultModel.getEpisodeTitle() == null) {
                Log.e("URL NULL", "YES");
            } else {
                Intent intent = new Intent(WatchAnimeEpisodeActivity.this, AnimeDetailActivity.class);
                intent.putExtra("animeDetailURL", videoStreamResultModel.getAnimeDetailURL());
                intent.putExtra("animeDetailTitle", videoStreamResultModel.getEpisodeTitle());
                intent.putExtra("animeDetailType", videoStreamResultModel.getAnimeType());
                intent.putExtra("animeDetailStatus", videoStreamResultModel.getAnimeStatus());
                intent.putExtra("animeDetailThumb", videoStreamResultModel.getAnimeThumb());
                startActivity(intent);
                finish();
            }
        });
        animeEpisodeBinding.prevEpisodeButton.setOnClickListener(view -> getAnimeWatchData(prevURL));
        animeEpisodeBinding.nextEpisodeButton.setOnClickListener(view -> getAnimeWatchData(nextURL));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        progressDialog = new ProgressDialog(WatchAnimeEpisodeActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
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
        String episodeTitle = getIntent().getStringExtra("animeEpisodeTitle");
        String episodeType = getIntent().getStringExtra("animeEpisodeType");
        String episodeStatus = getIntent().getStringExtra("animeEpisodeStatus");
        String episodeThumb = getIntent().getStringExtra("animeEpisodeThumb");
        if (episodeURL != null && episodeTitle != null && episodeType != null) {
            if (episodeType.equalsIgnoreCase(getResources().getString(R.string.series_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.blue_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.blue_series_color));
            } else if (episodeType.equalsIgnoreCase(getResources().getString(R.string.ona_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.purple_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.purple_series_color));
            } else if (episodeType.equalsIgnoreCase(getResources().getString(R.string.movie_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.green_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.green_series_color));
                animeEpisodeBinding.nextEpisodeButton.setVisibility(View.GONE);
                animeEpisodeBinding.prevEpisodeButton.setVisibility(View.GONE);
            } else if (episodeType.equalsIgnoreCase(getResources().getString(R.string.la_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.red_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.red_series_color));
            } else if (episodeType.equalsIgnoreCase(getResources().getString(R.string.special_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.orange_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.orange_series_color));
            } else if (episodeType.equalsIgnoreCase(getResources().getString(R.string.ova_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.pink_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.pink_series_color));
            }
            animeEpisodeBinding.textAnimeTitleWatch.setText(episodeTitle);
            videoStreamResultModel.setEpisodeTitle(episodeTitle);
            videoStreamResultModel.setAnimeType(episodeType);
            videoStreamResultModel.setAnimeStatus(episodeStatus);
            videoStreamResultModel.setAnimeThumb(episodeThumb);
            getAnimeWatchData(episodeURL);
        } else {
            Log.e("nowURL", "gak ada");
        }
    }

    private void getAnimeWatchData(String animeEpisodeToWatch) {
        progressDialog.show();
        String afterCut = animeEpisodeToWatch.substring(21);
        nowEpisodeNumber = afterCut.substring(afterCut.indexOf("episode-") + 8);
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitAnimeRetrofit();
        apiEndPointService.getWatchAnimeData(afterCut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        progressDialog.dismiss();
                        parseHtmlToViewableContent(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(WatchAnimeEpisodeActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void parseHtmlToViewableContent(String result) {
        Document doc = Jsoup.parse(result);

        //Anime Details URL settings
        Elements getElementsAnimeDetails = doc.select("a[href^=https://animeindo.to/anime/]");
        if (getElementsAnimeDetails.isEmpty()) {
            Log.e("VideoDetailNull?", "Ya");
        } else {
            String animeDetailsURL = getElementsAnimeDetails.attr("href");
            if (!animeDetailsURL.startsWith("https://animeindo.to/")) {
                Log.e("VideoresultURLError?", "Ya");
                videoStreamResultModel.setAnimeDetailURL(null);
            } else {
                videoStreamResultModel.setAnimeDetailURL(animeDetailsURL);
            }
        }

        //get next and prev URL
        Elements getElementsNextAndPrevEpisode = doc.select("a[href^=https://animeindo.to/]");
        List<String> nextAndPrevURL = new ArrayList<>();
        if (nextAndPrevURL != null) {
            nextAndPrevURL.clear();
        }
        for (int position = 0; position < getElementsNextAndPrevEpisode.size(); position++) {
            Element element = getElementsNextAndPrevEpisode.get(position);
            String nextandprevurlsingle = element.absUrl("href");
            if (!nextandprevurlsingle.startsWith("https://animeindo.to/anime/") && nextandprevurlsingle.contains("episode")) {
                nextAndPrevURL.add(nextandprevurlsingle);
            }
        }
        Log.e("nextAndPrevURLstring", new Gson().toJson(nextAndPrevURL));
        String nextOrPrevURL, nextOrPrevEpisodeNumber;
        if (nextAndPrevURL.isEmpty()) {
            animeEpisodeBinding.prevEpisodeButton.setVisibility(View.GONE);
            animeEpisodeBinding.nextEpisodeButton.setVisibility(View.GONE);
        } else {
            if (nextAndPrevURL.size() < 2) {
                nextOrPrevURL = nextAndPrevURL.get(0);
                nextOrPrevEpisodeNumber = nextOrPrevURL.substring(nextOrPrevURL.indexOf("episode-") + 8);
                if (nowEpisodeNumber.endsWith("/")) {
                    if (Integer.parseInt(nextOrPrevEpisodeNumber) < Integer.parseInt(nowEpisodeNumber.substring(0, nowEpisodeNumber.length() - 1))) {
                        prevURL = nextAndPrevURL.get(0);
                        nextURL = null;
                        animeEpisodeBinding.prevEpisodeButton.setVisibility(View.VISIBLE);
                        animeEpisodeBinding.nextEpisodeButton.setVisibility(View.GONE);
                    } else if (Integer.parseInt(nextOrPrevEpisodeNumber) > Integer.parseInt(nowEpisodeNumber.substring(0, nowEpisodeNumber.length() - 1))) {
                        prevURL = null;
                        nextURL = nextAndPrevURL.get(0);
                        animeEpisodeBinding.prevEpisodeButton.setVisibility(View.GONE);
                        animeEpisodeBinding.nextEpisodeButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (Integer.parseInt(nextOrPrevEpisodeNumber) < Integer.parseInt(nowEpisodeNumber)) {
                        prevURL = nextAndPrevURL.get(0);
                        nextURL = null;
                        animeEpisodeBinding.prevEpisodeButton.setVisibility(View.VISIBLE);
                        animeEpisodeBinding.nextEpisodeButton.setVisibility(View.GONE);
                    } else if (Integer.parseInt(nextOrPrevEpisodeNumber) > Integer.parseInt(nowEpisodeNumber)) {
                        prevURL = null;
                        nextURL = nextAndPrevURL.get(0);
                        animeEpisodeBinding.prevEpisodeButton.setVisibility(View.GONE);
                        animeEpisodeBinding.nextEpisodeButton.setVisibility(View.VISIBLE);
                    }
                }
            } else if (nextAndPrevURL.size() == 2) {
                prevURL = nextAndPrevURL.get(0);
                nextURL = nextAndPrevURL.get(1);
                previousEpisodeNumber = prevURL.substring(prevURL.indexOf("episode-") + 8);
                nextEpisodeNumber = nextURL.substring(nextURL.indexOf("episode-") + 8);
                animeEpisodeBinding.prevEpisodeButton.setVisibility(View.VISIBLE);
                animeEpisodeBinding.nextEpisodeButton.setVisibility(View.VISIBLE);
            }
        }


        //Anime videos URL settings
        Elements getVideoEmbedURL = doc.select("iframe[allowfullscreen=allowfullscreen]");
        String getURLFromElementSrc = getVideoEmbedURL.attr("src");
        String getURLFromElementLazy = getVideoEmbedURL.attr("data-lazy-src");
        String animeVideoEmbedURL;
        if (!getURLFromElementSrc.startsWith("//")) {
            if (getURLFromElementLazy.startsWith("https:")) {
                animeVideoEmbedURL = getURLFromElementLazy;
            } else {
                if (!getURLFromElementLazy.startsWith("http:")) {
                    animeVideoEmbedURL = "http:" + getURLFromElementLazy;
                } else {
                    animeVideoEmbedURL = getURLFromElementLazy;
                }
            }
            String animeStreamURL = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"" + animeVideoEmbedURL + "\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>";
            videoStreamResultModel.setVideoUrl(animeStreamURL);
            Log.e("allData", new Gson().toJson(videoStreamResultModel));
            animeEpisodeBinding.webViewWatchAnime.loadData(videoStreamResultModel.getVideoUrl(), "text/html", "utf-8");
        } else if (!getURLFromElementLazy.startsWith("//")) {
            if (getURLFromElementSrc.startsWith("https:")) {
                animeVideoEmbedURL = getURLFromElementSrc;
            } else {
                if (!getURLFromElementSrc.startsWith("http:")) {
                    animeVideoEmbedURL = "http:" + getURLFromElementSrc;
                } else {
                    animeVideoEmbedURL = getURLFromElementSrc;
                }
            }
            String animeStreamURL = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"" + animeVideoEmbedURL + "\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>";
            videoStreamResultModel.setVideoUrl(animeStreamURL);
            Log.e("allData", new Gson().toJson(videoStreamResultModel));
            animeEpisodeBinding.webViewWatchAnime.loadData(videoStreamResultModel.getVideoUrl(), "text/html", "utf-8");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
