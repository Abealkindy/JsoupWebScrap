package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.example.myapplication.models.animemodels.roommodels.AnimeAppDatabase;

public class MyApp extends Application {
    public static AnimeAppDatabase animeLocalDB;

    @Override
    public void onCreate() {
        super.onCreate();
        animeLocalDB = Room.databaseBuilder(
                getApplicationContext(),
                AnimeAppDatabase.class,
                "anime_bookmark_db"
        )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}
