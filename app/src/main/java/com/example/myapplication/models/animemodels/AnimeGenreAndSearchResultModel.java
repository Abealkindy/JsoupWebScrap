package com.example.myapplication.models.animemodels;

import java.util.List;

import lombok.Data;

@Data
public class AnimeGenreAndSearchResultModel {
    private AnimeSearchResult animeSearchResults;
    private AnimeSearchResultNew animeSearchResultsNew;
    private AnimeGenreResult animeGenreResults;

    @Data
    public class AnimeSearchResult {
        private String animeTitle;
        private String animeThumb;
        private String animeDetailURL;
        private String animeStatus;
        private String animeType;
    }

    @Data
    public class AnimeSearchResultNew {
        private String animeTitle;
        private String animeThumb;
        private String animeDetailURL;
        private String animeGenre;
        private String animeRating;
    }

    @Data
    public class AnimeGenreResult {
        private String genreTitle;
        private String genreURL;
    }
}
