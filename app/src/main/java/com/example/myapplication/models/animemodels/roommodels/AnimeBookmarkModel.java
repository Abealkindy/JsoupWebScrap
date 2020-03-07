package com.example.myapplication.models.animemodels.roommodels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "tb_anime_bookmark")
public class AnimeBookmarkModel {
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
    @ColumnInfo(name = "anime_status")
    String animeStatus = "";
    @ColumnInfo(name = "anime_type")
    String animeType = "";
}
