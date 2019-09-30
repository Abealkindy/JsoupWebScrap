package com.example.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class NewReleaseResultModel implements Serializable {

    private String animeEpisode;
    private String animeEpisodeNumber;
    private String animeEpisodeType;
    private String animeEpisodeStatus;
    private String episodeThumb;
    private String episodeURL;

    @NonNull
    @Override
    public String toString() {
        return "NewReleaseResult{" +
                "animeEpisode='" + animeEpisode + '\'' +
                ", animeEpisodeNumber='" + animeEpisodeNumber + '\'' +
                ", animeEpisodeType='" + animeEpisodeType + '\'' +
                ", animeEpisodeStatus='" + animeEpisodeStatus + '\'' +
                ", episodeThumb='" + episodeThumb + '\'' +
                ", episodeURL='" + episodeURL + '\'' +
                '}';
    }
}
