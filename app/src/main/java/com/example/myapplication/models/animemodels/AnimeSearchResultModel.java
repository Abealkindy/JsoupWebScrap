package com.example.myapplication.models.animemodels;

import java.util.List;

import lombok.Data;

@Data
public class AnimeSearchResultModel {
    private List<AnimeSearchResult> animeSearchResults;

    @Data
    public class AnimeSearchResult {
        private String animeTitle;
        private String animeThumb;
        private String animeDetailUrl;
    }
}
