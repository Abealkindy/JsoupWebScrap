package com.example.myapplication.models.mangamodels;

import java.util.List;

import lombok.Data;

@Data
public class DetailMangaModel {
    private String mangaTitle;
    private String mangaThumb;
    private String otherNames;
    private String mangaAuthor;
    private String mangaSynopsis;
    private String mangaStatus;
    private String mangaType;
    private String mangaRating;
    private String firstUpdateYear;
    private String totalMangaChapter;
    private String lastMangaUpdateDate;
    private List<DetailMangaGenres> detailGenres;
    private List<DetailAllChapterDatas> allChapterDatas;

    @Data
    public static class DetailAllChapterDatas {
        private String chapterTitle;
        private String chapterURL;
        private String chapterReleaseTime;
    }

    @Data
    public static class DetailMangaGenres {
        private String genreTitle;
        private String genreURL;
    }
}
