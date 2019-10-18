package com.example.myapplication.activities.mangapage.manga_detail_mvp;

public interface MangaDetailInterface {
    void onGetDetailDataSuccess(String detailHTMLResult);

    void onGetDetailDataFailed();
}
