package com.example.journal.core.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    public @NonNull String userId;
    public String fullName;
    public String email;
    public boolean isLoggedIn;
}
