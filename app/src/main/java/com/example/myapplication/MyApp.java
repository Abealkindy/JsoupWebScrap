package com.example.myapplication;

import android.app.Application;
import android.os.StrictMode;

import androidx.room.Room;

import com.example.myapplication.localstorages.LocalAppDB;

import java.util.Map;

public class MyApp extends Application {
    public static LocalAppDB localAppDB;
    public static Map<String, String> cookiesz;

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
