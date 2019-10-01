package com.example.myapplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitConfig {
    private static Retrofit setInitAnimeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://animeindo.to")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static Retrofit setInitMangaRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://komikcast.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static ApiEndPointService getInitAnimeRetrofit() {
        return setInitAnimeRetrofit().create(ApiEndPointService.class);
    }

    public static ApiEndPointService getInitMangaRetrofit() {
        return setInitMangaRetrofit().create(ApiEndPointService.class);
    }

}
