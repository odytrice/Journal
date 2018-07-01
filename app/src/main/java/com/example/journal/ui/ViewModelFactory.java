package com.example.journal.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.journal.core.database.AppDatabase;
import com.example.journal.core.services.FireBaseService;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context _context;

    ViewModelFactory(Context context){
        _context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            AppDatabase database = AppDatabase.getInstance(_context);
            ViewModel model = new MainViewModel(database.getUserDao(),database.getEntryDao(), new FireBaseService());
            //noinspection unchecked
            return (T) model;
        }

        if(modelClass.isAssignableFrom(DetailsViewModel.class)){
            AppDatabase database = AppDatabase.getInstance(_context);
            ViewModel model = new DetailsViewModel(database.getUserDao(),database.getEntryDao(), new FireBaseService());
            //noinspection unchecked
            return (T) model;
        }
        return null;
    }
}
