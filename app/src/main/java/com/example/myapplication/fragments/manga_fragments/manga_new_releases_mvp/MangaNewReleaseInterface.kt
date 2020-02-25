package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp

import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel

interface MangaNewReleaseInterface {
    fun onGetNewReleasesDataSuccess(mangaNewReleaseResultModels: List<MangaNewReleaseResultModel>, hitStatus: String)

    fun onGetNewReleasesDataFailed()
}
