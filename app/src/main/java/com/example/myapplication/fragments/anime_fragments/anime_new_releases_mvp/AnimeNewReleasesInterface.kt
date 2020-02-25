package com.example.myapplication.fragments.anime_fragments.anime_new_releases_mvp

import com.example.myapplication.models.animemodels.AnimeNewReleaseResultModel

interface AnimeNewReleasesInterface {
    fun onGetNewReleasesDataSuccess(animeNewReleases: List<AnimeNewReleaseResultModel>, hitStatus: String)

    fun onGetNewReleasesDataFailed()
}
