package com.example.journal.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.journal.core.database.EntryDao;
import com.example.journal.core.database.UserDao;
import com.example.journal.core.interfaces.IBackendService;
import com.example.journal.core.models.Entry;
import com.example.journal.core.models.User;
import com.example.journal.core.utility.AppExecutors;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final IBackendService _server;

    private EntryDao _entry;
    private UserDao _user;

    private MutableLiveData<User> _liveUser = new MutableLiveData<>();
    private MutableLiveData<Entry> _liveEntry = new MutableLiveData<>();
    private MutableLiveData<List<Entry>> _liveEntries = new MutableLiveData<>();

    DetailsViewModel(UserDao user, EntryDao entry, IBackendService server) {
        _user = user;
        _entry = entry;
        _server = server;
    }

    public LiveData<User> getUser() {
        return _liveUser;
    }

    public void fetchUser(){
        AppExecutors.getInstance().diskIO().execute(() -> {
            _liveUser.postValue(_user.getCurrentUser());
        });
    }

    public LiveData<List<Entry>> getEntries() {
        return _liveEntries;
    }

    public void fetchEntries(String userId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Entry> entries = _entry.getEntries(userId);
            _liveEntries.postValue(entries);
        });
    }

    public void signIn(AuthCredential credential) {
        _server.authenticate(credential, user -> {
            if (user != null) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    _user.signOutAllUsers();
                    _user.saveUser(user);
                });
            }
            _liveUser.postValue(user);
        });
    }

    public void addEntry(Entry entry) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            _entry.InserEntries(entry);
            List<Entry> entries = _entry.getEntries(entry.userId);
            _server.saveEntries(entry.userId, entries);
            _liveEntries.postValue(entries);
        });
    }

    public void deleteEntry(Entry entry){
        AppExecutors.getInstance().diskIO().execute(() -> {
            _entry.DeleteEntry(entry);
            List<Entry> entries = _entry.getEntries(entry.userId);
            _server.saveEntries(entry.userId, entries);
            _liveEntries.postValue(entries);
        });

    }

    public LiveData<Entry> getEntry(int entryId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Entry entry = _entry.getEntry(entryId);
            _liveEntry.postValue(entry);
        });
        return _liveEntry;
    }
}
