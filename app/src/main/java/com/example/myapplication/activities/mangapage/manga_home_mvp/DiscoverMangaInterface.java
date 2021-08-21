package com.example.myapplication.activities.mangapage.manga_home_mvp;

import com.example.myapplication.models.animemodels.AnimeGenreAndSearchResultModel;
import com.example.myapplication.models.mangamodels.DiscoverMangaModel;

import java.util.List;

public interface DiscoverMangaInterface {
    void onGetDiscoverMangaDataSuccess(List<DiscoverMangaModel> discoverMangaResultList);

    void onGetDiscoverMangaDataFailed();

    void onGetStatusDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> statusResultList);

    void onGetTypeDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> typeResultList);

    void onGetSortDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> sortResultList);

    void onGetGenreDataSuccess(List<AnimeGenreAndSearchResultModel.AnimeGenreResult> genreResultList);

}
