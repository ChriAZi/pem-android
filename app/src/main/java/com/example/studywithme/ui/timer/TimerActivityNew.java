package com.example.studywithme.ui.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestActivity;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.StringHelper;
import com.example.studywithme.utils.ToastMaster;

public class TimerActivityNew extends NavigationActivity {

    private TextView timerCategory, timerName, timerPartner, timerCountdown;
    private ProgressBar timer;
    CountDownTimer countdownTimer;
    private TimerViewModel timerViewModel;
    private int layoutId;
    private String sessionId;
    private boolean isOwner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionId = Session.getIdFromPreferences(this);
        if (sessionId == null) {
            layoutId = R.layout.activity_timer_empty;
            super.onCreate(savedInstanceState);
            initEmptyTimerLayout();
        } else {
            layoutId = R.layout.activity_timer_new;
            super.onCreate(savedInstanceState);
            initTimerLayout();
        }
    }

    private void initEmptyTimerLayout() {
        Button startSessionButton = findViewById(R.id.bt_empty_start_session);
        startSessionButton.setOnClickListener(view -> startActivity(new Intent(this, QuestActivity.class)));
    }

    private void initTimerLayout() {
        timerCategory = findViewById(R.id.tv_timer_category);
        timerName = findViewById(R.id.tv_timer_name);
        timerPartner = findViewById(R.id.tv_timer_partner);

        timer = findViewById(R.id.barTimer);
        timerCountdown = findViewById(R.id.tv_countdown);

        // TODO loadTasks

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.getActiveSession(sessionId).observe(this, session -> {
            String userId = User.getIdFromPreferences(this);
            if (session.getPartner() == null) {
                isOwner = true;
                setViewsForOwner(session, false);
            } else if (session.getOwner().getUid().equals(userId)) {
                isOwner = true;
                setViewsForOwner(session, true);
            } else {
                isOwner = false;
                setViewsForPartner(session);
            }
            setTimer(session);
        });
    }

    private void setViewsForOwner(Session session, boolean isPublic) {
        timerCategory.setText(StringHelper.capitalize(session.getOwnerSetting().getCategory().toString()));
        timerName.setText(session.getOwnerSetting().getName());
        if (isPublic) {
            timerPartner.setText(session.getPartner().getName());
        }
    }

    private void setViewsForPartner(Session session) {
        timerCategory.setText(StringHelper.capitalize(session.getPartnerSetting().getCategory().toString()));
        timerName.setText(session.getPartnerSetting().getName());
        timerPartner.setText(session.getOwner().getName());
    }

    private void setTimer(Session session) {
        int durationInMillis = session.getDuration() * 60000;
        timer.setMax(durationInMillis);
        countdownTimer = new CountDownTimer(durationInMillis, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setProgress((int) millisUntilFinished, true);
                timerCountdown.setText("Minutes left: " + (int) millisUntilFinished / 60000);
            }

            @Override
            public void onFinish() {
                timerCountdown.setText("Session finished.");
                ToastMaster.showToast(getApplicationContext(), "Finished");
                // endSession();
            }
        };
        countdownTimer.start();
    }

    private void endSession() {
        timerViewModel.endSession(sessionId).observe(this, finished -> {
            if (finished) {
                startReflectionActivity(isOwner);
            }
        });
    }

    private void startReflectionActivity(boolean owner) {

    }

    @Override
    public int getContentViewId() {
        return layoutId;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_timer;
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_timer);
    }

}
