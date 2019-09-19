package com.example.myapplication;

import java.util.List;

import lombok.Data;

@Data
public class VideoStreamResultModel {
    private String videoUrl;
    private String animeSynopsis;
    private String allEpisodeAnimeURL;
    private String nextEpisodeAnimeURL;
    private String previousEpisodeAnimeURL;
}
