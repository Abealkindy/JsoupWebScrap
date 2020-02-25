package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel

interface GenreAndSearchAnimeInterface {
    fun onGetSearchAndGenreDataSuccess(searchAndGenreHTMLResult: List<AnimeGenreAndSearchResultModel.AnimeSearchResult>)

    fun onGetSearchAndGenreDataFailed()

    fun onGetOnlyGenreDataSuccess(onlyGenreHTMLResult: List<AnimeGenreAndSearchResultModel.AnimeGenreResult>)

    fun onGetOnlyGenreDataFailed()
}
