package com.example.myapplication.activities.mangapage.manga_detail_mvp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.activities.mangapage.MangaReleaseListActivity;
import com.example.myapplication.activities.mangapage.read_manga_mvp.ReadMangaActivity;
import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerAllChapterDetailAdapter;
import com.example.myapplication.adapters.RecyclerGenreAdapter;
import com.example.myapplication.databinding.ActivityMangaDetailBinding;
import com.example.myapplication.localstorages.manga_local.MangaBookmarkModel;
import com.example.myapplication.models.mangamodels.DetailMangaModel;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.myapplication.MyApp.localAppDB;

public class MangaDetailActivity extends AppCompatActivity implements MangaDetailInterface {

    ActivityMangaDetailBinding detailBinding;
    private String mangaType = "", detailType = "", detailTitle = "", detailThumb = "", detailRating = "", mangaDetailURL = "", detailFrom = "", chapterURL = "";
    private boolean detailStatus = false;
    private MangaDetailPresenter mangaDetailPresenter = new MangaDetailPresenter(this);
    List<DetailMangaModel.DetailAllChapterDatas> detailAllChapterDatasList = new ArrayList<>();
    MangaBookmarkModel mangaBookmarkModel = new MangaBookmarkModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = ActivityMangaDetailBinding.inflate(getLayoutInflater());
        setContentView(detailBinding.getRoot());
        setSupportActionBar(detailBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initVariables();
        initEvent();
    }

    private void initEvent() {
        detailBinding.linearFavourite.setOnClickListener(v -> {
            Log.e("FAVCLICKED? ", "YES");
            if (detailBinding.favouriteImageInactive.getVisibility() == View.VISIBLE) {
                Date dateNow = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
                String formattedDate = df.format(dateNow);
                mangaBookmarkModel.setMangaAddedDate(formattedDate);
                mangaBookmarkModel.setMangaTitle(detailTitle);
                mangaBookmarkModel.setMangaThumb(detailThumb);
                mangaBookmarkModel.setMangaDetailURL(mangaDetailURL);
                mangaBookmarkModel.setMangaStatus(detailStatus);
                mangaBookmarkModel.setMangaType(detailType);
                mangaBookmarkModel.setMangaRating(detailRating);
                localAppDB.mangaBookmarkDAO().insertBookmarkData(mangaBookmarkModel);
                detailBinding.favouriteImageInactive.setVisibility(View.GONE);
                detailBinding.favouriteImageActive.setVisibility(View.VISIBLE);
            } else if (detailBinding.favouriteImageActive.getVisibility() == View.VISIBLE) {
                localAppDB.mangaBookmarkDAO().deleteBookmarkItem(mangaDetailURL);
                detailBinding.favouriteImageInactive.setVisibility(View.VISIBLE);
                detailBinding.favouriteImageActive.setVisibility(View.GONE);
            }

        });
    }

    private void initCollapsingToolbar(String titleManga) {
        detailBinding.toolbarLayout.setTitle("");
        detailBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = detailBinding.appBar.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    detailBinding.toolbarLayout.setTitle(titleManga);
                    isShow = true;
                } else if (isShow) {
                    detailBinding.toolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

    }

    private void initVariables() {
        mangaDetailURL = getIntent().getStringExtra("detailURL");
        detailType = getIntent().getStringExtra("detailType");
        detailThumb = getIntent().getStringExtra("detailThumb");
        detailTitle = getIntent().getStringExtra("detailTitle");
        detailRating = getIntent().getStringExtra("detailRating");
        detailFrom = getIntent().getStringExtra("detailFrom");
        chapterURL = getIntent().getStringExtra("chapterURL");
        detailStatus = getIntent().getBooleanExtra("detailStatus", false);

        MangaBookmarkModel mangaBookmarkModel = localAppDB.mangaBookmarkDAO().findByName(mangaDetailURL);
        if (mangaBookmarkModel != null && mangaBookmarkModel.getMangaDetailURL() != null && mangaBookmarkModel.getMangaDetailURL().equals(mangaDetailURL)) {
            detailBinding.favouriteImageInactive.setVisibility(View.GONE);
            detailBinding.favouriteImageActive.setVisibility(View.VISIBLE);
        } else {
            detailBinding.favouriteImageInactive.setVisibility(View.VISIBLE);
            detailBinding.favouriteImageActive.setVisibility(View.GONE);
        }

        if (!detailThumb.equalsIgnoreCase("")) {
            Picasso.get().load(detailThumb).into(detailBinding.headerThumbnailDetail);
        }
        if (!detailTitle.equalsIgnoreCase("")) {
            detailBinding.detailHeaderTitle.setText(detailTitle);
            initCollapsingToolbar(detailTitle);
        }
        if (!detailType.equalsIgnoreCase("")) {
            mangaType = detailType;
            if (detailType.equalsIgnoreCase(getResources().getString(R.string.manga_string))) {
                detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.manga_string));
                detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manga));
            } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.manhua_string))) {
                detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.manhua_string));
                detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manhua));
            } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.manhwa_string))) {
                detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.manhwa_string));
                detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manhwa));
            } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.mangaoneshot_string))) {
                detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.mangaoneshot_string));
                detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manga));
            } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.oneshot_string))) {
                detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.oneshot_string));
                detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manga));
            }
        }
        if (!String.valueOf(detailStatus).equalsIgnoreCase("")) {
            if (detailStatus) {
                detailBinding.detailStatus.setBackground(getResources().getDrawable(R.drawable.bubble_background_completed));
                detailBinding.detailStatus.setText(getResources().getString(R.string.completed_text));
            } else {
                detailBinding.detailStatus.setBackground(getResources().getDrawable(R.drawable.bubble_background_ongoing));
                detailBinding.detailStatus.setText(getResources().getString(R.string.ongoing_text));
            }
        }
        if (!detailRating.equalsIgnoreCase("")) {
            detailBinding.ratingBarDetail.setNumStars(5);
            String replaceComma = detailRating.replace(",", ".");
            if (detailRating.equalsIgnoreCase("N/A") || detailRating.equalsIgnoreCase("?") || detailRating.equalsIgnoreCase("-")) {
                detailBinding.ratingBarDetail.setRating(0);
                detailBinding.ratingNumberDetail.setText(detailRating);
            } else if (Float.parseFloat(replaceComma) <= 0) {
                detailBinding.ratingBarDetail.setRating(0);
                detailBinding.ratingNumberDetail.setText(detailRating);
            } else {
                detailBinding.ratingBarDetail.setRating(Float.parseFloat(replaceComma) / 2);
                detailBinding.ratingNumberDetail.setText(replaceComma);
            }

        }
        if (mangaDetailURL != null) {
            mangaDetailPresenter.getDetailMangaData(mangaDetailURL);
        }
    }

    @Override
    public void onGetDetailDataSuccess(DetailMangaModel detailMangaModel) {
        runOnUiThread(() -> {
            //get title
            if (detailTitle.equalsIgnoreCase("")) {
                detailTitle = detailMangaModel.getMangaTitle();
                detailBinding.detailHeaderTitle.setText(detailMangaModel.getMangaTitle());
                initCollapsingToolbar(detailTitle);
            }

            //get thumb
            if (detailThumb.equalsIgnoreCase("")) {
                detailThumb = detailMangaModel.getMangaThumb();
                Picasso.get().load(detailMangaModel.getMangaThumb()).into(detailBinding.headerThumbnailDetail);
            }

            //get Synopsis
            detailBinding.contentManga.textSynopsis.setText(detailMangaModel.getMangaSynopsis());

            //get Updated on
            detailBinding.contentManga.mangaAboutLayout.latestUpdateDate.setText(detailMangaModel.getLastMangaUpdateDate());

            //get Other name
            detailBinding.contentManga.mangaAboutLayout.textOtherName.setText(detailMangaModel.getOtherNames());

            //get Author
            if (detailMangaModel.getMangaAuthor() == null || detailMangaModel.getMangaAuthor().isEmpty()) {
                detailBinding.contentManga.mangaAboutLayout.textAuthor.setText("-");
            } else {
                detailBinding.contentManga.mangaAboutLayout.textAuthor.setText(detailMangaModel.getMangaAuthor());
            }

            //get released on
            if (detailMangaModel.getFirstUpdateYear() == null || detailMangaModel.getFirstUpdateYear().isEmpty()) {
                detailBinding.contentManga.mangaAboutLayout.textReleasedOn.setText("-");
            } else {
                detailBinding.contentManga.mangaAboutLayout.textReleasedOn.setText(detailMangaModel.getFirstUpdateYear());
            }

            //get total chapter
            if (detailMangaModel.getTotalMangaChapter() == null || detailMangaModel.getTotalMangaChapter().isEmpty()) {
                detailBinding.contentManga.mangaAboutLayout.textTotalChapters.setText("-");
            } else {
                detailBinding.contentManga.mangaAboutLayout.textTotalChapters.setText(detailMangaModel.getTotalMangaChapter());
            }

            //get manga status
            if (String.valueOf(detailStatus).equalsIgnoreCase("")) {
                if (detailMangaModel.getMangaStatus().equalsIgnoreCase(getResources().getString(R.string.completed_text))) {
                    detailBinding.detailStatus.setBackground(getResources().getDrawable(R.drawable.bubble_background_completed));
                    detailBinding.detailStatus.setText(getResources().getString(R.string.completed_text));
                } else {
                    detailBinding.detailStatus.setBackground(getResources().getDrawable(R.drawable.bubble_background_ongoing));
                    detailBinding.detailStatus.setText(getResources().getString(R.string.ongoing_text));
                }
            }

            //get manga type
            if (detailType.equalsIgnoreCase("")) {
                detailType = detailMangaModel.getMangaType();
                mangaType = detailType;
                if (detailType.equalsIgnoreCase(getResources().getString(R.string.manga_string))) {
                    detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.manga_string));
                    detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manga));
                } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.manhua_string))) {
                    detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.manhua_string));
                    detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manhua));
                } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.manhwa_string))) {
                    detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.manhwa_string));
                    detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manhwa));
                } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.mangaoneshot_string))) {
                    detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.mangaoneshot_string));
                    detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manga));
                } else if (detailType.equalsIgnoreCase(getResources().getString(R.string.oneshot_string))) {
                    detailBinding.mangaTypeDetail.setText(getResources().getString(R.string.oneshot_string));
                    detailBinding.mangaTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_manga));
                }
            }

            //get manga rating
            if (detailRating.equalsIgnoreCase("")) {
                detailRating = detailMangaModel.getMangaRating();
                detailBinding.ratingBarDetail.setNumStars(5);
                if (detailRating.equalsIgnoreCase("N/A") || detailRating.equalsIgnoreCase("?") || detailRating.equalsIgnoreCase("-")) {
                    detailBinding.ratingBarDetail.setRating(0);
                    detailBinding.ratingNumberDetail.setText(detailRating);
                } else if (Float.parseFloat(detailRating) <= 0) {
                    detailBinding.ratingBarDetail.setRating(0);
                    detailBinding.ratingNumberDetail.setText(detailRating);
                } else {
                    detailBinding.ratingBarDetail.setRating(Float.parseFloat(detailRating) / 2);
                    detailBinding.ratingNumberDetail.setText(detailRating);
                }
            }
        });
    }

    @Override
    public void onGetDetailDataFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetAllChapterSuccess(List<DetailMangaModel.DetailAllChapterDatas> detailAllChapterDatasList) {
        runOnUiThread(() -> {
            this.detailAllChapterDatasList = detailAllChapterDatasList;
            detailBinding.contentManga.recyclerAllChaptersDetail.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MangaDetailActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            detailBinding.contentManga.recyclerAllChaptersDetail.setLayoutManager(linearLayoutManager);
            detailBinding.contentManga.recyclerAllChaptersDetail.setAdapter(new RecyclerAllChapterDetailAdapter(MangaDetailActivity.this, detailAllChapterDatasList, mangaType));
        });
    }

    @Override
    public void onGetAllChapterFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetGenreSuccess(List<DetailMangaModel.DetailMangaGenres> genresList) {
        runOnUiThread(() -> {
            detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManagerGenre = new LinearLayoutManager(MangaDetailActivity.this);
            linearLayoutManagerGenre.setOrientation(RecyclerView.HORIZONTAL);
            detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setLayoutManager(linearLayoutManagerGenre);
            detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setAdapter(new RecyclerGenreAdapter(MangaDetailActivity.this, genresList));
        });

    }

    @Override
    public void onGetGenreFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (detailFrom != null && !detailFrom.isEmpty()) {
            Intent intent;
            if (detailFrom.equalsIgnoreCase("MangaRead")) {
                intent = new Intent(MangaDetailActivity.this, ReadMangaActivity.class);
                intent.putExtra("appBarColorStatus", mangaType);
                intent.putExtra("chapterURL", chapterURL);
                intent.putExtra("readFrom", "MangaDetail");
            } else {
                intent = new Intent(MangaDetailActivity.this, MangaReleaseListActivity.class);
                intent.putExtra("cameFrom", detailFrom);
            }
            startActivity(intent);
            finish();
        }
    }
}
