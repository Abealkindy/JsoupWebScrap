package com.example.myapplication;


import lombok.Data;

@Data
public class VideoStreamResultModel {
    private String videoUrl;
    private String episodeTitle;
    private String allEpisodeAnimeURL;
    private String nextEpisodeAnimeURL;
    private String previousEpisodeAnimeURL;
}
