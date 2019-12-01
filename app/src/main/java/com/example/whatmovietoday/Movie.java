package com.example.whatmovietoday;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Used to keep track of the movie ID's saved
 * by users
 */

@Entity(tableName = "tblSavedMovie")
public class Movie {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int uid;

    @NonNull
    public int id;

    @NonNull
    public int userId;
}
