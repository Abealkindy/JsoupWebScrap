package com.example.myapplication.localstorages;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.localstorages.anime_local.AnimeBookmarkDAO;
import com.example.myapplication.localstorages.anime_local.AnimeBookmarkModel;
import com.example.myapplication.localstorages.manga_local.manga_bookmark.MangaBookmarkDAO;
import com.example.myapplication.localstorages.manga_local.manga_bookmark.MangaBookmarkModel;
import com.example.myapplication.localstorages.manga_local.read_history.MangaHistoryDAO;
import com.example.myapplication.localstorages.manga_local.read_history.MangaHistoryModel;

@Database(entities = {AnimeBookmarkModel.class, MangaBookmarkModel.class, MangaHistoryModel.class}, version = 7)
public abstract class LocalAppDB extends RoomDatabase {
    public abstract AnimeBookmarkDAO animeBookmarkDAO();
    public abstract MangaBookmarkDAO mangaBookmarkDAO();
    public abstract MangaHistoryDAO mangaHistoryDAO();
}
