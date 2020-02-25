package com.example.myapplication.activities.mangapage.manga_detail_mvp

import android.os.Bundle

import com.example.myapplication.adapters.mangaadapters.recycleradapters.RecyclerAllChapterDetailAdapter
import com.example.myapplication.adapters.RecyclerGenreAdapter
import com.example.myapplication.databinding.ActivityMangaDetailBinding
import com.example.myapplication.models.mangamodels.DetailMangaModel
import com.google.android.material.appbar.AppBarLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.widget.Toast
import androidx.appcompat.app.ActionBar

import com.example.myapplication.R
import com.squareup.picasso.Picasso
import java.util.Objects

class MangaDetailActivity : AppCompatActivity(), MangaDetailInterface {

    internal lateinit var detailBinding: ActivityMangaDetailBinding
    private var mangaType: String? = null
    private var detailType: String? = null
    private var detailTitle: String? = null
    private var detailThumb: String? = null
    private var detailRating: String? = null
    private var detailStatus: Boolean = false
    private val mangaDetailPresenter = MangaDetailPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_manga_detail)
        setSupportActionBar(detailBinding.toolbar)
        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initVariables()
    }

    private fun initCollapsingToolbar(titleManga: String) {
        detailBinding.toolbarLayout.title = ""
        detailBinding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = detailBinding.appBar.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    detailBinding.toolbarLayout.title = titleManga
                    isShow = true
                } else if (isShow) {
                    detailBinding.toolbarLayout.title = " "
                    isShow = false
                }
            }
        })

    }

    private fun initVariables() {
        val detailURL = intent.getStringExtra("detailURL")
        detailType = intent.getStringExtra("detailType")
        detailThumb = intent.getStringExtra("detailThumb")
        detailTitle = intent.getStringExtra("detailTitle")
        detailRating = intent.getStringExtra("detailRating")
        detailStatus = intent.getBooleanExtra("detailStatus", false)

        if (!detailThumb!!.equals("", ignoreCase = true)) {
            Picasso.get().load(detailThumb).into(detailBinding.headerThumbnailDetail)
        }
        if (!detailTitle!!.equals("", ignoreCase = true)) {
            detailBinding.detailHeaderTitle.text = detailTitle
            initCollapsingToolbar(detailTitle!!)
        }
        if (!detailType!!.equals("", ignoreCase = true)) {
            mangaType = detailType
            when {
                detailType!!.equals(resources.getString(R.string.manga_string), ignoreCase = true) -> {
                    detailBinding.mangaTypeDetail.text = resources.getString(R.string.manga_string)
                    detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manga)
                }
                detailType!!.equals(resources.getString(R.string.manhua_string), ignoreCase = true) -> {
                    detailBinding.mangaTypeDetail.text = resources.getString(R.string.manhua_string)
                    detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manhua)
                }
                detailType!!.equals(resources.getString(R.string.manhwa_string), ignoreCase = true) -> {
                    detailBinding.mangaTypeDetail.text = resources.getString(R.string.manhwa_string)
                    detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manhwa)
                }
                detailType!!.equals(resources.getString(R.string.mangaoneshot_string), ignoreCase = true) -> {
                    detailBinding.mangaTypeDetail.text = resources.getString(R.string.mangaoneshot_string)
                    detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manga)
                }
                detailType!!.equals(resources.getString(R.string.oneshot_string), ignoreCase = true) -> {
                    detailBinding.mangaTypeDetail.text = resources.getString(R.string.oneshot_string)
                    detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manga)
                }
            }
        }
        if (!detailStatus.toString().equals("", ignoreCase = true)) {
            if (detailStatus) {
                detailBinding.detailStatus.background = resources.getDrawable(R.drawable.bubble_background_completed)
                detailBinding.detailStatus.text = resources.getString(R.string.completed_text)
            } else {
                detailBinding.detailStatus.background = resources.getDrawable(R.drawable.bubble_background_ongoing)
                detailBinding.detailStatus.text = resources.getString(R.string.ongoing_text)
            }
        }
        if (!detailRating!!.equals("", ignoreCase = true)) {
            detailBinding.ratingBarDetail.numStars = 5
            val replaceComma = detailRating!!.replace(",", ".")
            if (detailRating!!.equals("N/A", ignoreCase = true) || detailRating!!.equals("?", ignoreCase = true) || detailRating!!.equals("-", ignoreCase = true)) {
                detailBinding.ratingBarDetail.rating = 0f
                detailBinding.ratingNumberDetail.text = detailRating
            } else if (java.lang.Float.parseFloat(replaceComma) <= 0) {
                detailBinding.ratingBarDetail.rating = 0f
                detailBinding.ratingNumberDetail.text = detailRating
            } else {
                detailBinding.ratingBarDetail.rating = java.lang.Float.parseFloat(replaceComma) / 2
                detailBinding.ratingNumberDetail.text = replaceComma
            }

        }
        if (detailURL != null) {
            mangaDetailPresenter.getDetailMangaData(detailURL)
        }
    }

    override fun onGetDetailDataSuccess(detailMangaModel: DetailMangaModel) {
        runOnUiThread {
            //get title
            if (detailTitle!!.equals("", ignoreCase = true)) {
                detailBinding.detailHeaderTitle.text = detailMangaModel.mangaTitle
                initCollapsingToolbar(detailTitle!!)
            }

            //get thumb
            if (detailThumb!!.equals("", ignoreCase = true)) {
                Picasso.get().load(detailMangaModel.mangaThumb).into(detailBinding.headerThumbnailDetail)
            }

            //get Synopsis
            detailBinding.contentManga.textSynopsis.text = detailMangaModel.mangaSynopsis

            //get Updated on
            detailBinding.contentManga.mangaAboutLayout.latestUpdateDate.text = detailMangaModel.lastMangaUpdateDate

            //get Other name
            detailBinding.contentManga.mangaAboutLayout.textOtherName.text = detailMangaModel.otherNames

            //get Author
            if (detailMangaModel.mangaAuthor == null || detailMangaModel.mangaAuthor.isEmpty()) {
                detailBinding.contentManga.mangaAboutLayout.textAuthor.text = "-"
            } else {
                detailBinding.contentManga.mangaAboutLayout.textAuthor.text = detailMangaModel.mangaAuthor
            }

            //get released on
            if (detailMangaModel.firstUpdateYear == null || detailMangaModel.firstUpdateYear.isEmpty()) {
                detailBinding.contentManga.mangaAboutLayout.textReleasedOn.text = "-"
            } else {
                detailBinding.contentManga.mangaAboutLayout.textReleasedOn.text = detailMangaModel.firstUpdateYear
            }

            //get total chapter
            if (detailMangaModel.totalMangaChapter == null || detailMangaModel.totalMangaChapter.isEmpty()) {
                detailBinding.contentManga.mangaAboutLayout.textTotalChapters.text = "-"
            } else {
                detailBinding.contentManga.mangaAboutLayout.textTotalChapters.text = detailMangaModel.totalMangaChapter
            }

            //get manga status
            if (detailStatus.toString().equals("", ignoreCase = true)) {
                if (detailMangaModel.mangaStatus.equals(resources.getString(R.string.completed_text), ignoreCase = true)) {
                    detailBinding.detailStatus.background = resources.getDrawable(R.drawable.bubble_background_completed)
                    detailBinding.detailStatus.text = resources.getString(R.string.completed_text)
                } else {
                    detailBinding.detailStatus.background = resources.getDrawable(R.drawable.bubble_background_ongoing)
                    detailBinding.detailStatus.text = resources.getString(R.string.ongoing_text)
                }
            }

            //get manga type
            if (detailType!!.equals("", ignoreCase = true)) {
                detailType = detailMangaModel.mangaType
                mangaType = detailType
                when {
                    detailType!!.equals(resources.getString(R.string.manga_string), ignoreCase = true) -> {
                        detailBinding.mangaTypeDetail.text = resources.getString(R.string.manga_string)
                        detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manga)
                    }
                    detailType!!.equals(resources.getString(R.string.manhua_string), ignoreCase = true) -> {
                        detailBinding.mangaTypeDetail.text = resources.getString(R.string.manhua_string)
                        detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manhua)
                    }
                    detailType!!.equals(resources.getString(R.string.manhwa_string), ignoreCase = true) -> {
                        detailBinding.mangaTypeDetail.text = resources.getString(R.string.manhwa_string)
                        detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manhwa)
                    }
                    detailType!!.equals(resources.getString(R.string.mangaoneshot_string), ignoreCase = true) -> {
                        detailBinding.mangaTypeDetail.text = resources.getString(R.string.mangaoneshot_string)
                        detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manga)
                    }
                    detailType!!.equals(resources.getString(R.string.oneshot_string), ignoreCase = true) -> {
                        detailBinding.mangaTypeDetail.text = resources.getString(R.string.oneshot_string)
                        detailBinding.mangaTypeDetail.background = resources.getDrawable(R.drawable.bubble_background_manga)
                    }
                }
            }

            //get manga rating
            if (detailRating!!.equals("", ignoreCase = true)) {
                detailRating = detailMangaModel.mangaRating
                detailBinding.ratingBarDetail.numStars = 5
                if (detailRating!!.equals("N/A", ignoreCase = true) || detailRating!!.equals("?", ignoreCase = true) || detailRating!!.equals("-", ignoreCase = true)) {
                    detailBinding.ratingBarDetail.rating = 0f
                    detailBinding.ratingNumberDetail.text = detailRating
                } else if (java.lang.Float.parseFloat(detailRating!!) <= 0) {
                    detailBinding.ratingBarDetail.rating = 0f
                    detailBinding.ratingNumberDetail.text = detailRating
                } else {
                    detailBinding.ratingBarDetail.rating = java.lang.Float.parseFloat(detailRating!!) / 2
                    detailBinding.ratingNumberDetail.text = detailRating
                }
            }
        }
    }

    override fun onGetDetailDataFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show()
    }

    override fun onGetAllChapterSuccess(detailAllChapterDatasList: List<DetailMangaModel.DetailAllChapterDatas>) {
        runOnUiThread {
            detailBinding.contentManga.recyclerAllChaptersDetail.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(this@MangaDetailActivity)
            linearLayoutManager.orientation = RecyclerView.VERTICAL
            detailBinding.contentManga.recyclerAllChaptersDetail.layoutManager = linearLayoutManager
            detailBinding.contentManga.recyclerAllChaptersDetail.adapter = RecyclerAllChapterDetailAdapter(this@MangaDetailActivity, detailAllChapterDatasList, mangaType)
        }
    }

    override fun onGetAllChapterFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show()
    }

    override fun onGetGenreSuccess(genresList: List<DetailMangaModel.DetailMangaGenres>) {
        runOnUiThread {
            detailBinding.contentManga.mangaAboutLayout.recyclerGenre.setHasFixedSize(true)
            val linearLayoutManagerGenre = LinearLayoutManager(this@MangaDetailActivity)
            linearLayoutManagerGenre.orientation = RecyclerView.HORIZONTAL
            detailBinding.contentManga.mangaAboutLayout.recyclerGenre.layoutManager = linearLayoutManagerGenre
            detailBinding.contentManga.mangaAboutLayout.recyclerGenre.adapter = RecyclerGenreAdapter(this@MangaDetailActivity, genresList)
        }

    }

    override fun onGetGenreFailed() {
        Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show()
    }
}
