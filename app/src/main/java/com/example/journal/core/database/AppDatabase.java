package com.example.journal.core.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.journal.core.models.Entry;
import com.example.journal.core.models.User;
import com.example.journal.core.utility.Converters;

@TypeConverters(Converters.class)
@Database(entities = {User.class, Entry.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EntryDao getEntryDao();

    public abstract UserDao getUserDao();

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "application.db";
    private static AppDatabase sInstance;
    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating a new Database Instance");

                sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                                .fallbackToDestructiveMigration()
                                .build();

                return sInstance;
            }
        }

        return sInstance;
    }
}

