package com.example.myapplication.activities.mangapage.manga_detail_mvp

import com.example.myapplication.models.mangamodels.DetailMangaModel

interface MangaDetailInterface {
    fun onGetDetailDataSuccess(detailMangaModel: DetailMangaModel)

    fun onGetDetailDataFailed()

    fun onGetAllChapterSuccess(detailAllChapterDatasList: List<DetailMangaModel.DetailAllChapterDatas>)

    fun onGetAllChapterFailed()

    fun onGetGenreSuccess(genresList: List<DetailMangaModel.DetailMangaGenres>)

    fun onGetGenreFailed()
}
