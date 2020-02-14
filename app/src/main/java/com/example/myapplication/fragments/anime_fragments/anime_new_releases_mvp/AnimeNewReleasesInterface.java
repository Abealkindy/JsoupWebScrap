package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp;

public interface AnimeNewReleasesInterface {
    void onGetNewReleasesDataSuccess(String newReleasesHTMLResult, String hitStatus);

    void onGetNewReleasesDataFailed();
}
