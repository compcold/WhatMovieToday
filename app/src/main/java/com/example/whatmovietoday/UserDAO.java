package com.example.whatmovietoday;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;


@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM tblUser")
    List<User> getAll();

    @Query("SELECT id FROM tblUser WHERE username= :username")
    int getId(String username);

    @Query("DELETE FROM tblUser")
    void nukeTable();

    @Query("UPDATE tblUser SET lastLogin = 0 WHERE username =:username")
    void removeLogin(String username);

    @Query("UPDATE tblUser SET lastLogin = 1 WHERE username =:username")
    void setLogin(String username);
}
