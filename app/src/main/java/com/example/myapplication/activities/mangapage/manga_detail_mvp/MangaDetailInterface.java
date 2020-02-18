package com.example.myapplication.activities.mangapage.manga_detail_mvp;

import com.example.myapplication.models.mangamodels.DetailMangaModel;

import java.util.List;

public interface MangaDetailInterface {
    void onGetDetailDataSuccess(DetailMangaModel detailMangaModel);

    void onGetDetailDataFailed();

    void onGetAllChapterSuccess(List<DetailMangaModel.DetailAllChapterDatas> detailAllChapterDatasList);

    void onGetAllChapterFailed();

    void onGetGenreSuccess(List<DetailMangaModel.DetailMangaGenres> genresList);

    void onGetGenreFailed();
}
