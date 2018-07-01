package com.example.journal.core.interfaces;

import com.example.journal.core.models.Entry;
import com.example.journal.core.models.User;
import com.example.journal.core.utility.Consumer;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public interface IBackendService {
    void authenticate(AuthCredential credential, Consumer<User> consumer);
    void saveEntries(String userId, List<Entry> entries);
}
