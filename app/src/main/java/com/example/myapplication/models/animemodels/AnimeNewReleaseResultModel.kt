package com.example.myapplication.models.animemodels

import java.io.Serializable

data class AnimeNewReleaseResultModel(
        var animeEpisode: String? = null,
        var animeEpisodeNumber: String? = null,
        var animeEpisodeType: String? = null,
        var animeEpisodeStatus: String? = null,
        var episodeThumb: String? = null,
        var episodeURL: String? = null
) : Serializable {
    override fun toString(): String {
        return "AnimeNewReleaseResult{" +
                "animeEpisode='" + this.animeEpisode + '\''.toString() +
                ", animeEpisodeNumber='" + this.animeEpisodeNumber + '\''.toString() +
                ", animeEpisodeType='" + this.animeEpisodeType + '\''.toString() +
                ", animeEpisodeStatus='" + this.animeEpisodeStatus + '\''.toString() +
                ", episodeThumb='" + this.episodeThumb + '\''.toString() +
                ", episodeURL='" + this.episodeURL + '\''.toString() +
                '}'.toString()
    }
}