package com.example.whatmovietoday;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Movie.class}, version=2, exportSchema = false)
public abstract class UserDB extends RoomDatabase {
    private static UserDB INSTANCE;
    public abstract UserDAO getUserDAO();
    public abstract MovieDAO getMovieDAO();


    public static UserDB getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), UserDB.class, "WhatMovieDatabase")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    static final Migration migrate = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //database.execSQL("ALTER TABLE tblUsers ADD COLUMN nickName String");
        }
    };

}