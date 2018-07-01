package com.example.journal.core.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.journal.core.models.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE isLoggedIn = 1")
    User getCurrentUser();

    @Query("UPDATE User SET isLoggedIn = 0")
    void signOutAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUser(User... user);
}
