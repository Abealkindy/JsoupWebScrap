package com.example.myapplication.models.animemodels.roommodels;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AnimeBookmarkModel.class}, version = 3)
public abstract class AnimeAppDatabase extends RoomDatabase {
    public abstract AnimeBookmarkDAO animeBookmarkDAO();
}
