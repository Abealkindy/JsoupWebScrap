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

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WatchAnimeEpisodeActivity extends AppCompatActivity {

    private ActivityWatchAnimeEpisodeBinding animeEpisodeBinding;
    private VideoStreamResultModel videoStreamResultModel = new VideoStreamResultModel();
    private ProgressDialog progressDialog;

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
                startActivity(intent);
                finish();
            }
        });
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
        animeEpisodeBinding.webViewWatchAnime.getSettings().setDomStorageEnabled(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setJavaScriptEnabled(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setLoadWithOverviewMode(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setAllowContentAccess(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setAllowFileAccess(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setAllowFileAccessFromFileURLs(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setAllowUniversalAccessFromFileURLs(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setSupportZoom(true);
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

        //Anime videos URL settings
        Elements getVideoEmbedURL = doc.select("iframe[allowfullscreen=allowfullscreen]");
        String getURLFromElement = getVideoEmbedURL.attr("src");
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
        String animeStreamURL = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"" + animeVideoEmbedURL + "\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>";
        videoStreamResultModel.setVideoUrl(animeStreamURL);
        Log.e("allData", new Gson().toJson(videoStreamResultModel));
        animeEpisodeBinding.webViewWatchAnime.loadData(videoStreamResultModel.getVideoUrl(), "text/html", "utf-8");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
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
