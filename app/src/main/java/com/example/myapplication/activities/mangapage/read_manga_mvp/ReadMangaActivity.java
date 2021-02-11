package com.example.myapplication.activities.mangapage.read_manga_mvp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerAllChapterAdapter;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerReadMangaAdapter;
import com.example.myapplication.databinding.ActivityReadMangaBinding;
import com.example.myapplication.databinding.SelectChapterDialogBinding;
import com.example.myapplication.localstorages.manga_local.read_history.MangaHistoryModel;
import com.example.myapplication.models.mangamodels.ReadMangaModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.MyApp.localAppDB;

public class ReadMangaActivity extends AppCompatActivity implements RecyclerAllChapterAdapter.ClickListener, RecyclerReadMangaAdapter.ClickItemListener, ReadMangaInterface {
    private ActivityReadMangaBinding readMangaBinding;
    private ReadMangaModel readMangaModel;
    private ReadMangaPresenter readMangaPresenter = new ReadMangaPresenter(this);
    private List<ReadMangaModel.AllChapterDatas> allChapterDatasList = new ArrayList<>();
    private Dialog dialog;
    private String chapterURL = "", chapterNextURL = "", chapterPrevURL = "", detailURL = "", appColorBarStatus = "", readFrom = "", chapterThumb = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readMangaBinding = ActivityReadMangaBinding.inflate(getLayoutInflater());
        setContentView(readMangaBinding.getRoot());
        setUI();
        initEvent();
    }

    private void initEvent() {
        readMangaBinding.nextChapButton.setOnClickListener(v -> {
            if (chapterNextURL != null && !chapterNextURL.isEmpty()) {
                getReadMangaContentData(chapterNextURL);
            }
        });
        readMangaBinding.prevChapButton.setOnClickListener(v -> {
            if (chapterPrevURL != null && !chapterPrevURL.isEmpty()) {
                getReadMangaContentData(chapterPrevURL);
            }
        });
        readMangaBinding.refreshButton.setOnClickListener(v -> {
            if (chapterURL != null && !chapterURL.isEmpty()) {
                getReadMangaContentData(chapterURL);
            }
        });
        readMangaBinding.mangaInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReadMangaActivity.this, MangaDetailActivity.class);
            intent.putExtra("detailURL", detailURL);
            intent.putExtra("detailType", appColorBarStatus);
            intent.putExtra("detailTitle", "");
            intent.putExtra("detailRating", "");
            intent.putExtra("detailStatus", "");
            intent.putExtra("detailThumb", chapterThumb);
            intent.putExtra("chapterURL", chapterURL);
            intent.putExtra("detailFrom", "MangaRead");
            startActivity(intent);
//            finish();
        });
        readMangaBinding.mangaShareChapterButton.setOnClickListener(v -> {
            Intent shareToOther = new Intent(Intent.ACTION_SEND);
            shareToOther.setType("text/plain");
            shareToOther.putExtra(Intent.EXTRA_SUBJECT, "Share " + appColorBarStatus + " " + readMangaModel.getChapterTitle() + " ke teman-teman mu\n");
            shareToOther.putExtra(Intent.EXTRA_TEXT, "Share " + appColorBarStatus + " " + readMangaModel.getChapterTitle() + " ke teman-teman mu\n" + chapterURL);
            startActivity(Intent.createChooser(shareToOther, "Share URL"));
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUI() {
        progressDialog = new ProgressDialog(ReadMangaActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
        appColorBarStatus = getIntent().getStringExtra("appBarColorStatus");
        chapterURL = getIntent().getStringExtra("chapterURL");
        chapterThumb = getIntent().getStringExtra("chapterThumb");
        readFrom = getIntent().getStringExtra("readFrom");

        if (chapterURL != null) {
            getReadMangaContentData(chapterURL);
        } else {
            Toast.makeText(this, "Chapter URL Null!", Toast.LENGTH_SHORT).show();
        }
        if (appColorBarStatus != null) {
            if (appColorBarStatus.equalsIgnoreCase(getResources().getString(R.string.manga_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
                readMangaBinding.designBottomSheet.setBackgroundColor(getResources().getColor(R.color.manga_color));
            } else if (appColorBarStatus.equalsIgnoreCase(getResources().getString(R.string.manhua_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manhua_color));
                readMangaBinding.designBottomSheet.setBackgroundColor(getResources().getColor(R.color.manhua_color));
            } else if (appColorBarStatus.equalsIgnoreCase(getResources().getString(R.string.manhwa_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manhwa_color));
                readMangaBinding.designBottomSheet.setBackgroundColor(getResources().getColor(R.color.manhwa_color));
            } else if (appColorBarStatus.equalsIgnoreCase(getResources().getString(R.string.oneshot_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
                readMangaBinding.designBottomSheet.setBackgroundColor(getResources().getColor(R.color.manga_color));
            } else if (appColorBarStatus.equalsIgnoreCase(getResources().getString(R.string.mangaoneshot_string))) {
                readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
                readMangaBinding.designBottomSheet.setBackgroundColor(getResources().getColor(R.color.manga_color));
            }
        } else {
            readMangaBinding.appBarReadManga.setBackgroundColor(getResources().getColor(R.color.manga_color));
            readMangaBinding.designBottomSheet.setBackgroundColor(getResources().getColor(R.color.manga_color));
        }
    }

    public void getReadMangaContentData(String chapterURL) {
        this.chapterURL = chapterURL;
        progressDialog.show();
        new MyTask(chapterURL).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String totalURL;

        public MyTask(String totalURL) {
            this.totalURL = totalURL;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readMangaPresenter.getMangaContent(this.totalURL);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

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
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent;
//        if (readFrom.equalsIgnoreCase("MangaDetail")) {
//            intent = new Intent(ReadMangaActivity.this, MangaDetailActivity.class);
//            intent.putExtra("detailURL", detailURL);
//            intent.putExtra("detailType", appColorBarStatus);
//            intent.putExtra("detailTitle", "");
//            intent.putExtra("detailRating", "");
//            intent.putExtra("detailStatus", false);
//            intent.putExtra("detailThumb", "");
//            intent.putExtra("chapterURL", chapterURL);
//            intent.putExtra("detailFrom", "MangaRead");
//        } else {
//            intent = new Intent(ReadMangaActivity.this, MangaReleaseListActivity.class);
//            intent.putExtra("cameFrom", readFrom);
//        }
//        startActivity(intent);
//        finish();
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
        getReadMangaContentData(allChapterDatasList.get(position).getChapterUrl());
    }

    @Override
    public void onGetMangaContentDataSuccess(ReadMangaModel mangaContents) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            readMangaModel = mangaContents;
            readMangaBinding.textViewChapterTitle.setText(mangaContents.getChapterTitle());
            new Handler(Looper.getMainLooper()).post(() -> readMangaBinding.nestedBase.smoothScrollTo(0, 0));
            readMangaBinding.recyclerImageContentManga.scrollToPosition(0);
            RecyclerReadMangaAdapter mangaRecyclerNewReleasesAdapter = new RecyclerReadMangaAdapter(ReadMangaActivity.this, mangaContents.getImageContent());
            readMangaBinding.recyclerImageContentManga.setAdapter(mangaRecyclerNewReleasesAdapter);
            chapterNextURL = mangaContents.getNextMangaURL();
            if (chapterNextURL == null || chapterNextURL.isEmpty()) {
                readMangaBinding.nextChapButton.setVisibility(View.GONE);
            } else {
                readMangaBinding.nextChapButton.setVisibility(View.VISIBLE);
            }

            chapterPrevURL = mangaContents.getPreviousMangaURL();
            if (chapterPrevURL == null || chapterPrevURL.isEmpty()) {
                readMangaBinding.prevChapButton.setVisibility(View.GONE);
            } else {
                readMangaBinding.prevChapButton.setVisibility(View.VISIBLE);
            }

            detailURL = mangaContents.getMangaDetailURL();
            //add manga data
            Date dateNow = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedDate = df.format(dateNow);
            MangaHistoryModel mangaBookmarkModel = new MangaHistoryModel();
            mangaBookmarkModel.setChapterAddedDate(formattedDate);
            mangaBookmarkModel.setChapterTitle(mangaContents.getChapterTitle());
            mangaBookmarkModel.setChapterURL(chapterURL);
            mangaBookmarkModel.setChapterThumb(chapterThumb);
            mangaBookmarkModel.setChapterType(appColorBarStatus);
            localAppDB.mangaHistoryDAO().insertHistoryData(mangaBookmarkModel);
            Log.e("historyList", new Gson().toJson(localAppDB.mangaHistoryDAO().getMangaHistoryData()));
        });

    }

    @Override
    public void onGetMangaContentDataFailed() {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            Toast.makeText(ReadMangaActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onGetMangaChaptersDataSuccess(List<ReadMangaModel.AllChapterDatas> allChapters) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            allChapterDatasList = allChapters;
            readMangaBinding.showAllChap.setOnClickListener(v -> {
                dialog = new Dialog(ReadMangaActivity.this);
                SelectChapterDialogBinding chapterDialogBinding = SelectChapterDialogBinding.inflate(getLayoutInflater(), null, false);
                dialog.setContentView(chapterDialogBinding.getRoot());
                dialog.setTitle("Select other chapter");
                chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true);
                chapterDialogBinding.recyclerAllChapters.setLayoutManager(new LinearLayoutManager(ReadMangaActivity.this));
                chapterDialogBinding.recyclerAllChapters.setAdapter(new RecyclerAllChapterAdapter(ReadMangaActivity.this, allChapterDatasList));
                dialog.show();
            });
        });
    }

    @Override
    public void onGetMangaChaptersDataFailed() {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            Toast.makeText(ReadMangaActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onItemClickMangaContent() {
        if (readMangaBinding.appBarReadManga.getVisibility() == View.VISIBLE) {
            readMangaBinding.appBarReadManga.setVisibility(View.INVISIBLE);
            readMangaBinding.designBottomSheet.setVisibility(View.INVISIBLE);
        } else {
            readMangaBinding.appBarReadManga.setVisibility(View.VISIBLE);
            readMangaBinding.designBottomSheet.setVisibility(View.VISIBLE);
        }
    }
}
