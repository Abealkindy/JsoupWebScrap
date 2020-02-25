package com.example.myapplication.activities.animepage.anime_detail_mvp

import com.example.myapplication.models.mangamodels.DetailMangaModel

interface AnimeDetailInterface {
    fun onGetDetailDataSuccess(detailMangaModel: DetailMangaModel)

    fun onGetDetailDataFailed()

    fun onGetAllEpisodeSuccess(detailAllEpisodeDataList: List<DetailMangaModel.DetailAllChapterDatas>)

    fun onGetAllEpisodeFailed()

    fun onGetGenreSuccess(genresList: List<DetailMangaModel.DetailMangaGenres>)

    fun onGetGenreFailed()
}
