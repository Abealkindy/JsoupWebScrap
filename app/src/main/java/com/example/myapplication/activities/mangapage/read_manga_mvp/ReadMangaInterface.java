package com.example.myapplication.activities.mangapage.read_manga_mvp;

import com.example.myapplication.models.mangamodels.ReadMangaModel;

import java.util.List;

public interface ReadMangaInterface {
    void onGetMangaContentDataSuccess(ReadMangaModel mangaContents);

    void onGetMangaContentDataFailed();

    void onGetMangaChaptersDataSuccess(List<ReadMangaModel.AllChapterDatas> allChapters);

    void onGetMangaChaptersDataFailed();
}
