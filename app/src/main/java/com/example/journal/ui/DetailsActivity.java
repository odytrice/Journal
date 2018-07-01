package com.example.journal.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.journal.R;

public class DetailsActivity extends AppCompatActivity implements INavigationController{

    public static final String EXTRA_ENTRYID = "extra_entry";

    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        String action = getIntent().getAction();

        if(action == null) action = ACTIONS.CREATE;


        _toolbar = findViewById(R.id.toolbar);
        _toolbar.setNavigationOnClickListener((View v) -> onBackPressed());


        int entryId = getIntent().getIntExtra(EXTRA_ENTRYID,0);

        if(ACTIONS.CREATE.equals(action)||ACTIONS.EDIT.equals(action)){
            moveToFragment(CreateFragment.newInstance(entryId));
        }
        else if(ACTIONS.VIEW.equals(action)){
            moveToFragment(DetailsFragment.newInstance(entryId));
        }
        else{
            Toast.makeText(this,"Invalid Action Recieved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void moveToFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, fragment, "")
                        .commit();
    }

    @Override
    public void setTitle(String title) {
        _toolbar.setTitle(title);
    }

    public static class ACTIONS {
        public static final String CREATE = "action_create";
        public static final String VIEW = "action_view";
        public static final String EDIT = "action_edit";
    }
}
