package com.example.myapplication.activities.animepage.watch_anime_mvp;

import com.example.myapplication.models.animemodels.VideoStreamResultModel;

public interface WatchAnimeEpisodeInterface {
    void onGetWatchAnimeEpisodeDataSuccess(VideoStreamResultModel videoStreamResultModel);

    void onGetWatchAnimeEpisodeDataFailed();
}
