package com.example.myapplication.models.mangamodels;

import java.util.List;

import lombok.Data;

@Data
public class DetailMangaModel {
    private String otherNames;
    private String mangaAuthor;
    private String mangaSynopsis;
    private String firstUpdateYear;
    private String totalMangaChapter;
    private String lastMangaUpdateDate;
    private List<DetailMangaGenres> detailGenres;
    private List<DetailAllChapterDatas> allChapterDatas;

    @Data
    public class DetailAllChapterDatas {
        private String chapterTitle;
        private String chapterURL;
        private String chapterReleaseTime;
    }

    @Data
    public class DetailMangaGenres {
        private String genreTitle;
        private String genreURL;
    }
}
