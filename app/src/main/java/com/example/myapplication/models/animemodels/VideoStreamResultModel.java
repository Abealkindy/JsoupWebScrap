package com.example.myapplication.models.animemodels;


import lombok.Data;

@Data
public class VideoStreamResultModel {
    private String videoUrl;
    private String videoPrevUrl;
    private String videoNextUrl;
    private String episodeTitle;
    private String animeDetailURL;
    private String animeType;
    private String animeStatus;
    private String animeThumb;
}
