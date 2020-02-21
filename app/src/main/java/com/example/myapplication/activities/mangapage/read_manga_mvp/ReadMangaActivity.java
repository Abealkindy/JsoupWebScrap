package com.example.myapplication.activities.mangapage.read_manga_mvp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.mangapage.MangaReleaseListActivity;
import com.example.myapplication.activities.mangapage.manga_detail_mvp.MangaDetailActivity;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerAllChapterAdapter;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerReadMangaAdapter;
import com.example.myapplication.databinding.ActivityReadMangaBinding;
import com.example.myapplication.databinding.SelectChapterDialogBinding;
import com.example.myapplication.models.mangamodels.ReadMangaModel;

import java.util.ArrayList;
import java.util.List;

public class ReadMangaActivity extends AppCompatActivity implements RecyclerAllChapterAdapter.ClickListener, ReadMangaInterface {
    private ActivityReadMangaBinding readMangaBinding;
    private ReadMangaPresenter readMangaPresenter = new ReadMangaPresenter(this);
    private List<ReadMangaModel.AllChapterDatas> allChapterDatasList = new ArrayList<>();
    private Dialog dialog;
    ProgressDialog progressDialog;
    String appColorBarStatus;

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
        appColorBarStatus = getIntent().getStringExtra("appBarColorStatus");
        String chapterURL = getIntent().getStringExtra("chapterURL");

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
        progressDialog.show();
        readMangaPresenter.getMangaContent(chapterURL);
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
        getReadMangaContentData(allChapterDatasList.get(position).getChapterUrl());
    }

    @Override
    public void onGetMangaContentDataSuccess(ReadMangaModel mangaContents) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            readMangaBinding.textViewChapterTitle.setText(mangaContents.getChapterTitle());
            readMangaBinding.recyclerImageContentManga.setHasFixedSize(true);
            RecyclerReadMangaAdapter mangaRecyclerNewReleasesAdapter = new RecyclerReadMangaAdapter(ReadMangaActivity.this, mangaContents.getImageContent());
            readMangaBinding.recyclerImageContentManga.setAdapter(mangaRecyclerNewReleasesAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReadMangaActivity.this);
            readMangaBinding.recyclerImageContentManga.setLayoutManager(linearLayoutManager);

            String getNextChapterURL = mangaContents.getNextMangaURL();
            if (getNextChapterURL == null || getNextChapterURL.isEmpty()) {
                readMangaBinding.nextChapButton.setVisibility(View.GONE);
            } else {
                readMangaBinding.nextChapButton.setVisibility(View.VISIBLE);
                readMangaBinding.nextChapButton.setOnClickListener(v -> getReadMangaContentData(getNextChapterURL));
            }

            String getPrevChapterURL = mangaContents.getPreviousMangaURL();
            if (getPrevChapterURL == null || getPrevChapterURL.isEmpty()) {
                readMangaBinding.prevChapButton.setVisibility(View.GONE);
            } else {
                readMangaBinding.prevChapButton.setVisibility(View.VISIBLE);
                readMangaBinding.prevChapButton.setOnClickListener(v -> getReadMangaContentData(getPrevChapterURL));
            }

            readMangaBinding.mangaInfoButton.setOnClickListener(v -> {
                Intent intent = new Intent(ReadMangaActivity.this, MangaDetailActivity.class);
                intent.putExtra("detailURL", mangaContents.getMangaDetailURL());
                intent.putExtra("detailType", appColorBarStatus);
                intent.putExtra("detailTitle", "");
                intent.putExtra("detailRating", "");
                intent.putExtra("detailStatus", "");
                intent.putExtra("detailThumb", "");
                startActivity(intent);
                finish();
            });
        });

    }

    @Override
    public void onGetMangaContentDataFailed() {
        progressDialog.dismiss();
        Toast.makeText(ReadMangaActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMangaChaptersDataSuccess(List<ReadMangaModel.AllChapterDatas> allChapters) {
        progressDialog.dismiss();
        allChapterDatasList = allChapters;
        readMangaBinding.showAllChap.setOnClickListener(v -> {
            dialog = new Dialog(ReadMangaActivity.this);
            SelectChapterDialogBinding chapterDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(ReadMangaActivity.this), R.layout.select_chapter_dialog, null, false);
            dialog.setContentView(chapterDialogBinding.getRoot());
            dialog.setTitle("Select other chapter");
            chapterDialogBinding.recyclerAllChapters.setHasFixedSize(true);
            chapterDialogBinding.recyclerAllChapters.setLayoutManager(new LinearLayoutManager(ReadMangaActivity.this));
            chapterDialogBinding.recyclerAllChapters.setAdapter(new RecyclerAllChapterAdapter(ReadMangaActivity.this, allChapterDatasList));
            dialog.show();
        });
    }

    @Override
    public void onGetMangaChaptersDataFailed() {
        progressDialog.dismiss();
        Toast.makeText(ReadMangaActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show();
    }
}
