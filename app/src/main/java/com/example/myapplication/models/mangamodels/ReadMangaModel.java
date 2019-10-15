package com.example.myapplication.models.mangamodels;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ReadMangaModel {
    private String chapterTitle;
    private List<AllChapterDatas> allChapterDatas;
    private String nextMangaURL;
    private String previousMangaURL;
    private String mangaDetailURL;
    private List<String> imageContent = new ArrayList<>();

    @Data
    public class AllChapterDatas {
        private String chapterTitle;
        private String chapterUrl;
    }

}
