package com.example.myapplication.networks;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiEndPointService {
    @GET
    Observable<String> getSearchAnimeData(
            @Url String getAllAnimeDataUrl1
    );

    @GET
    Observable<String> getGenreAnimeData(
            @Url String getAllAnimeDataUrl2
    );

    @GET
    Observable<String> getNewReleaseAnimeData(
            @Url String getAllAnimeDataUrl3
    );

    @GET
    Observable<String> getWatchAnimeData(
            @Url String getAllAnimeDataUrl4
    );

    @GET
    Observable<String> getNewReleaseMangaData(
            @Url String getAllMangaDataUrl
    );

    @GET
    Observable<String> getDiscoverMangaData(
            @Url String getAllMangaDataUrl
    );

    @GET
    Observable<String> getReadMangaData(
            @Url String getAllMangaDataUrl
    );

}
