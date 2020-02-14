package com.example.myapplication.fragments.anime_fragments.genre_and_search_mvp;

public interface GenreAndSearchAnimeInterface {
    void onGetSearchAndGenreDataSuccess(String searchAndGenreHTMLResult);

    void onGetSearchAndGenreDataFailed();

    void onGetOnlyGenreDataSuccess(String onlyGenreHTMLResult);

    void onGetOnlyGenreDataFailed();
}
