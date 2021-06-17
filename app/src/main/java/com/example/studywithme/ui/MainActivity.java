package com.example.studywithme.ui;

import android.annotation.SuppressLint;
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
import com.example.studywithme.ui.timer.TimerActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.ToastMaster;
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

    @SuppressLint("SetTextI18n")
    private void initViews() {
        TextView textView = findViewById(R.id.tv_username);
        textView.setText("Username: " + user.getName());

        Button createPublicSession = findViewById(R.id.bt_create_public_session);
        createPublicSession.setOnClickListener(view -> {
            Session publicSession = createSession();
            questionnaireViewModel.startSession(user.getUid(), publicSession).observe(this, sessionId -> {
                if (sessionId != null) {
                    Session.setIdInPreferences(this, sessionId);
                    ToastMaster.showToast(this, "Session was created with id: " + sessionId);
                }
            });
        });

        Button joinLastSession = findViewById(R.id.bt_join_last_session);
        joinLastSession.setOnClickListener(view -> {
            SessionSetting sessionSetting = createSessionSetting();
            questionnaireViewModel.joinSession(Session.getIdFromPreferences(this), User.getIdFromPreferences(this), sessionSetting).observe(this, joined -> {
                if (joined) {
                    ToastMaster.showToast(this, "Joined Session");
                }
            });
        });

        Button startTimerButton = findViewById(R.id.bt_start_timer);
        startTimerButton.setOnClickListener(view -> {
            startTimerActivity();
        });

        Button endSessionButton = findViewById(R.id.bt_end_session);
        endSessionButton.setOnClickListener(view -> {
            timerViewModel.endSession(Session.getIdFromPreferences(this)).observe(this, finished -> {
                if (finished) {
                    ToastMaster.showToast(this, "Session finished!");
                }
            });
        });

        Button sessionHistoryButton = findViewById(R.id.bt_history);
        sessionHistoryButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SessionHistoryActivity.class);
            startActivity(intent);
        });

        Button signOutButton = findViewById(R.id.bt_sign_out);
        signOutButton.setOnClickListener(view -> firebaseAuth.signOut());
    }

    private void startTimerActivity() {
        Intent i = new Intent(MainActivity.this, TimerActivity.class);
        i.putExtra(Constants.USER, user);
        i.putExtra(Constants.SESSION_ID, Session.getIdFromPreferences(this));
        MainActivity.this.startActivity(i);
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
