package com.example.studywithme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionCategory;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.authentication.AuthActivity;
import com.example.studywithme.ui.history.SessionHistoryActivity;
import com.example.studywithme.ui.questionnaire.QuestActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private QuestionnaireViewModel questionnaireViewModel;
    private TimerViewModel timerViewModel;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storeUserInfo();
        initViewModel();
        initViews();

        //////////////////
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation );

        bottomNavigationView.setSelectedItemId(R.id.home);

        //perform ItemSeletecdListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                   switch(item.getItemId()){
                       case R.id.Home:

                           return true;

                       case R.id.Session:
                           startActivity(new Intent(getApplicationContext(),
                                   MenuActivity1.class));
                           overridePendingTransition(0,0);
                           return true;

                       case R.id.History:
                           startActivity(new Intent(getApplicationContext(),
                                   SessionHistoryActivity.class));
                           overridePendingTransition(0,0);
                           return true;
                   }
                   return false;

            }
        });
    }
///////////////////////////////Home item with signOut with username
    private void initViews() {
        TextView textView = findViewById(R.id.tv_username);
        textView.setText("Username: " + user.getName());

        Button signOutButton = findViewById(R.id.bt_sign_out);
        signOutButton.setOnClickListener(view -> firebaseAuth.signOut());
    }

    private void storeUserInfo() {
        user = getUserFromIntent();
        User.setIdInPreferences(user.getUid(), this);
    }

    private User getUserFromIntent() {
        return (User) getIntent().getSerializableExtra(Constants.USER);
    }

    private void initViewModel() {
        questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
    }

    private Session createSession() {
        SessionTask sessionTask1 = new SessionTask("Subtask1", true);
        SessionTask sessionTask2 = new SessionTask("Subtask2", false);
        SessionSetting ownerSetting = new SessionSetting(
                "TestSession",
                "MyGoal",
                new ArrayList<SessionCategory>() {{
                    add(SessionCategory.HOBBY);
                }},
                new ArrayList<SessionTask>() {
                    {
                        add(sessionTask1);
                        add(sessionTask2);
                    }
                });
        return new Session(20, true, null, null, ownerSetting, null);
    }

    private SessionSetting createSessionSetting() {
        SessionTask sessionTask1 = new SessionTask("PartnerTask1", true);
        SessionTask sessionTask2 = new SessionTask("PartnerTask2", false);
        return new SessionSetting(
                "PartnerSession",
                "PartnerGoal",
                new ArrayList<SessionCategory>() {{
                    add(SessionCategory.UNIVERSITY);
                }},
                new ArrayList<SessionTask>() {
                    {
                        add(sessionTask1);
                        add(sessionTask2);
                    }
                });
    }


    // part of Firebase login and register
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startAuthActivity();
        }
    }

    private void startAuthActivity() {
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }
}
