package com.example.myapplication.activities.mangapage.manga_detail_mvp;

import android.os.Bundle;

import com.example.myapplication.adapters.RecyclerAllChapterDetailAdapter;
import com.example.myapplication.adapters.RecyclerGenreAdapter;
import com.example.myapplication.databinding.ActivityMangaDetailBinding;
import com.example.myapplication.models.mangamodels.DetailMangaModel;
import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MangaDetailActivity extends AppCompatActivity implements MangaDetailInterface {

    ActivityMangaDetailBinding detailBinding;
    String mangaType;
    private String detailType, detailTitle, detailThumb, detailRating;
    private boolean detailStatus;
    private MangaDetailPresenter mangaDetailPresenter = new MangaDetailPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_manga_detail);
        setSupportActionBar(detailBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initVariables();
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
        String detailURL = getIntent().getStringExtra("detailURL");
        detailType = getIntent().getStringExtra("detailType");
        detailThumb = getIntent().getStringExtra("detailThumb");
        detailTitle = getIntent().getStringExtra("detailTitle");
        detailRating = getIntent().getStringExtra("detailRating");
        detailStatus = getIntent().getBooleanExtra("detailStatus", false);

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
        if (detailURL != null) {
            mangaDetailPresenter.getDetailMangaData(detailURL);
        }
    }

    private void parseHtmlToViewableContent(String result) {
        Document document = Jsoup.parse(result);
        //get title
        if (detailTitle.equalsIgnoreCase("")) {
            Elements getTitle = document.select("h1[itemprop=headline]");
            detailTitle = getTitle.text().substring(0, getTitle.text().indexOf(" Bahasa Indonesia"));
            detailBinding.detailHeaderTitle.setText(detailTitle);
            initCollapsingToolbar(detailTitle);
        }

        if (detailThumb.equalsIgnoreCase("")) {
            Elements getThumb = document.select("img[src^=https://komikcast.com/wp-content/uploads/]");
            Picasso.get().load(getThumb.eachAttr("src").get(1)).into(detailBinding.headerThumbnailDetail);
        }
        //get Synopsis
        Elements getSynopsis = document.getElementsByTag("p");
        detailBinding.contentManga.textSynopsis.setText(getSynopsis.text());

        //get All chapter data
        List<DetailMangaModel.DetailAllChapterDatas> detailAllChapterDatasList = new ArrayList<>();
        Elements getAllMangaChapters = document.getElementsByTag("li");
        for (Element element : getAllMangaChapters) {
            DetailMangaModel.DetailAllChapterDatas allChapterDatas = new DetailMangaModel().new DetailAllChapterDatas();
            String chapterReleaseTime = element.getElementsByClass("rightoff").text();
            String chapterTitle = element.select("a[href^=https://komikcast.com/chapter/]").text();
            String chapterURL = element.select("a[href^=https://komikcast.com/chapter/]").attr("href");
            allChapterDatas.setChapterReleaseTime(chapterReleaseTime);
            allChapterDatas.setChapterTitle(chapterTitle);
            allChapterDatas.setChapterURL(chapterURL);
            detailAllChapterDatasList.add(allChapterDatas);
        }
        List<DetailMangaModel.DetailAllChapterDatas> afterCut = new ArrayList<>(detailAllChapterDatasList.subList(8, detailAllChapterDatasList.size() - 6));
        detailBinding.contentManga.recyclerAllChaptersDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MangaDetailActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        detailBinding.contentManga.recyclerAllChaptersDetail.setLayoutManager(linearLayoutManager);
        detailBinding.contentManga.recyclerAllChaptersDetail.setAdapter(new RecyclerAllChapterDetailAdapter(MangaDetailActivity.this, afterCut, mangaType));

        //get genre data
        List<DetailMangaModel.DetailMangaGenres> genresList = new ArrayList<>();
        Elements getGenres = document.select("a[rel=tag]");
        for (Element element : getGenres) {
            String genreTitle = element.text();
            String genreURL = element.attr("href");
            DetailMangaModel.DetailMangaGenres mangaGenres = new DetailMangaModel().new DetailMangaGenres();
            mangaGenres.setGenreTitle(genreTitle);
            mangaGenres.setGenreURL(genreURL);
            genresList.add(mangaGenres);
        }
        List<DetailMangaModel.DetailMangaGenres> genreCut = new ArrayList<>(genresList.subList(0, genresList.size() - 1));
        detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerGenre = new LinearLayoutManager(MangaDetailActivity.this);
        linearLayoutManagerGenre.setOrientation(RecyclerView.HORIZONTAL);
        detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setLayoutManager(linearLayoutManagerGenre);
        detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setAdapter(new RecyclerGenreAdapter(MangaDetailActivity.this, genreCut));

        //get Updated on
        Elements getLatestUpdate = document.select("time[itemprop=dateModified]");
        detailBinding.contentManga.mangaAboutLayout.latestUpdateDate.setText(getLatestUpdate.text());

        //get Other name
        Elements getOtherName = document.getElementsByClass("alter");
        detailBinding.contentManga.mangaAboutLayout.textOtherName.setText(getOtherName.text());

        //get Author and others
        Elements getAuthor = document.getElementsByTag("span");
        if (getAuthor.eachText().get(15).length() < 8) {
            detailBinding.contentManga.mangaAboutLayout.textAuthor.setText("-");
        } else {
            detailBinding.contentManga.mangaAboutLayout.textAuthor.setText(getAuthor.eachText().get(15).substring(8));
        }
        detailBinding.contentManga.mangaAboutLayout.textReleasedOn.setText(getAuthor.eachText().get(14).substring(10));
        detailBinding.contentManga.mangaAboutLayout.textTotalChapters.setText(getAuthor.eachText().get(17).substring(15));
        if (String.valueOf(detailStatus).equalsIgnoreCase("")) {
            String getStatus = getAuthor.eachText().get(13).substring(8);
            if (getStatus.equalsIgnoreCase(getResources().getString(R.string.completed_text))) {
                detailBinding.detailStatus.setBackground(getResources().getDrawable(R.drawable.bubble_background_completed));
                detailBinding.detailStatus.setText(getResources().getString(R.string.completed_text));
            } else {
                detailBinding.detailStatus.setBackground(getResources().getDrawable(R.drawable.bubble_background_ongoing));
                detailBinding.detailStatus.setText(getResources().getString(R.string.ongoing_text));
            }
        }
        if (detailType.equalsIgnoreCase("")) {
            detailType = getAuthor.eachText().get(16).substring(6);
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
        if (detailRating.equalsIgnoreCase("")) {
            Elements getRating = document.getElementsByClass("rating");
            detailRating = getRating.eachText().get(0).substring(7, getRating.eachText().get(0).length() - 10);
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

    }

    @Override
    public void onGetDetailDataSuccess(String detailHTMLResult) {
        parseHtmlToViewableContent(detailHTMLResult);
    }

    @Override
    public void onGetDetailDataFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
    }
}
