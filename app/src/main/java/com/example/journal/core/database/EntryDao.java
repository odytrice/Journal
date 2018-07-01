package com.example.journal.core.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.journal.core.models.Entry;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM Entry WHERE userId = :userId")
    List<Entry> getEntries(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InserEntries(Entry... entries);

    @Delete
    int DeleteEntry(Entry entry);

    @Query("SELECT * FROM Entry WHERE entryId = :entryId")
    Entry getEntry(int entryId);
}
