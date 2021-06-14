package com.example.studywithme.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.studywithme.ui.viewmodels.SessionCreationViewModel;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.ToastMaster;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SessionHistoryViewModel sessionHistoryViewModel;
    private SessionCreationViewModel sessionCreationViewModel;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storeUserInfo();
        initViewModel();
        initViews();
    }

    private void storeUserInfo() {
        user = getUserFromIntent();
        User.setIdInPreferences(user.getUid(), this);
    }

    private void initViewModel() {
        sessionCreationViewModel = new ViewModelProvider(this).get(SessionCreationViewModel.class);
    }

    private void initViews() {
        TextView textView = findViewById(R.id.tv_username);
        textView.setText(user.getName());

        Button createSessionButton = findViewById(R.id.bt_create_session);
        Session session = createSession();
        createSessionButton.setOnClickListener(view -> {
            sessionCreationViewModel.createSession(user.getUid(), session);
            startViewModelObservation();
        });

        Button sessionHistoryButton = findViewById(R.id.bt_history);
        sessionHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SessionHistoryActivity.class);
            startActivity(intent);
        });

        MaterialButton signOutButton = findViewById(R.id.bt_sign_out);
        signOutButton.setOnClickListener(view -> firebaseAuth.signOut());
    }

    private User getUserFromIntent() {
        return (User) getIntent().getSerializableExtra(Constants.USER);
    }

    private Session createSession() {
        SessionTask sessionTask1 = new SessionTask("Subtask1", true);
        SessionTask sessionTask2 = new SessionTask("Subtask2", false);
        SessionSetting ownerSetting = new SessionSetting(
                "TestSession",
                "TestGoal",
                new ArrayList<SessionCategory>() {{
                    add(SessionCategory.HOBBY);
                }},
                new ArrayList<SessionTask>() {
                    {
                        add(sessionTask1);
                        add(sessionTask2);
                    }
                });
        return new Session(20, new Timestamp(new Date()), false, null, null, ownerSetting, null);
    }

    private void startViewModelObservation() {
        sessionCreationViewModel.getCurrentSession().observe(this, session -> {
            TextView testView = findViewById(R.id.tv_session_id);
            String sessionId = session.getUid();
            testView.setText(sessionId);
        });
        sessionCreationViewModel.getCurrentUser(User.getIdFromPreferences(this)).observe(this, user -> {
            ToastMaster.showToast(this, "Current User: " + user.getName());
        });
    }

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