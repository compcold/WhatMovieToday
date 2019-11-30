package com.example.whatmovietoday;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = User.class, version=1, exportSchema = false)
public abstract class UserDB extends RoomDatabase {
    private static UserDB INSTANCE;

    public abstract UserDAO getUserDAO();


    public static UserDB getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), UserDB.class, "tblEntry")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
}