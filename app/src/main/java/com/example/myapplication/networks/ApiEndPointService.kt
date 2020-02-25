package com.example.myapplication.networks


import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiEndPointService {
    @GET
    fun getSearchAnimeData(
            @Url getAllAnimeDataUrl1: String
    ): Observable<String>

    @GET
    fun getGenreAnimeData(
            @Url getAllAnimeDataUrl2: String
    ): Observable<String>

    @GET
    fun getNewReleaseAnimeData(
            @Url getAllAnimeDataUrl3: String
    ): Observable<String>

    @GET
    fun getWatchAnimeData(
            @Url getAllAnimeDataUrl4: String
    ): Observable<String>

    @GET
    fun getNewReleaseMangaData(
            @Url getAllMangaDataUrl: String
    ): Observable<String>

    @GET
    fun getDiscoverMangaData(
            @Url getAllMangaDataUrl: String
    ): Observable<String>

    @GET
    fun getReadMangaData(
            @Url getAllMangaDataUrl: String
    ): Observable<String>

}
