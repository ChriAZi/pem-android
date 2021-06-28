package com.example.studywithme.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.MainActivity;
import com.example.studywithme.ui.MenuActivity1;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SessionHistoryActivity extends AppCompatActivity {
    private SessionHistoryViewModel sessionHistoryViewModel;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_list);
        recyclerView = findViewById(R.id.rv_session_list);

        initViewModel();

        //navigation menu item
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.History);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.Timer:
                        startActivity(new Intent(getApplicationContext(),
                                MenuActivity1.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.History:

                        return  true;
                }
                return false;
            }
        });

    }

    private void initViewModel() {
        String userId = User.getIdFromPreferences(this);
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getPastSessions(userId).observe(this, sessions -> {
            SessionHistoryAdapter adapter = new SessionHistoryAdapter(sessions);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(adapter);
        });


    }
}
