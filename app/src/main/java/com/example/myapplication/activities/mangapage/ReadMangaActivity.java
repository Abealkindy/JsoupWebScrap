package com.example.myapplication.activities.mangapage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RecyclerAllChapterAdapter;
import com.example.myapplication.adapters.RecyclerReadMangaAdapter;
import com.example.myapplication.databinding.ActivityReadMangaBinding;
import com.example.myapplication.databinding.SelectChapterDialogBinding;
import com.example.myapplication.listener.OnSwipeTouchListener;
import com.example.myapplication.models.mangamodels.ReadMangaModel;
import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReadMangaActivity extends AppCompatActivity implements RecyclerAllChapterAdapter.ClickListener {
    private ActivityReadMangaBinding readMangaBinding;
    private ReadMangaModel readMangaModel = new ReadMangaModel();
    ProgressDialog progressDialog;
    private List<ReadMangaModel.AllChapterDatas> allChapterDatasList = new ArrayList<>();
    List<ReadMangaModel.AllChapterDatas> allChapterDatas;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readMangaBinding = DataBindingUtil.setContentView(this, R.layout.activity_read_manga);
        setUI();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUI() {
        progressDialog = new ProgressDialog(ReadMangaActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        String appBarColorStatus = getIntent().getStringExtra("appBarColorStatus");
        String chapterURL = getIntent().getStringExtra("chapterURL");

        if (chapterURL != null) {
            getReadMangaContentData(chapterURL);
        } else {
            Toast.makeText(this, "Chapter URL Null!", Toast.LENGTH_SHORT).show();
        }
        if (appBarColorStatus != null) {
            if (appBarColorStatus.equalsIgnoreCase(getResources().getString(R.string.manga_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
            } else if (appBarColorStatus.equalsIgnoreCase(getResources().getString(R.string.manhua_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manhua_color));
            } else if (appBarColorStatus.equalsIgnoreCase(getResources().getString(R.string.manhwa_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manhwa_color));
            } else if (appBarColorStatus.equalsIgnoreCase(getResources().getString(R.string.oneshot_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
            } else if (appBarColorStatus.equalsIgnoreCase(getResources().getString(R.string.mangaoneshot_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
            }
        } else {
            readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
        }
        readMangaBinding.nestedBase.setOnTouchListener(new OnSwipeTouchListener(ReadMangaActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if (readMangaModel.getNextMangaURL().isEmpty() || readMangaModel.getNextMangaURL() == null) {
                    Toast.makeText(ReadMangaActivity.this, "There's no more next chapter", Toast.LENGTH_SHORT).show();
                } else {
                    getReadMangaContentData(readMangaModel.getNextMangaURL());
                }
                Log.e("swipeStatus", "Left");
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if (readMangaModel.getPreviousMangaURL().isEmpty() || readMangaModel.getPreviousMangaURL() == null) {
                    Toast.makeText(ReadMangaActivity.this, "There's no more previous chapter", Toast.LENGTH_SHORT).show();
                } else {
                    getReadMangaContentData(readMangaModel.getPreviousMangaURL());
                }
                Log.e("swipeStatus", "Right");
            }
        });
    }

    public void getReadMangaContentData(String chapterURL) {
        progressDialog.show();
        String URLAfterCut = chapterURL.substring(22);
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitMangaRetrofit();
        apiEndPointService.getReadMangaData(URLAfterCut)
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
                        Toast.makeText(ReadMangaActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static List<ReadMangaModel.AllChapterDatas> removeDuplicates(List<ReadMangaModel.AllChapterDatas> list) {

        // Create a new LinkedHashSet
        Set<ReadMangaModel.AllChapterDatas> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }

    @SuppressLint({"ClickableViewAccessibility", "LongLogTag"})
    private void parseHtmlToViewableContent(String result) {
        Document doc = Jsoup.parse(result);
        Elements getChapterTitle = doc.getElementsByTag("h1");
        String chapterTitle = getChapterTitle.text();
        readMangaBinding.textViewChapterTitle.setText(chapterTitle);

        Elements getAllChapterDatas = doc.select("option[value^=https://komikcast.com/chapter/]");
        if (allChapterDatasList != null || !allChapterDatasList.isEmpty()) {
            allChapterDatasList.clear();
        }
        for (Element element : getAllChapterDatas) {
            String allChapterTitles = element.getElementsContainingOwnText("Chapter").text();
            String allChapterURLs = element.absUrl("value");
            ReadMangaModel.AllChapterDatas chapterDatas = new ReadMangaModel().new AllChapterDatas();
            chapterDatas.setChapterTitle(allChapterTitles);
            chapterDatas.setChapterUrl(allChapterURLs);
            allChapterDatasList.add(chapterDatas);
        }
        allChapterDatas = removeDuplicates(allChapterDatasList);
        Elements getPreviousChapterURL = doc.select("a[rel=prev]");
        if (getPreviousChapterURL == null || getPreviousChapterURL.isEmpty()) {
            readMangaBinding.buttonPrevChap.setVisibility(View.GONE);
        } else {
            Element prevElement = getPreviousChapterURL.get(0);
            String previousChapterUrl = prevElement.absUrl("href");
            readMangaBinding.buttonPrevChap.setVisibility(View.VISIBLE);
            readMangaModel.setPreviousMangaURL(previousChapterUrl);
            readMangaBinding.buttonPrevChap.setOnClickListener(v -> getReadMangaContentData(previousChapterUrl));
        }

        Elements getNextChapterURL = doc.select("a[rel=next]");
        if (getNextChapterURL == null || getNextChapterURL.isEmpty()) {
            readMangaBinding.buttonNextChap.setVisibility(View.GONE);
        } else {
            Element nextElement = getNextChapterURL.get(0);
            String nextChapterUrl = nextElement.absUrl("href");
            readMangaBinding.buttonNextChap.setVisibility(View.VISIBLE);
            readMangaModel.setNextMangaURL(nextChapterUrl);
            readMangaBinding.buttonNextChap.setOnClickListener(v -> getReadMangaContentData(nextChapterUrl));
        }
        Elements getMangaImageContentNewerSeries = doc.select("img[src^=https://cdn.komikcast.com/wp-content/]");
        Elements getMangaImageContentOlderSeries = doc.select("img[src^=https://i0.wp.com/lh3.googleusercontent.com/]");

        Elements getMangaImageContentOtherSeries = doc.select("img[src^=https://docs.google.com/uc?export=view]");
        Elements getMangaImageContentOtherAgainSeries = doc.select("img[src^=https://4.bp.blogspot.com/]");
        Elements getMangaImageContentOther1Series = doc.select("img[src^=https://3.bp.blogspot.com/]");
        Elements getMangaImageContentOther2Series = doc.select("img[src^=https://1.bp.blogspot.com/]");
        Elements getMangaImageContentOther3Series = doc.select("img[src^=https://2.bp.blogspot.com/]");
        if (readMangaModel.getImageContent() != null || !readMangaModel.getImageContent().isEmpty()) {
            readMangaModel.getImageContent().clear();
        }
        if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())) {
            Log.e("getContentWithNewerandgetContentWithOlder", "null");
            for (Element element : getMangaImageContentOtherSeries) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        }
        if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                && (getMangaImageContentOtherSeries.isEmpty() || getMangaImageContentOtherSeries == null)) {
            for (Element element : getMangaImageContentOtherAgainSeries) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        }
        if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                && (getMangaImageContentOtherSeries == null || getMangaImageContentOtherSeries.isEmpty())
                && (getMangaImageContentOtherAgainSeries.isEmpty() || getMangaImageContentOtherAgainSeries == null)) {
            for (Element element : getMangaImageContentOther1Series) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        }
        if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                && (getMangaImageContentOtherSeries.isEmpty() || getMangaImageContentOtherSeries == null)
                && (getMangaImageContentOtherAgainSeries.isEmpty() || getMangaImageContentOtherAgainSeries == null)
                && (getMangaImageContentOther1Series.isEmpty() || getMangaImageContentOther1Series == null)) {
            for (Element element : getMangaImageContentOther2Series) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        }
        if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                && (getMangaImageContentOtherSeries.isEmpty() || getMangaImageContentOtherSeries == null)
                && (getMangaImageContentOtherAgainSeries.isEmpty() || getMangaImageContentOtherAgainSeries == null)
                && (getMangaImageContentOther1Series.isEmpty() || getMangaImageContentOther1Series == null)
                && (getMangaImageContentOther2Series.isEmpty() || getMangaImageContentOther2Series == null)) {
            for (Element element : getMangaImageContentOther3Series) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        }
        if (getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty()) {
            Log.e("getContentWithNewer", "null");
            for (Element element : getMangaImageContentOlderSeries) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        } else {
            Log.e("getContentWithNewer", "success");
            for (Element element : getMangaImageContentNewerSeries) {
                String mangaContent = element.absUrl("src");
                readMangaModel.getImageContent().add(mangaContent);
            }
        }

        Log.e("mangaChapterContent", new Gson().toJson(readMangaModel.getImageContent()));
        readMangaBinding.recyclerImageContentManga.setHasFixedSize(true);
        RecyclerReadMangaAdapter mangaRecyclerNewReleasesAdapter = new RecyclerReadMangaAdapter(ReadMangaActivity.this, readMangaModel.getImageContent());
        readMangaBinding.recyclerImageContentManga.setAdapter(mangaRecyclerNewReleasesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        readMangaBinding.recyclerImageContentManga.setLayoutManager(linearLayoutManager);
        readMangaBinding.cardSelectAllChapter.setOnClickListener(v -> {
            dialog = new Dialog(ReadMangaActivity.this);
            SelectChapterDialogBinding chapterDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(ReadMangaActivity.this), R.layout.select_chapter_dialog, null, false);
            dialog.setContentView(chapterDialogBinding.getRoot());
            dialog.setTitle("Select other chapter");
            chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true);
            chapterDialogBinding.recyclerAllChapters.setLayoutManager(new LinearLayoutManager(ReadMangaActivity.this));
            chapterDialogBinding.recyclerAllChapters.setAdapter(new RecyclerAllChapterAdapter(ReadMangaActivity.this, allChapterDatas));
            dialog.show();
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ReadMangaActivity.this, MangaReleaseListActivity.class));
        finish();
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
    public void onItemClick(int position, View v) {
        dialog.dismiss();
        getReadMangaContentData(allChapterDatas.get(position).getChapterUrl());
    }

}
