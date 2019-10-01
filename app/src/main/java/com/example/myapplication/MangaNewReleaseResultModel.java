package com.example.myapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MangaNewReleaseResultModel implements Serializable {

    private String mangaType;
    private String mangaTitle;
    private String mangaThumb;
    private String mangaDetailURL;
    private List<LatestMangaDetailModel> latestMangaDetail;

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
        private String chapterURL;
        private String chapterReleaseTime;

        @NonNull
        @Override
        public String toString() {
            return "LatestMangaDetailModel{" +
                    "chapterURL='" + chapterURL + '\'' +
                    ", chapterReleaseTime='" + chapterReleaseTime + '\'' +
                    '}';
        }
    }
}
