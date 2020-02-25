package com.example.myapplication.models.mangamodels

import java.io.Serializable
import java.util.ArrayList

data class MangaNewReleaseResultModel(
        var mangaType: String? = null,
        var mangaTitle: String? = null,
        var mangaThumb: String? = null,
        var mangaDetailURL: String? = null,
        var isMangaStatus: Boolean = false,
        var latestMangaDetail: List<LatestMangaDetailModel> = ArrayList()
) : Serializable {

    data class LatestMangaDetailModel(
            var chapterTitle: List<String>? = null,
            var chapterURL: List<String>? = null,
            var chapterReleaseTime: List<String>? = null
    ) : Serializable {

        override fun toString(): String {
            return "LatestMangaDetailModel{" +
                    "chapterTitle=" + this.chapterTitle +
                    ", chapterURL=" + this.chapterURL +
                    ", chapterReleaseTime=" + this.chapterReleaseTime +
                    '}'.toString()
        }
    }

    override fun toString(): String {
        return "MangaNewReleaseResult{" +
                "mangaType='" + this.mangaType + '\''.toString() +
                ", mangaTitle='" + this.mangaTitle + '\''.toString() +
                ", mangaThumb='" + this.mangaThumb + '\''.toString() +
                ", mangaDetailURL='" + this.mangaDetailURL + '\''.toString() +
                ", latestMangaDetail=" + this.latestMangaDetail +
                '}'.toString()
    }
}
