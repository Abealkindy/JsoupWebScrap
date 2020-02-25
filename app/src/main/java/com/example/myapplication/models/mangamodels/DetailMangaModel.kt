package com.example.myapplication.models.mangamodels

data class DetailMangaModel(
        var mangaTitle: String? = null,
        var mangaThumb: String? = null,
        var otherNames: String? = null,
        var mangaAuthor: String? = null,
        var mangaSynopsis: String? = null,
        var mangaStatus: String? = null,
        var mangaType: String? = null,
        var mangaRating: String? = null,
        var firstUpdateYear: String? = null,
        var totalMangaChapter: String? = null,
        var lastMangaUpdateDate: String? = null,
        var detailGenres: List<DetailMangaGenres>? = null,
        var allChapterDatas: List<DetailAllChapterDatas>? = null
) {
    data class DetailAllChapterDatas(
            var chapterTitle: String? = null,
            var chapterURL: String? = null,
            var chapterReleaseTime: String? = null
    )

    data class DetailMangaGenres(
            var genreTitle: String? = null,
            var genreURL: String? = null
    )
}
