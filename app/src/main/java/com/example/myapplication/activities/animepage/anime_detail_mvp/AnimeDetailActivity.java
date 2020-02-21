package com.example.myapplication.activities.animepage.anime_detail_mvp;

import android.os.Bundle;

import com.example.myapplication.adapters.animeadapters.recycleradapters.RecyclerAllEpisodeDetailAdapter;
import com.example.myapplication.adapters.RecyclerGenreAdapter;
import com.example.myapplication.databinding.ActivityAnimeDetailBinding;
import com.example.myapplication.models.animemodels.AnimeDetailModel;
import com.example.myapplication.models.mangamodels.DetailMangaModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AnimeDetailActivity extends AppCompatActivity implements AnimeDetailInterface {

    ActivityAnimeDetailBinding animeDetailBinding;
    AnimeDetailModel animeDetailModel = new AnimeDetailModel();
    private AnimeDetailPresenter detailPresenter = new AnimeDetailPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime_detail);
        setSupportActionBar(animeDetailBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initVariables();
    }

    private void initVariables() {
        String getAnimeDetailURL = getIntent().getStringExtra("animeDetailURL");
        String getAnimeDetailTitle = getIntent().getStringExtra("animeDetailTitle");
        String getAnimeDetailThumb = getIntent().getStringExtra("animeDetailThumb");
        String getAnimeDetailStatus = getIntent().getStringExtra("animeDetailStatus");
        String getAnimeDetailType = getIntent().getStringExtra("animeDetailType");
        animeDetailModel.setEpisodeTitle(getAnimeDetailTitle);
        animeDetailModel.setEpisodeThumb(getAnimeDetailThumb);
        animeDetailModel.setEpisodeStatus(getAnimeDetailStatus);
        animeDetailModel.setEpisodeType(getAnimeDetailType);

        if (getAnimeDetailTitle != null) {
            if (getAnimeDetailTitle.contains("Episode")) {
                getAnimeDetailTitle = getAnimeDetailTitle.substring(0, getAnimeDetailTitle.length() - 11);
            } else {
                Log.e("CUT?", "NO");
            }
        }

        if (getAnimeDetailType != null) {
            if (getAnimeDetailType.equalsIgnoreCase(getResources().getString(R.string.series_string)) ||
                    getAnimeDetailType.contains(getResources().getString(R.string.series_string))) {
                animeDetailBinding.animeTypeDetail.setText(getResources().getString(R.string.series_string));
                animeDetailBinding.animeTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_series));
            } else if (getAnimeDetailType.equalsIgnoreCase(getResources().getString(R.string.ova_string)) ||
                    getAnimeDetailType.contains(getResources().getString(R.string.ova_string))) {
                animeDetailBinding.animeTypeDetail.setText(getResources().getString(R.string.ova_string));
                animeDetailBinding.animeTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_ova));
            } else if (getAnimeDetailType.equalsIgnoreCase(getResources().getString(R.string.ona_string)) ||
                    getAnimeDetailType.contains(getResources().getString(R.string.ona_string))) {
                animeDetailBinding.animeTypeDetail.setText(getResources().getString(R.string.ona_string));
                animeDetailBinding.animeTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_ona));
            } else if (getAnimeDetailType.equalsIgnoreCase(getResources().getString(R.string.movie_string)) ||
                    getAnimeDetailType.contains(getResources().getString(R.string.movie_string_lower))) {
                animeDetailBinding.animeTypeDetail.setText(getResources().getString(R.string.movie_string));
                animeDetailBinding.animeTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_movie));
            } else if (getAnimeDetailType.equalsIgnoreCase(getResources().getString(R.string.special_string)) ||
                    getAnimeDetailType.contains(getResources().getString(R.string.special_string))) {
                animeDetailBinding.animeTypeDetail.setText(getResources().getString(R.string.special_string));
                animeDetailBinding.animeTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_special));
            } else if (getAnimeDetailType.equalsIgnoreCase(getResources().getString(R.string.la_string)) ||
                    getAnimeDetailType.contains(getResources().getString(R.string.la_string))) {
                animeDetailBinding.animeTypeDetail.setText(getResources().getString(R.string.la_string));
                animeDetailBinding.animeTypeDetail.setBackground(getResources().getDrawable(R.drawable.bubble_background_la));
            }
        }

        if (getAnimeDetailStatus != null) {
            if (getAnimeDetailStatus.equalsIgnoreCase(getResources().getString(R.string.ongoing_text))) {
                animeDetailBinding.detailStatusAnime.setText(getResources().getString(R.string.ongoing_text));
                animeDetailBinding.detailStatusAnime.setBackground(getResources().getDrawable(R.drawable.bubble_background_ongoing));
            } else if (getAnimeDetailStatus.equalsIgnoreCase(getResources().getString(R.string.completed_text))) {
                animeDetailBinding.detailStatusAnime.setText(getResources().getString(R.string.completed_text));
                animeDetailBinding.detailStatusAnime.setBackground(getResources().getDrawable(R.drawable.bubble_background_completed));
            }
        }
        animeDetailBinding.detailHeaderTitleAnime.setText(getAnimeDetailTitle);
        initCollapsingToolbar(getAnimeDetailTitle);
        Picasso.get().load(getAnimeDetailThumb).into(animeDetailBinding.headerThumbnailDetailAnime);
        if (getAnimeDetailURL != null) {
            detailPresenter.getAnimeDetailContent(getAnimeDetailURL);
        }
    }


    private void initCollapsingToolbar(String titleManga) {
        animeDetailBinding.toolbarLayoutAnime.setTitle("");
        animeDetailBinding.appBarAnime.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = animeDetailBinding.appBarAnime.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    animeDetailBinding.toolbarLayoutAnime.setTitle(titleManga);
                    isShow = true;
                } else if (isShow) {
                    animeDetailBinding.toolbarLayoutAnime.setTitle(" ");
                    isShow = false;
                }
            }
        });

    }

    @Override
    public void onGetDetailDataSuccess(DetailMangaModel detailData) {
        runOnUiThread(() -> {
            //get synopsis
            if (detailData.getMangaSynopsis() == null || detailData.getMangaSynopsis().isEmpty()) {
                animeDetailBinding.contentAnime.textSynopsisAnime.setText("-");
            } else {
                animeDetailBinding.contentAnime.textSynopsisAnime.setText(detailData.getMangaSynopsis());
            }

            if (detailData.getFirstUpdateYear() == null || detailData.getFirstUpdateYear().isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textReleasedOnAnime.setText("-");
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textReleasedOnAnime.setText(detailData.getFirstUpdateYear());
            }

            if (detailData.getLastMangaUpdateDate() == null || detailData.getLastMangaUpdateDate().isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textDuration.setText("-");
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textDuration.setText(detailData.getLastMangaUpdateDate());
            }

            if (detailData.getTotalMangaChapter() == null || detailData.getTotalMangaChapter().isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textTotalEpisode.setText("-");
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textTotalEpisode.setText(detailData.getTotalMangaChapter());
            }

            if (detailData.getMangaAuthor() == null || detailData.getMangaAuthor().isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textStudio.setText("-");
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textStudio.setText(detailData.getMangaAuthor());
            }

            if (detailData.getOtherNames() == null || detailData.getOtherNames().isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textOtherNameAnime.setText("-");
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textOtherNameAnime.setText(detailData.getOtherNames());
            }

            String animeDetailRating = detailData.getMangaRating();
            animeDetailBinding.ratingBarDetailAnime.setNumStars(5);
            if (animeDetailRating.equalsIgnoreCase("N/A") || animeDetailRating.equalsIgnoreCase("?") || animeDetailRating.equalsIgnoreCase("-")) {
                animeDetailBinding.ratingBarDetailAnime.setRating(0);
                animeDetailBinding.ratingNumberDetailAnime.setText(animeDetailRating);
            } else if (Float.parseFloat(animeDetailRating) <= 0) {
                animeDetailBinding.ratingBarDetailAnime.setRating(0);
                animeDetailBinding.ratingNumberDetailAnime.setText(animeDetailRating);
            } else {
                animeDetailBinding.ratingBarDetailAnime.setRating(Float.parseFloat(animeDetailRating) / 2 / 10);
                animeDetailBinding.ratingNumberDetailAnime.setText(animeDetailRating);
            }
        });
    }

    @Override
    public void onGetDetailDataFailed() {
        runOnUiThread(() -> Toast.makeText(AnimeDetailActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onGetAllEpisodeSuccess(List<DetailMangaModel.DetailAllChapterDatas> detailAllEpisodeDataList) {
        runOnUiThread(() -> {
            animeDetailBinding.contentAnime.recyclerAllEpisodesDetail.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManagerAllEpisode = new LinearLayoutManager(AnimeDetailActivity.this);
            linearLayoutManagerAllEpisode.setOrientation(RecyclerView.VERTICAL);
            animeDetailBinding.contentAnime.recyclerAllEpisodesDetail.setLayoutManager(linearLayoutManagerAllEpisode);
            animeDetailBinding.contentAnime.recyclerAllEpisodesDetail.setAdapter(new RecyclerAllEpisodeDetailAdapter(AnimeDetailActivity.this, detailAllEpisodeDataList, animeDetailModel));
        });
    }

    @Override
    public void onGetAllEpisodeFailed() {
        runOnUiThread(() -> Toast.makeText(AnimeDetailActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onGetGenreSuccess(List<DetailMangaModel.DetailMangaGenres> genresList) {
        runOnUiThread(() -> {
            animeDetailBinding.contentAnime.animeAboutLayout.recyclerGenreAnime.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManagerGenre = new LinearLayoutManager(AnimeDetailActivity.this);
            linearLayoutManagerGenre.setOrientation(RecyclerView.HORIZONTAL);
            animeDetailBinding.contentAnime.animeAboutLayout.recyclerGenreAnime.setLayoutManager(linearLayoutManagerGenre);
            animeDetailBinding.contentAnime.animeAboutLayout.recyclerGenreAnime.setAdapter(new RecyclerGenreAdapter(AnimeDetailActivity.this, genresList));
        });
    }

    @Override
    public void onGetGenreFailed() {
        runOnUiThread(() -> Toast.makeText(AnimeDetailActivity.this, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show());
    }
}
