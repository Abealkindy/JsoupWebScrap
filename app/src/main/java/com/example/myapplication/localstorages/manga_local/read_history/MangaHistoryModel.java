package com.example.myapplication.localstorages.manga_local.read_history;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "tb_manga_history")
public class MangaHistoryModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "chapter_url")
    String chapterURL = "";
    @ColumnInfo(name = "chapter_added_date")
    String chapterAddedDate = "";
    @ColumnInfo(name = "chapter_title")
    String chapterTitle = "";
    @ColumnInfo(name = "chapter_thumb")
    String chapterThumb = "";
    @ColumnInfo(name = "chapter_type")
    String chapterType = "";
}
