package com.example.myapplication.localstorages.manga_local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.localstorages.manga_local.MangaBookmarkModel;

import java.util.List;

@Dao
public interface MangaBookmarkDAO {
    @Query("SELECT * FROM tb_manga_bookmark ORDER BY manga_added_date DESC")
    List<MangaBookmarkModel> getMangaBookmarkData();

    @Query("SELECT * FROM tb_manga_bookmark ORDER BY manga_added_date ASC")
    List<MangaBookmarkModel> sortByDateASC();

    @Query("SELECT * FROM tb_manga_bookmark ORDER BY manga_title ASC")
    List<MangaBookmarkModel> sortByNameASC();

    @Query("SELECT * FROM tb_manga_bookmark ORDER BY manga_title DESC")
    List<MangaBookmarkModel> sortByNameDESC();

    @Query("SELECT * FROM tb_manga_bookmark ORDER BY manga_type ASC")
    List<MangaBookmarkModel> sortByTypeASC();

    @Query("SELECT * FROM tb_manga_bookmark ORDER BY manga_type DESC")
    List<MangaBookmarkModel> sortByTypeDESC();

    @Query("SELECT * FROM tb_manga_bookmark WHERE manga_title LIKE :mangaTitle ORDER BY manga_title ASC")
    List<MangaBookmarkModel> searchByName(String mangaTitle);

    @Query("SELECT * FROM tb_manga_bookmark WHERE manga_url LIKE :mangaURL")
    MangaBookmarkModel findByName(String mangaURL);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmarkData(MangaBookmarkModel... mangaBookmarkModel);

    @Query("DELETE FROM tb_manga_bookmark WHERE manga_url = :mangaURL")
    void deleteBookmarkItem(String mangaURL);

    @Query("DELETE FROM tb_manga_bookmark")
    void deleteAllBookmarkItem();

    @Delete()
    void deleteAllBookmarkItem(MangaBookmarkModel... mangaBookmarkModel);
}
