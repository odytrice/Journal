package com.example.journal.core.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Entry {
    @PrimaryKey(autoGenerate = true)
    public int entryId;
    public String title;
    public String text;
    public Date createdDate;
    public String userId;
}
