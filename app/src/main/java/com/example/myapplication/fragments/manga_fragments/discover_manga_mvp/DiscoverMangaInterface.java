package com.example.myapplication.fragments.manga_fragments.discover_manga_mvp;

import com.example.myapplication.models.mangamodels.DiscoverMangaModel;

import java.util.List;

public interface DiscoverMangaInterface {
    void onGetDiscoverMangaDataSuccess(List<DiscoverMangaModel> discoverMangaResultList);

    void onGetDiscoverMangaDataFailed();
}
