package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp;

import com.example.myapplication.models.mangamodels.MangaNewReleaseResultModel;

import java.util.List;

public interface MangaNewReleaseInterface {
    void onGetNewReleasesDataSuccess(List<MangaNewReleaseResultModel> mangaNewReleaseResultModels , String hitStatus);

    void onGetNewReleasesDataFailed();
}
