package com.example.myapplication.models.animemodels;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AnimeNewReleaseResultModel implements Serializable {

    private String animeEpisode;
    private String animeEpisodeNumber;
    private String animeEpisodeType;
    private String animeEpisodeStatus;
    private String episodeThumb;
    private String episodeURL;

    @NonNull
    @Override
    public String toString() {
        return "AnimeNewReleaseResult{" +
                "animeEpisode='" + animeEpisode + '\'' +
                ", animeEpisodeNumber='" + animeEpisodeNumber + '\'' +
                ", animeEpisodeType='" + animeEpisodeType + '\'' +
                ", animeEpisodeStatus='" + animeEpisodeStatus + '\'' +
                ", episodeThumb='" + episodeThumb + '\'' +
                ", episodeURL='" + episodeURL + '\'' +
                '}';
    }
}
