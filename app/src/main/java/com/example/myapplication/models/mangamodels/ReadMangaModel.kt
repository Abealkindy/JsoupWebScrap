package com.example.myapplication.models.mangamodels


data class ReadMangaModel(
        var chapterTitle: String? = null,
        var allChapterDatas: List<AllChapterDatas>? = null,
        var nextMangaURL: String? = null,
        var previousMangaURL: String? = null,
        var mangaDetailURL: String? = null,
        var imageContent: MutableList<String>? = mutableListOf()
) {
    data class AllChapterDatas(
            var chapterTitle: String? = null,
            var chapterUrl: String? = null
    )
}
