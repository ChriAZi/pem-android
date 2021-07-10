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

public abstract class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
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

    public abstract int getContentViewId();

    public abstract int getNavigationMenuItemId();

    public abstract String getActionBarTitle();
}

