package com.example.whatmovietoday;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM tblSavedMovie")
    List<Movie> getAll();

    @Query("SELECT * FROM tblSavedMovie WHERE userId = :userID")
    List<Movie> getUserSaves(int userID);

    @Query("DELETE FROM tblSavedMovie")
    void nukeTable();
}
