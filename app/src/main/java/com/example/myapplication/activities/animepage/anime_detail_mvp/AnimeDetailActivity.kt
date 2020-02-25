package com.example.myapplication.activities.animepage.anime_detail_mvp

import android.os.Bundle

import com.example.myapplication.adapters.animeadapters.recycleradapters.RecyclerAllEpisodeDetailAdapter
import com.example.myapplication.adapters.RecyclerGenreAdapter
import com.example.myapplication.databinding.ActivityAnimeDetailBinding
import com.example.myapplication.models.animemodels.AnimeDetailModel
import com.example.myapplication.models.mangamodels.DetailMangaModel

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.util.Log
import android.widget.Toast

import com.example.myapplication.R
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso


class AnimeDetailActivity : AppCompatActivity(), AnimeDetailInterface {

    internal lateinit var animeDetailBinding: ActivityAnimeDetailBinding
    private var animeDetailModel = AnimeDetailModel()
    private val detailPresenter = AnimeDetailPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animeDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_anime_detail)
        setSupportActionBar(animeDetailBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initVariables()
    }

    private fun initVariables() {
        val getAnimeDetailURL = intent.getStringExtra("animeDetailURL")
        var getAnimeDetailTitle = intent.getStringExtra("animeDetailTitle")
        val getAnimeDetailThumb = intent.getStringExtra("animeDetailThumb")
        val getAnimeDetailStatus = intent.getStringExtra("animeDetailStatus")
        val getAnimeDetailType = intent.getStringExtra("animeDetailType")
        animeDetailModel.episodeTitle = getAnimeDetailTitle
        animeDetailModel.episodeThumb = getAnimeDetailThumb
        animeDetailModel.episodeStatus = getAnimeDetailStatus
        animeDetailModel.episodeType = getAnimeDetailType

        if (getAnimeDetailTitle != null) {
            if (getAnimeDetailTitle.contains("Episode")) {
                getAnimeDetailTitle = getAnimeDetailTitle.substring(0, getAnimeDetailTitle.length - 11)
            } else {
                Log.e("CUT?", "NO")
            }
        }

        if (getAnimeDetailType != null) {
            if (getAnimeDetailType.equals(resources.getString(R.string.series_string), ignoreCase = true) || getAnimeDetailType.contains(resources.getString(R.string.series_string))) {
                animeDetailBinding.animeTypeDetail.text = resources.getString(R.string.series_string)
                animeDetailBinding.animeTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_series)
            } else if (getAnimeDetailType.equals(resources.getString(R.string.ova_string), ignoreCase = true) || getAnimeDetailType.contains(resources.getString(R.string.ova_string))) {
                animeDetailBinding.animeTypeDetail.text = resources.getString(R.string.ova_string)
                animeDetailBinding.animeTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_ova)
            } else if (getAnimeDetailType.equals(resources.getString(R.string.ona_string), ignoreCase = true) || getAnimeDetailType.contains(resources.getString(R.string.ona_string))) {
                animeDetailBinding.animeTypeDetail.text = resources.getString(R.string.ona_string)
                animeDetailBinding.animeTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_ona)
            } else if (getAnimeDetailType.equals(resources.getString(R.string.movie_string), ignoreCase = true) || getAnimeDetailType.contains(resources.getString(R.string.movie_string_lower))) {
                animeDetailBinding.animeTypeDetail.text = resources.getString(R.string.movie_string)
                animeDetailBinding.animeTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_movie)
            } else if (getAnimeDetailType.equals(resources.getString(R.string.special_string), ignoreCase = true) || getAnimeDetailType.contains(resources.getString(R.string.special_string))) {
                animeDetailBinding.animeTypeDetail.text = resources.getString(R.string.special_string)
                animeDetailBinding.animeTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_special)
            } else if (getAnimeDetailType.equals(resources.getString(R.string.la_string), ignoreCase = true) || getAnimeDetailType.contains(resources.getString(R.string.la_string))) {
                animeDetailBinding.animeTypeDetail.text = resources.getString(R.string.la_string)
                animeDetailBinding.animeTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_la)
            }
        }

        if (getAnimeDetailStatus != null) {
            if (getAnimeDetailStatus.equals(resources.getString(R.string.ongoing_text), ignoreCase = true)) {
                animeDetailBinding.detailStatusAnime.text = resources.getString(R.string.ongoing_text)
                animeDetailBinding.detailStatusAnime.background = resources.getDrawable(R.drawable.bubble_background_ongoing)
            } else if (getAnimeDetailStatus.equals(resources.getString(R.string.completed_text), ignoreCase = true)) {
                animeDetailBinding.detailStatusAnime.text = resources.getString(R.string.completed_text)
                animeDetailBinding.detailStatusAnime.background = resources.getDrawable(R.drawable.bubble_background_completed)
            }
        }
        animeDetailBinding.detailHeaderTitleAnime.text = getAnimeDetailTitle
        initCollapsingToolbar(getAnimeDetailTitle)
        Picasso.get().load(getAnimeDetailThumb).into(animeDetailBinding.headerThumbnailDetailAnime)
        if (getAnimeDetailURL != null) {
            detailPresenter.getAnimeDetailContent(getAnimeDetailURL)
        }
    }


    private fun initCollapsingToolbar(titleManga: String?) {
        animeDetailBinding.toolbarLayoutAnime.title = ""
        animeDetailBinding.appBarAnime.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = animeDetailBinding.appBarAnime.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    animeDetailBinding.toolbarLayoutAnime.title = titleManga
                    isShow = true
                } else if (isShow) {
                    animeDetailBinding.toolbarLayoutAnime.title = " "
                    isShow = false
                }
            }
        })

    }

    override fun onGetDetailDataSuccess(detailMangaModel: DetailMangaModel) {
        runOnUiThread {
            //get synopsis
            if (detailMangaModel.mangaSynopsis == null || detailMangaModel.mangaSynopsis!!.isEmpty()) {
                animeDetailBinding.contentAnime.textSynopsisAnime.text = "-"
            } else {
                animeDetailBinding.contentAnime.textSynopsisAnime.text = detailMangaModel.mangaSynopsis
            }

            if (detailMangaModel.firstUpdateYear == null || detailMangaModel.firstUpdateYear!!.isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textReleasedOnAnime.text = "-"
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textReleasedOnAnime.text = detailMangaModel.firstUpdateYear
            }

            if (detailMangaModel.lastMangaUpdateDate == null || detailMangaModel.lastMangaUpdateDate!!.isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textDuration.text = "-"
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textDuration.text = detailMangaModel.lastMangaUpdateDate
            }

            if (detailMangaModel.totalMangaChapter == null || detailMangaModel.totalMangaChapter!!.isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textTotalEpisode.text = "-"
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textTotalEpisode.text = detailMangaModel.totalMangaChapter
            }

            if (detailMangaModel.mangaAuthor == null || detailMangaModel.mangaAuthor!!.isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textStudio.text = "-"
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textStudio.text = detailMangaModel.mangaAuthor
            }

            if (detailMangaModel.otherNames == null || detailMangaModel.otherNames!!.isEmpty()) {
                animeDetailBinding.contentAnime.animeAboutLayout.textOtherNameAnime.text = "-"
            } else {
                animeDetailBinding.contentAnime.animeAboutLayout.textOtherNameAnime.text = detailMangaModel.otherNames
            }

            val animeDetailRating = detailMangaModel.mangaRating
            animeDetailBinding.ratingBarDetailAnime.numStars = 5
            if (animeDetailRating.equals("N/A", ignoreCase = true) || animeDetailRating.equals("?", ignoreCase = true) || animeDetailRating.equals("-", ignoreCase = true)) {
                animeDetailBinding.ratingBarDetailAnime.rating = 0f
                animeDetailBinding.ratingNumberDetailAnime.text = animeDetailRating
            } else if (java.lang.Float.parseFloat(animeDetailRating!!) <= 0) {
                animeDetailBinding.ratingBarDetailAnime.rating = 0f
                animeDetailBinding.ratingNumberDetailAnime.text = animeDetailRating
            } else {
                animeDetailBinding.ratingBarDetailAnime.rating = java.lang.Float.parseFloat(animeDetailRating) / 2f / 10f
                animeDetailBinding.ratingNumberDetailAnime.text = animeDetailRating
            }
        }
    }

    override fun onGetDetailDataFailed() {
        runOnUiThread { Toast.makeText(this@AnimeDetailActivity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show() }
    }

    override fun onGetAllEpisodeSuccess(detailAllEpisodeDataList: List<DetailMangaModel.DetailAllChapterDatas>) {
        runOnUiThread {
            animeDetailBinding.contentAnime.recyclerAllEpisodesDetail.setHasFixedSize(true)
            val linearLayoutManagerAllEpisode = LinearLayoutManager(this@AnimeDetailActivity)
            linearLayoutManagerAllEpisode.orientation = RecyclerView.VERTICAL
            animeDetailBinding.contentAnime.recyclerAllEpisodesDetail.layoutManager = linearLayoutManagerAllEpisode
            animeDetailBinding.contentAnime.recyclerAllEpisodesDetail.adapter = RecyclerAllEpisodeDetailAdapter(this@AnimeDetailActivity, detailAllEpisodeDataList, animeDetailModel)
        }
    }

    override fun onGetAllEpisodeFailed() {
        runOnUiThread { Toast.makeText(this@AnimeDetailActivity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show() }
    }

    override fun onGetGenreSuccess(genresList: List<DetailMangaModel.DetailMangaGenres>) {
        runOnUiThread {
            animeDetailBinding.contentAnime.animeAboutLayout.recyclerGenreAnime.setHasFixedSize(true)
            val linearLayoutManagerGenre = LinearLayoutManager(this@AnimeDetailActivity)
            linearLayoutManagerGenre.orientation = RecyclerView.HORIZONTAL
            animeDetailBinding.contentAnime.animeAboutLayout.recyclerGenreAnime.layoutManager = linearLayoutManagerGenre
            animeDetailBinding.contentAnime.animeAboutLayout.recyclerGenreAnime.adapter = RecyclerGenreAdapter(this@AnimeDetailActivity, genresList)
        }
    }

    override fun onGetGenreFailed() {
        runOnUiThread { Toast.makeText(this@AnimeDetailActivity, "Your internet connection is worse than your face onii-chan :3", Toast.LENGTH_SHORT).show() }
    }
}
