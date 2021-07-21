package com.example.studywithme.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.ui.history.SessionHistoryActivity;
import com.example.studywithme.ui.join.SessionListActivity;
import com.example.studywithme.ui.timer.TimerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Our app main component are  based on Activities, not fragments.
 * The traditional Jetpack setup from the Bottom Navigation does not work.
 * so NavigationActivity is used.
 * What is Bottom Navigation Activity?
 * It is an implementation of material design bottom navigation.
 * Bottom navigation bars make it easy for users to explore and switch between top-level views in a single tap.
 * They should be used when an application has three to five top-level destination.
 */
public abstract class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getActionBarTitle());
        }
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    /**
     * Handles the click of one of the three BottomNavigation items
     *
     * @param item the items that was clicked
     * @return indicating whether the transition was successful
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(this, SessionListActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_timer:
                    startActivity(new Intent(this, TimerActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                case R.id.navigation_history:
                    startActivity(new Intent(this, SessionHistoryActivity.class));
                    overridePendingTransition(0, 0);
                    break;
            }
            finish();
        }, 0);
        return false;
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    /**
     * Highlights the selected item in the navigation bar
     *
     * @param itemId the ID of the selected item
     */
    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    /**
     * Abstract method to be implemented by the child activities to inform the NavigationActivity about the layout to be set
     *
     * @return the resource ID of the relevant layout file
     */
    public abstract int getContentViewId();

    /**
     * Abstract method to be implemented by the child activities to inform the NavigationActivity about which Menu Item to set selected
     *
     * @return the ID of the BottomNavigation item to be set as selected
     */
    public abstract int getNavigationMenuItemId();

    /**
     * Abstract method to be implemented by the child activities to inform the NavigationActivity about the title of the ActionBar
     *
     * @return the title of the actionBar
     */
    public abstract String getActionBarTitle();
}

