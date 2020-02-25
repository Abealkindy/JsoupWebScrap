package com.example.myapplication.activities.animepage.watch_anime_mvp

interface WatchAnimeEpisodeInterface {
    fun onGetWatchAnimeEpisodeDataSuccess(watchHTMLResult: String)

    fun onGetWatchAnimeEpisodeDataFailed()
}
