package com.example.myapplication.localstorages;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class AnimeLocalDataStorage {
    private Context context;

    public AnimeLocalDataStorage(Context context) {
        this.context = context;
        Hawk.init(context).build();
    }

}
