package com.example.studywithme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.history.SessionHistoryActivity;
import com.example.studywithme.ui.timer.TimerActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.ReflectionViewModel;
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MenuActivity1 extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private User user;
    private QuestionnaireViewModel questionnaireViewModel;
    private TimerViewModel timerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
      // initViews();

        //navigation menu item
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.Timer);

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
                        return true;

                    case R.id.History:
                        startActivity(new Intent(getApplicationContext(),
                                SessionHistoryActivity.class));
                        overridePendingTransition(0, 0);
                }
                return false;
            }
        });


    }



}






