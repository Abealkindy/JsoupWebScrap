package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp

import com.example.myapplication.models.mangamodels.DiscoverMangaModel

interface DiscoverMangaInterface {
    fun onGetDiscoverMangaDataSuccess(discoverMangaResultList: List<DiscoverMangaModel>)

    fun onGetDiscoverMangaDataFailed()
}
