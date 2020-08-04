package com.example.myapplication.activities.animepage.watch_anime_mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.activities.animepage.anime_detail_mvp.AnimeDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.animemodels.VideoStreamResultModel;
import com.example.myapplication.databinding.ActivityWatchAnimeEpisodeBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatchAnimeEpisodeActivity extends AppCompatActivity implements WatchAnimeEpisodeInterface {

    private ActivityWatchAnimeEpisodeBinding animeEpisodeBinding;
    private VideoStreamResultModel videoStreamResultModel = new VideoStreamResultModel();
    private ProgressDialog progressDialog;
    private String nowEpisodeNumber = "", animeType = "", animeStatus = "", animeThumb = "";
    private WatchAnimeEpisodePresenter episodePresenter = new WatchAnimeEpisodePresenter(this);
    private boolean isShowAtasBawah = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeEpisodeBinding = ActivityWatchAnimeEpisodeBinding.inflate(getLayoutInflater());
        setContentView(animeEpisodeBinding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(0x10);
        initUI();
        initEvent();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        animeEpisodeBinding.animeInfoButton.setOnClickListener(v -> {
            if (videoStreamResultModel.getAnimeDetailURL() == null || videoStreamResultModel.getEpisodeTitle() == null) {
                Log.e("URL NULL", "YES");
            } else {
                Intent intent = new Intent(WatchAnimeEpisodeActivity.this, AnimeDetailActivity.class);
                intent.putExtra("animeDetailURL", videoStreamResultModel.getAnimeDetailURL());
                intent.putExtra("animeDetailTitle", videoStreamResultModel.getEpisodeTitle());
                intent.putExtra("animeDetailType", animeType);
                intent.putExtra("animeDetailStatus", animeStatus);
                intent.putExtra("animeDetailThumb", animeThumb);
                startActivity(intent);
                finish();
            }
        });
        animeEpisodeBinding.prevEpisodeButton.setOnClickListener(view -> getWatchAnimeDataFromWebView(videoStreamResultModel.getVideoPrevUrl()));
        animeEpisodeBinding.nextEpisodeButton.setOnClickListener(view -> getWatchAnimeDataFromWebView(videoStreamResultModel.getVideoNextUrl()));
        animeEpisodeBinding.buttonFull.setOnClickListener(v -> {
            isShowAtasBawah = !isShowAtasBawah;
            isAtasBawahShow(isShowAtasBawah);
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        progressDialog = new ProgressDialog(WatchAnimeEpisodeActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        animeEpisodeBinding.webViewWatchAnime.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/12");
        animeEpisodeBinding.webViewWatchAnime.getSettings().setJavaScriptEnabled(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setLoadWithOverviewMode(true);
        animeEpisodeBinding.webViewWatchAnime.getSettings().setUseWideViewPort(false);
        String episodeURL = getIntent().getStringExtra("animeEpisodeToWatch");
        String episodeTitle = getIntent().getStringExtra("animeEpisodeTitle");
        animeType = getIntent().getStringExtra("animeEpisodeType");
        animeStatus = getIntent().getStringExtra("animeEpisodeStatus");
        animeThumb = getIntent().getStringExtra("animeEpisodeThumb");
        if (episodeURL != null && episodeTitle != null && animeType != null) {
            if (animeType.equalsIgnoreCase(getResources().getString(R.string.series_string)) ||
                    animeType.contains(getResources().getString(R.string.series_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.blue_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.blue_series_color));
            } else if (animeType.equalsIgnoreCase(getResources().getString(R.string.ona_string)) ||
                    animeType.contains(getResources().getString(R.string.ona_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.purple_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.purple_series_color));
            } else if (animeType.equalsIgnoreCase(getResources().getString(R.string.movie_string)) ||
                    animeType.contains(getResources().getString(R.string.movie_string_lower))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.green_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.green_series_color));
                animeEpisodeBinding.nextEpisodeButton.setVisibility(View.GONE);
                animeEpisodeBinding.prevEpisodeButton.setVisibility(View.GONE);
            } else if (animeType.equalsIgnoreCase(getResources().getString(R.string.la_string)) ||
                    animeType.contains(getResources().getString(R.string.la_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.red_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.red_series_color));
            } else if (animeType.equalsIgnoreCase(getResources().getString(R.string.special_string)) ||
                    animeType.contains(getResources().getString(R.string.special_string_lower))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.orange_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.orange_series_color));
            } else if (animeType.equalsIgnoreCase(getResources().getString(R.string.ova_string)) ||
                    animeType.contains(getResources().getString(R.string.ova_string))) {
                animeEpisodeBinding.linearAbove.setBackgroundColor(getResources().getColor(R.color.pink_series_color));
                animeEpisodeBinding.linearBelow.setBackgroundColor(getResources().getColor(R.color.pink_series_color));
            }
            animeEpisodeBinding.textAnimeTitleWatch.setText(episodeTitle);
            getWatchAnimeDataFromWebView(episodeURL);
        } else {
            Log.e("nowURL", "gak ada");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void getWatchAnimeDataFromWebView(String episodeURL) {
        progressDialog.show();
        List<String> splitList = Arrays.asList(episodeURL.split("-"));
        nowEpisodeNumber = splitList.get(splitList.indexOf("episode") + 1);
        animeEpisodeBinding.webViewWatchAnimeBg.getSettings().setJavaScriptEnabled(true);
        animeEpisodeBinding.webViewWatchAnimeBg.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        animeEpisodeBinding.webViewWatchAnimeBg.loadUrl(episodeURL);
        if (animeEpisodeBinding.webViewWatchAnimeBg.isShown()) {
            animeEpisodeBinding.webViewWatchAnimeBg.reload();
        }
        animeEpisodeBinding.webViewWatchAnimeBg.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
            }

            public void onPageFinished(WebView view, String url) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                }, 5000);
            }

        });
    }

    public void showToast(String getURLFromElementSrc, String message) {
        runOnUiThread(() -> {
            Toast.makeText(WatchAnimeEpisodeActivity.this, message, Toast.LENGTH_SHORT).show();
            if (!getURLFromElementSrc.isEmpty()) {
                episodePresenter.getEpisodeToWatchData(nowEpisodeNumber, message);
            } else {
                animeEpisodeBinding.webViewWatchAnimeBg.reload();
                Toast.makeText(WatchAnimeEpisodeActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class LoadListener {
        public String content = "";

        @JavascriptInterface
        public void processHTML(String html) {
            Document doc = Jsoup.parse(html);
            Elements getVideoEmbedURL = doc.getElementsByClass("playeriframe");
            String getURLFromElementSrc = getVideoEmbedURL.attr("src");
            Log.e("result", getURLFromElementSrc);
            nextToAct(getURLFromElementSrc, html);
        }

        private void nextToAct(String getURLFromElementSrc, String content) {
            showToast(getURLFromElementSrc, content);
        }
    }

    private void isAtasBawahShow(boolean showStatus) {
        if (showStatus) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) animeEpisodeBinding.webViewWatchAnime.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            animeEpisodeBinding.webViewWatchAnime.setLayoutParams(layoutParams);
            animeEpisodeBinding.linearAbove.setVisibility(View.GONE);
            animeEpisodeBinding.linearBelow.setVisibility(View.GONE);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) animeEpisodeBinding.webViewWatchAnime.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) getResources().getDimension(R.dimen._300sdp);
            animeEpisodeBinding.webViewWatchAnime.setLayoutParams(layoutParams);
            animeEpisodeBinding.linearAbove.setVisibility(View.VISIBLE);
            animeEpisodeBinding.linearBelow.setVisibility(View.VISIBLE);
        }
    }

    private void parseHtmlToViewableContent(VideoStreamResultModel result) {
        videoStreamResultModel = result;
        if (result != null) {
            animeEpisodeBinding.textAnimeTitleWatch.setText(result.getEpisodeTitle());
            if (result.getVideoPrevUrl() != null && !result.getVideoPrevUrl().isEmpty()) {
                animeEpisodeBinding.prevEpisodeButton.setVisibility(View.VISIBLE);
            } else {
                animeEpisodeBinding.prevEpisodeButton.setVisibility(View.GONE);
            }

            if (result.getVideoNextUrl() != null && !result.getVideoNextUrl().isEmpty()) {
                animeEpisodeBinding.nextEpisodeButton.setVisibility(View.VISIBLE);
            } else {
                animeEpisodeBinding.nextEpisodeButton.setVisibility(View.GONE);
            }
            if (result.getVideoUrl() != null && !result.getVideoUrl().isEmpty()) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                double screenWidth = displayMetrics.widthPixels / displayMetrics.density;
                double screenHeight = displayMetrics.heightPixels / displayMetrics.density;
//                String imageURLModify = "<html><body style=\"margin: 0;\"><iframe width=\"" + screenWidth + "\" height=\"" + screenHeight + "\" src=\"" + result.getVideoUrl() + "\"></iframe></body></html>";
//                String imageURLModify = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" src=\"" + result.getVideoUrl() + "\" allowfullscreen=\"allowfullscreen\"></iframe></body></html>";
//                animeEpisodeBinding.webViewWatchAnime.loadData(imageURLModify, "text/html", "UTF-8");
                if (animeEpisodeBinding.webViewWatchAnime.isShown()) {
                    animeEpisodeBinding.webViewWatchAnime.reload();
                }
                animeEpisodeBinding.webViewWatchAnime.loadUrl(result.getVideoUrl());
            }

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

    @Override
    public void onGetWatchAnimeEpisodeDataSuccess(VideoStreamResultModel watchHTMLResult) {
        progressDialog.dismiss();
        parseHtmlToViewableContent(watchHTMLResult);
    }

    @Override
    public void onGetWatchAnimeEpisodeDataFailed() {
        progressDialog.dismiss();
        Toast.makeText(WatchAnimeEpisodeActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
    }
}
