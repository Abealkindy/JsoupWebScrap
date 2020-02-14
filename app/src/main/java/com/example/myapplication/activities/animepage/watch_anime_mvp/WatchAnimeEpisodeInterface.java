package com.example.myapplication.activities.animepage.watch_anime_mvp;

public interface WatchAnimeEpisodeInterface {
    void onGetWatchAnimeEpisodeDataSuccess(String watchHTMLResult);

    void onGetWatchAnimeEpisodeDataFailed();
}
