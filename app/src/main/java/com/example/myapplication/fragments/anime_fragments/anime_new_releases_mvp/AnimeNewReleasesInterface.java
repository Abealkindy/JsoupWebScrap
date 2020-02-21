package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp;

import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel;

import java.util.List;

public interface AnimeNewReleasesInterface {
    void onGetNewReleasesDataSuccess(List<AnimeNewReleaseResultModel> animeNewReleases, String hitStatus);

    void onGetNewReleasesDataFailed();
}
