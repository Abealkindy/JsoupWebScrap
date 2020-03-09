package com.example.myapplication.localstorages.manga_local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "tb_manga_bookmark")
public class MangaBookmarkModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "manga_url")
    String mangaDetailURL = "";
    @ColumnInfo(name = "manga_added_date")
    String mangaAddedDate = "";
    @ColumnInfo(name = "manga_title")
    String mangaTitle = "";
    @ColumnInfo(name = "manga_type")
    String mangaType = "";
    @ColumnInfo(name = "manga_rating")
    String mangaRating = "";
    @ColumnInfo(name = "manga_thumb")
    String mangaThumb = "";
    @ColumnInfo(name = "manga_status")
    boolean mangaStatus = false;
}
