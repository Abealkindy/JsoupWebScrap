package com.example.myapplication;

import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiEndPointService {
    @GET
    Observable<String> getSearchAnimeData(
            @Url String getAllAnimeDataUrl
    );

    @GET
    Observable<String> getNewReleaseAnimeData(
            @Url String getAllAnimeDataUrl
    );

    @GET
    Observable<String> getWatchAnimeData(
            @Url String getAllAnimeDataUrl
    );

}
