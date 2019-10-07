package com.example.myapplication.models.mangamodels;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MangaNewReleaseResultModel implements Serializable {

    private String mangaType;
    private String mangaTitle;
    private String mangaThumb;
    private String mangaDetailURL;
    private List<LatestMangaDetailModel> latestMangaDetail = new ArrayList<>();


    @NonNull
    @Override
    public String toString() {
        return "MangaNewReleaseResult{" +
                "mangaType='" + mangaType + '\'' +
                ", mangaTitle='" + mangaTitle + '\'' +
                ", mangaThumb='" + mangaThumb + '\'' +
                ", mangaDetailURL='" + mangaDetailURL + '\'' +
                ", latestMangaDetail=" + latestMangaDetail +
                '}';
    }

    @Data
    public class LatestMangaDetailModel implements Serializable {
        private List<String> chapterTitle;
        private List<String> chapterURL;
        private List<String> chapterReleaseTime;

        @NonNull
        @Override
        public String toString() {
            return "LatestMangaDetailModel{" +
                    "chapterTitle=" + chapterTitle +
                    ", chapterURL=" + chapterURL +
                    ", chapterReleaseTime=" + chapterReleaseTime +
                    '}';
        }
    }
}
