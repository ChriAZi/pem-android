package com.example.studywithme.ui.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestActivity;
import com.example.studywithme.ui.reflection.ReflectionQuestActivity;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.StringHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

public class TimerActivity extends NavigationActivity implements TimerTaskAdapter.ItemViewHolder.OnCheckedChangeListener {

    private TextView timerCategory, timerName, timerPartner, timerCountdown;
    private ProgressBar progressBar;
    private CountDownTimer countdownTimer;
    private RecyclerView tasksRecyclerView;
    private TimerViewModel timerViewModel;
    private int layoutId;
    private String sessionId;
    private List<SessionTask> tasks;
    private boolean timerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionId = Session.getIdFromPreferences(this);
        if (sessionId == null) {
            layoutId = R.layout.activity_timer_empty;
            super.onCreate(savedInstanceState);
            initEmptyTimerLayout();
        } else {
            layoutId = R.layout.activity_timer;
            super.onCreate(savedInstanceState);
            initTimerLayout();
        }
    }

    private void initEmptyTimerLayout() {
        ExtendedFloatingActionButton startSessionButton = findViewById(R.id.bt_timer_create_session);
        startSessionButton.setOnClickListener(view -> startActivity(new Intent(this, QuestActivity.class)));
    }

    private void initTimerLayout() {
        timerCategory = findViewById(R.id.tv_timer_category);
        timerName = findViewById(R.id.tv_timer_name);
        timerPartner = findViewById(R.id.tv_timer_partner);
        progressBar = findViewById(R.id.barTimer);
        timerCountdown = findViewById(R.id.tv_countdown);
        tasksRecyclerView = findViewById(R.id.rv_timer_tasks);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.getActiveSession(sessionId).observe(this, session -> {
            String userId = User.getIdFromPreferences(this);
            if (!session.isPublic()) {
                if (session.getPartner() == null) {
                    setTimerForOwner(session, false);
                } else if (session.getOwner().getUid().equals(userId)) {
                    setTimerForOwner(session, true);
                } else {
                    setTimerForPartner(session);
                }
            } else if (session.getHasPartner()) {
                if (session.getOwner().getUid().equals(userId)) {
                    setTimerForOwner(session, true);
                }
            } else {
                setTimerForOwner(session, false);
            }
        });
    }

    private void setTimerForOwner(Session session, boolean hasPartner) {
        setViewsForOwner(session, hasPartner);
        if (!timerStarted) {
            setTimer(session.getDuration());
            tasks = session.getOwnerSetting().getTasks();
            setTasksRecyclerView(tasks);
            timerStarted = true;
        }
    }

    private void setTimerForPartner(Session session) {
        setViewsForPartner(session);
        if (!timerStarted) {
            setTimer(getRemainingDuration(session));
            tasks = session.getPartnerSetting().getTasks();
            setTasksRecyclerView(tasks);
            timerStarted = true;
        }
    }

    private void setTasksRecyclerView(List<SessionTask> tasks) {
        TimerTaskAdapter sessionDetailTaskAdapter = new TimerTaskAdapter(tasks, this);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(sessionDetailTaskAdapter);
    }

    private int getRemainingDuration(Session session) {
        long currentTimeInMillis = System.currentTimeMillis();
        int sessionDurationInMinutes = session.getDuration();
        int sessionDurationInMillis = sessionDurationInMinutes * 60000;

        long startedAtInSeconds = session.getStartedAt().getSeconds();
        long passedTime = currentTimeInMillis - startedAtInSeconds * 1000;
        long timeLeftInMillis = sessionDurationInMillis - passedTime;
        return (int) TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis);
    }

    private void setViewsForOwner(Session session, boolean hasPartner) {
        timerCategory.setText(StringHelper.capitalize(session.getOwnerSetting().getCategory().toString()));
        timerName.setText(session.getOwnerSetting().getName());
        if (hasPartner) {
            User partner = session.getPartner();
            if (partner == null) {
                timerPartner.setText("Your partner will join soon");
            } else {
                timerPartner.setText(partner.getName());
            }
        } else {
            timerPartner.setText(R.string.private_session);
        }
    }

    private void setViewsForPartner(Session session) {
        timerCategory.setText(StringHelper.capitalize(session.getPartnerSetting().getCategory().toString()));
        timerName.setText(session.getPartnerSetting().getName());
        timerPartner.setText(session.getOwner().getName());
    }

    private void setTimer(int duration) {
        int durationInMillis = duration * 60000;
        progressBar.setMax(durationInMillis);
        countdownTimer = new CountDownTimer(durationInMillis, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) ((int) durationInMillis - millisUntilFinished));
                timerCountdown.setText(getTimeAsText(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(durationInMillis);
                timerCountdown.setText("00:00");
                endSession();
            }
        };
        countdownTimer.start();
    }

    private String getTimeAsText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
        int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
        String updatedTime = hours + ":" + minutes + ":" + seconds;

        if (updatedTime.equals("0:0:0")) {
            return "00:00:00";
        } else if ((valueOf(hours).length() == 1) && (valueOf(minutes).length() == 1) && (valueOf(seconds).length() == 1)) {
            return "0" + hours + ":0" + minutes + ":0" + seconds;
        } else if ((valueOf(hours).length() == 1) && (valueOf(minutes).length() == 1)) {
            return "0" + hours + ":0" + minutes + ":" + seconds;
        } else if ((valueOf(hours).length() == 1) && (valueOf(seconds).length() == 1)) {
            return "0" + hours + ":" + minutes + ":0" + seconds;
        } else if ((valueOf(minutes).length() == 1) && (valueOf(seconds).length() == 1)) {
            return hours + ":0" + minutes + ":0" + seconds;
        } else if (valueOf(hours).length() == 1) {
            return "0" + hours + ":" + minutes + ":" + seconds;
        } else if (valueOf(minutes).length() == 1) {
            return hours + ":0" + minutes + ":" + seconds;
        } else if (valueOf(seconds).length() == 1) {
            return hours + ":" + minutes + ":0" + seconds;
        } else {
            return hours + ":" + minutes + ":" + seconds;
        }
    }

    private void endSession() {
        timerViewModel.endSession(sessionId).observe(this, finished -> {
            if (finished) {
                startReflectionActivity();
            }
        });
    }

    private void startReflectionActivity() {
        Intent intent = new Intent(TimerActivity.this, ReflectionQuestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCheckedChange(int position, boolean checked) {
        tasks.get(position).setDone(checked);
        timerViewModel.updateTasks(sessionId, User.getIdFromPreferences(this), tasks);
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
