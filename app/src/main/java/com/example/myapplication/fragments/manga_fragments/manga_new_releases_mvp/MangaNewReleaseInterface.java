package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp;

import com.example.myapplication.models.mangamodels.DiscoverMangaModel;
import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;

import java.util.List;
import java.util.Map;

public interface MangaNewReleaseInterface {
    /* older version */
//    void onGetNewReleasesDataSuccess(List<MangaNewReleaseResultModel> mangaNewReleaseResultModels, String hitStatus, Map<String, String> cookies);
    void onGetNewReleasesDataSuccess(List<DiscoverMangaModel> mangaNewReleaseResultModels, String hitStatus, Map<String, String> cookies);

    void onGetNewReleasesDataFailed();
}
