package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.example.myapplication.localstorages.LocalAppDB;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    public static LocalAppDB localAppDB;
    public static Map<String, String> cookiesz = new HashMap<String, String>() {
        @Override
        public String get(Object key) {
            if (!containsKey(key))
                return "";
            return super.get(key);
        }
    };
    ;
    public static final String ua = "Mozilla/5.0 (Linux; Android 6.0.1; SM-G920V Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36";

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
