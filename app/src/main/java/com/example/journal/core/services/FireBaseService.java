package com.example.journal.core.services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.journal.core.interfaces.IBackendService;
import com.example.journal.core.models.Entry;
import com.example.journal.core.models.User;
import com.example.journal.core.utility.AppExecutors;
import com.example.journal.core.utility.Consumer;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FireBaseService implements IBackendService {

    private static final String TAG = FireBaseService.class.getSimpleName();

    public void authenticate(AuthCredential credential, Consumer<User> consumer) {

        final MutableLiveData<User> liveUser = new MutableLiveData<>();

        final FirebaseAuth _auth = FirebaseAuth.getInstance();
        _auth.signInWithCredential(credential)
                .addOnCompleteListener(AppExecutors.getInstance().diskIO(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser fireUser = _auth.getCurrentUser();
                        if (fireUser != null) {
                            User user = new User();
                            user.userId = fireUser.getUid();
                            user.email = fireUser.getEmail();
                            user.isLoggedIn = true;
                            user.fullName = fireUser.getDisplayName();
                            consumer.accept(user);
                        } else {
                            consumer.accept(null);
                        }
                    } else {
                        consumer.accept(null);
                    }
                });
    }

    @Override
    public void saveEntries(String userId, List<Entry> entries) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference entriesRef = database.getReference("entries");
        entriesRef.child(userId).setValue(entries);
    }
}
