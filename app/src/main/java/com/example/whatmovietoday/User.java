package com.example.whatmovietoday;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * For the scope of this project, usernames and passwords
 * are stored in plaintext.
 */

@Entity(tableName = "tblUser")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "username")
    @NonNull
    public String username;

    //Salt + Hash
    @ColumnInfo(name = "password")
    @NonNull
    public String password;

    @ColumnInfo(name = "lastLogin")
    @NonNull
    public boolean lastLogin;
}
