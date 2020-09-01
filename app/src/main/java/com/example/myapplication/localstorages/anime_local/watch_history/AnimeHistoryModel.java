package com.example.myapplication.localstorages.anime_local.watch_history;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "tb_anime_history")
public class AnimeHistoryModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "anime_url")
    String animeDetailURL = "";
    @ColumnInfo(name = "anime_added_date")
    String animeAddedDate = "";
    @ColumnInfo(name = "anime_title")
    String animeTitle = "";
    @ColumnInfo(name = "anime_thumb")
    String animeThumb = "";
    @ColumnInfo(name = "anime_type")
    String animeType = "";
}
