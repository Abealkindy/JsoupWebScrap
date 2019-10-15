package com.example.myapplication.localstorages;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class MangaLocalDataStorage {
    private Context context;

    public MangaLocalDataStorage(Context context) {
        this.context = context;
        Hawk.init(context).build();
    }

    public void setMangaThumbnail(String thumbnailURL) {
        Hawk.put("thumbnailURL", thumbnailURL);
    }

    public String getMangaThumbnail() {
        return Hawk.get("thumbnailURL");
    }

    public void setMangaType(String mangaType) {
        Hawk.put("mangaType", mangaType);
    }

    public String getMangaType() {
        return Hawk.get("mangaType");
    }

    public void setMangaStatus(boolean mangaStatus) {
        Hawk.put("mangaStatus", mangaStatus);
    }

    public boolean getMangaStatus() {
        return Hawk.get("mangaStatus");
    }

    public void setMangaTitle(String mangaTitle) {
        Hawk.put("mangaTitle", mangaTitle);
    }

    public String getMangaTitle() {
        return Hawk.get("mangaTitle");
    }

    public void setMangaRating(String mangaRating) {
        Hawk.put("mangaRating", mangaRating);
    }

    public String getMangaRating() {
        return Hawk.get("mangaRating");
    }
}
