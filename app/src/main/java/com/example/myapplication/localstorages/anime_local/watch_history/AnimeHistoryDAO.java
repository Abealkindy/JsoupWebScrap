package com.example.myapplication.localstorages.anime_local.watch_history;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.localstorages.anime_local.watch_history.AnimeHistoryModel;

import java.util.List;

@Dao
public interface AnimeHistoryDAO {
    @Query("SELECT * FROM tb_anime_history ORDER BY anime_added_date DESC")
    List<AnimeHistoryModel> getAnimeHistoryData();

    @Query("SELECT * FROM tb_anime_history ORDER BY anime_added_date ASC")
    List<AnimeHistoryModel> sortByDateASC();

    @Query("SELECT * FROM tb_anime_history ORDER BY anime_title ASC")
    List<AnimeHistoryModel> sortByNameASC();

    @Query("SELECT * FROM tb_anime_history ORDER BY anime_title DESC")
    List<AnimeHistoryModel> sortByNameDESC();

    @Query("SELECT * FROM tb_anime_history ORDER BY anime_type ASC")
    List<AnimeHistoryModel> sortByRatingASC();

    @Query("SELECT * FROM tb_anime_history ORDER BY anime_type DESC")
    List<AnimeHistoryModel> sortByRatingDESC();

    @Query("SELECT * FROM tb_anime_history WHERE anime_url LIKE :animeURL ")
    AnimeHistoryModel findByName(String animeURL);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistoryData(AnimeHistoryModel... animeHistoryModel);

    @Query("DELETE FROM tb_anime_history WHERE anime_url = :animeURL")
    void deleteHistoryItem(String animeURL);

    @Query("DELETE FROM tb_anime_history")
    void deleteAllHistoryItem();

    @Delete()
    void deleteAllHistoryItem(AnimeHistoryModel... animeHistoryModel);
}
