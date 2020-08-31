package com.example.myapplication.localstorages.manga_local.read_history;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MangaHistoryDAO {
    @Query("SELECT * FROM tb_manga_history ORDER BY chapter_added_date DESC")
    List<MangaHistoryModel> getMangaHistoryData();

    @Query("SELECT * FROM tb_manga_history ORDER BY chapter_added_date ASC")
    List<MangaHistoryModel> sortByDateASC();

    @Query("SELECT * FROM tb_manga_history ORDER BY chapter_title ASC")
    List<MangaHistoryModel> sortByNameASC();

    @Query("SELECT * FROM tb_manga_history ORDER BY chapter_title DESC")
    List<MangaHistoryModel> sortByNameDESC();

    @Query("SELECT * FROM tb_manga_history ORDER BY chapter_type ASC")
    List<MangaHistoryModel> sortByTypeASC();

    @Query("SELECT * FROM tb_manga_history ORDER BY chapter_type DESC")
    List<MangaHistoryModel> sortByTypeDESC();

    @Query("SELECT * FROM tb_manga_history WHERE chapter_title LIKE :mangaTitle ORDER BY chapter_title ASC")
    List<MangaHistoryModel> searchByName(String mangaTitle);

    @Query("SELECT * FROM tb_manga_history WHERE chapter_url LIKE :mangaURL")
    MangaHistoryModel findByName(String mangaURL);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistoryData(MangaHistoryModel... mangaBookmarkModel);

    @Query("DELETE FROM tb_manga_history WHERE chapter_url = :mangaURL")
    void deleteHistoryItem(String mangaURL);

    @Query("DELETE FROM tb_manga_history")
    void deleteAllHistoryItem();

    @Delete()
    void deleteAllHistoryItem(MangaHistoryModel... mangaBookmarkModel);
}
