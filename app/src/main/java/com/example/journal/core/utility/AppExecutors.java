package com.example.journal.core.utility;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.journal.core.database.AppDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final String TAG = AppExecutors.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor _mainThread;
    private final Executor _diskIO;
    private final Executor _networkIO;

    private AppExecutors(Executor mainThread, Executor diskIO, Executor networkIO){
        _mainThread = mainThread;
        _diskIO = diskIO;
        _networkIO = networkIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating a new Database Instance");

                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),Executors.newFixedThreadPool(3), new MainThreadExecutor());

                return sInstance;
            }
        }

        return sInstance;
    }

    public Executor mainThread() {
        return _mainThread;
    }

    public Executor diskIO() {
        return _diskIO;
    }

    public Executor networkIO() {
        return _networkIO;
    }

    private static class MainThreadExecutor implements Executor{

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
