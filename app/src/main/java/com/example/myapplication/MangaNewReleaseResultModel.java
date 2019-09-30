package com.example.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MangaNewReleaseResultModel implements Serializable {

    private String animeEpisode;
    private String animeEpisodeNumber;
    private String animeEpisodeType;
    private String animeEpisodeStatus;
    private String episodeThumb;
    private String episodeURL;
    private List<String> thumbnailUrls;

    @NonNull
    @Override
    public String toString() {
        return "MangaNewReleaseResult{" +
                "animeEpisode='" + animeEpisode + '\'' +
                ", animeEpisodeNumber='" + animeEpisodeNumber + '\'' +
                ", animeEpisodeType='" + animeEpisodeType + '\'' +
                ", thumbnailUrls=" + thumbnailUrls +
                ", animeEpisodeStatus='" + animeEpisodeStatus + '\'' +
                ", episodeThumb='" + episodeThumb + '\'' +
                ", episodeURL='" + episodeURL + '\'' +
                '}';
    }
}
