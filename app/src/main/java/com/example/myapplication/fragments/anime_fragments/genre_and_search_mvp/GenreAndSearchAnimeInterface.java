package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp;

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;

import java.util.List;

public interface GenreAndSearchAnimeInterface {
    void onGetSearchAndGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeSearchResult> searchAndGenreHTMLResult);

    void onGetSearchAndGenreDataFailed();

    void onGetOnlyGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> onlyGenreHTMLResult);

    void onGetOnlyGenreDataFailed();
}
