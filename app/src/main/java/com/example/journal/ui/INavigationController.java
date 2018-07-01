package com.example.journal.ui;

import android.support.v4.app.Fragment;

public interface INavigationController {
    void moveToFragment(Fragment fragment);
    void setTitle(String string);
}
