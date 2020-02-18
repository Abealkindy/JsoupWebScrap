package com.example.myapplication.activities.animepage.anime_detail_mvp;

import com.example.myapplication.models.mangamodels.DetailMangaModel;

import java.util.List;

public interface AnimeDetailInterface {
    void onGetDetailDataSuccess(DetailMangaModel detailMangaModel);

    void onGetDetailDataFailed();

    void onGetAllEpisodeSuccess(List<DetailMangaModel.DetailAllChapterDatas> detailAllEpisodeDataList);

    void onGetAllEpisodeFailed();

    void onGetGenreSuccess(List<DetailMangaModel.DetailMangaGenres> genresList);

    void onGetGenreFailed();
}
