package com.example.myapplication.localstorages.anime_local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AnimeBookmarkDAO {
    @Query("SELECT * FROM tb_anime_bookmark ORDER BY anime_added_date DESC")
    List<AnimeBookmarkModel> getAnimeBookmarkData();

    @Query("SELECT * FROM tb_anime_bookmark ORDER BY anime_added_date ASC")
    List<AnimeBookmarkModel> sortByDateASC();

    @Query("SELECT * FROM tb_anime_bookmark ORDER BY anime_title ASC")
    List<AnimeBookmarkModel> sortByNameASC();

    @Query("SELECT * FROM tb_anime_bookmark ORDER BY anime_title DESC")
    List<AnimeBookmarkModel> sortByNameDESC();

    @Query("SELECT * FROM tb_anime_bookmark ORDER BY anime_rating ASC")
    List<AnimeBookmarkModel> sortByRatingASC();

    @Query("SELECT * FROM tb_anime_bookmark ORDER BY anime_rating DESC")
    List<AnimeBookmarkModel> sortByRatingDESC();

    @Query("SELECT * FROM tb_anime_bookmark WHERE anime_url LIKE :animeURL ")
    AnimeBookmarkModel findByName(String animeURL);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmarkData(AnimeBookmarkModel... animeBookmarkModel);

    @Query("DELETE FROM tb_anime_bookmark WHERE anime_url = :animeURL")
    void deleteBookmarkItem(String animeURL);

    @Query("DELETE FROM tb_anime_bookmark")
    void deleteAllBookmarkItem();

    @Delete()
    void deleteAllBookmarkItem(AnimeBookmarkModel... animeBookmarkModel);
}
