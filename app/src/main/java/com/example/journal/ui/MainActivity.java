package com.example.journal.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.journal.R;
import com.example.journal.core.models.Entry;
import com.example.journal.core.models.User;
import com.example.journal.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 100;

    private GoogleSignInClient _googleApiClient;
    private ActivityMainBinding _binding;
    private MainViewModel _model;
    private User _user;
    EntryAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        _model = ViewModelProviders.of(this, new ViewModelFactory(getApplicationContext())).get(MainViewModel.class);

        setupAuthentication();
        setupUI();
    }

    private void setupAuthentication() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        _googleApiClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            signIn(account);
        }
    }

    private void setupUI() {
        //Setup Fab
        _binding.fab.setOnClickListener(view -> {

            if (_user == null) {
                performSignIn();
            } else {
                Intent intent = new Intent(this, DetailsActivity.class);
                //intent.putExtra("action",DetailsActivity.ACTIONS.CREATE);
                intent.setAction(DetailsActivity.ACTIONS.CREATE);
                startActivity(intent);
            }


        });

        _binding.toolbar.inflateMenu(R.menu.menu_main);
        _binding.toolbar.setOnMenuItemClickListener(item -> {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_sign_in) {
                performSignIn();
                return true;
            }

            if (id == R.id.action_sign_out) {
                performSignOut();
                return true;
            }
            return false;
        });

        // setup model
        _model.getUser().observe(this, this::showUser);

        setupRecyclerView();

        _model.getEntries().observe(this, _adapter::set_entries);
    }

    private void setupRecyclerView() {
        _adapter = new EntryAdapter();

        _adapter.onEntrySelected(entry -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.setAction(DetailsActivity.ACTIONS.VIEW);
            intent.putExtra(DetailsActivity.EXTRA_ENTRYID, entry.entryId);
            startActivity(intent);
        });

        _binding.rvEntries.setAdapter(_adapter);
        _binding.rvEntries.setLayoutManager(new LinearLayoutManager(this));


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                Entry entry = _adapter.getEntry(position);
                _adapter.removeItem(position);
                _model.deleteEntry(entry);
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(_binding.rvEntries);
    }

    private void performSignIn() {
        Intent intent = _googleApiClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void performSignOut() {
        _googleApiClient.signOut().addOnCompleteListener(this, task -> {
            //Swap Menu Visibility
            _user = null;
            showUser(null);
            _adapter.set_entries(new ArrayList<>());
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(_user != null) {
            _model.fetchEntries(_user.userId);
        }
    }

    private void showUser(User user) {
        if (user != null) {
            _binding.toolbar.getMenu().findItem(R.id.action_sign_in).setVisible(false);
            _binding.toolbar.getMenu().findItem(R.id.action_sign_out).setVisible(true);
            _user = user;
            _model.fetchEntries(user.userId);
        } else {
            _binding.toolbar.getMenu().findItem(R.id.action_sign_out).setVisible(false);
            _binding.toolbar.getMenu().findItem(R.id.action_sign_in).setVisible(true);
            _user = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            onGoogleSignIn(data);
        }
    }

    private void onGoogleSignIn(Intent data) {
        try {
            Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            //SignInWithFireBase
            signIn(acct);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            showUser(null);
        }
    }

    private void signIn(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), getString(R.string.default_web_client_id));
        _model.signIn(credential);
    }
}
