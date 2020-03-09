package com.example.myapplication.localstorages;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.localstorages.anime_local.AnimeBookmarkDAO;
import com.example.myapplication.localstorages.anime_local.AnimeBookmarkModel;
import com.example.myapplication.localstorages.manga_local.MangaBookmarkDAO;
import com.example.myapplication.localstorages.manga_local.MangaBookmarkModel;

@Database(entities = {AnimeBookmarkModel.class, MangaBookmarkModel.class}, version = 5)
public abstract class LocalAppDB extends RoomDatabase {
    public abstract AnimeBookmarkDAO animeBookmarkDAO();
    public abstract MangaBookmarkDAO mangaBookmarkDAO();
}
