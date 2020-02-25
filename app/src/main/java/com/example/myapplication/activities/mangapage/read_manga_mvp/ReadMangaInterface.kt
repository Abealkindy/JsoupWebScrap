package com.example.myapplication.activities.mangapage.read_manga_mvp

import com.example.myapplication.models.mangamodels.ReadMangaModel

interface ReadMangaInterface {
    fun onGetMangaContentDataSuccess(mangaContents: ReadMangaModel)

    fun onGetMangaContentDataFailed()

    fun onGetMangaChaptersDataSuccess(allChapters: List<ReadMangaModel.AllChapterDatas>)

    fun onGetMangaChaptersDataFailed()
}
