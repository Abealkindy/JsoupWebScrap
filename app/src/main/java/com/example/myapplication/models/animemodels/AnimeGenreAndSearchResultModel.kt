package com.example.myapplication.models.animemodels

data class AnimeGenreAndSearchResultModel(
        var animeSearchResults: AnimeSearchResult? = null,
        var animeGenreResults: AnimeGenreResult? = null
) {
    data class AnimeSearchResult(
            var animeTitle: String? = null,
            var animeThumb: String? = null,
            var animeDetailURL: String? = null,
            var animeStatus: String? = null,
            var animeType: String? = null
    )

    data class AnimeGenreResult(
            var genreTitle: String? = null,
            var genreURL: String? = null
    )
}
