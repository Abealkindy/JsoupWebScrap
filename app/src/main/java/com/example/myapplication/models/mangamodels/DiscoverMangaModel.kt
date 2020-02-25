package com.example.myapplication.models.mangamodels

data class DiscoverMangaModel(
        var mangaTitle: String? = null,
        var mangaType: String? = null,
        var mangaThumb: String? = null,
        var mangaLatestChapter: String? = null,
        var mangaLatestChapterText: String? = null,
        var mangaRating: String? = null,
        var mangaURL: String? = null,
        var isMangaStatus: Boolean = false
)