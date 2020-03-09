package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.example.myapplication.localstorages.LocalAppDB;

public class MyApp extends Application {
    public static LocalAppDB localAppDB;

    @Override
    public void onCreate() {
        super.onCreate();
        localAppDB = Room.databaseBuilder(
                getApplicationContext(),
                LocalAppDB.class,
                "local_bookmark_db"
        )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}
