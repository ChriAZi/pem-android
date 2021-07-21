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

/**
 * Activity that handles the Timer Functionality, extends the Navigation
 * and implements the Adapter for the Task List
 */
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

    /**
     * When the activity is created, this method checks whether the user has already started
     * a session or not and accordingly loads the right layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionId = Session.getIdFromPreferences(this);
        if (sessionId == null) {
            layoutId = R.layout.activity_timer_empty;
            super.onCreate(savedInstanceState);
            initEmptyTimerLayout(); //load empty timer layout
        } else {
            layoutId = R.layout.activity_timer;
            super.onCreate(savedInstanceState);
            initTimerLayout(); //load actual timer layout
        }
    }

    /**
     * loads the empty timer layout and enables a Clicklistener for a Floatingaction Button
     * by which a session can be started
     */
    private void initEmptyTimerLayout() {
        ExtendedFloatingActionButton startSessionButton = findViewById(R.id.bt_timer_create_session);
        startSessionButton.setOnClickListener(view -> startActivity(new Intent(this, QuestActivity.class)));
    }

    /**
     * loads the actual timer layout and observes the current session with the TimerViewModel class
     */
    private void initTimerLayout() {
        timerCategory = findViewById(R.id.tv_timer_category);
        timerName = findViewById(R.id.tv_timer_name);
        timerPartner = findViewById(R.id.tv_timer_partner);
        progressBar = findViewById(R.id.barTimer);
        timerCountdown = findViewById(R.id.tv_countdown);
        tasksRecyclerView = findViewById(R.id.rv_timer_tasks);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.getActiveSession(sessionId).observe(this, session -> {   //observes the active session
            String userId = User.getIdFromPreferences(this);    //get the current user ID
            if (!session.isPublic()) {  //check if the session is private
                if (session.getPartner() == null) {     //check if there is a session partner
                    setTimerForOwner(session, false);   //set timer for owner without a partner
                } else if (session.getOwner().getUid().equals(userId)) {  //check if the current user is the owner
                    setTimerForOwner(session, true);    //set timer for owner with a partner
                } else {
                    setTimerForPartner(session);    //set timer for the partner
                }
                } else if (session.getHasPartner()) {  // if the session has a partner
                if (session.getOwner().getUid().equals(userId)) {   //check if the current user is the owner
                    setTimerForOwner(session, true);    //set timer for owner with a partner
                }
            } else {
                setTimerForOwner(session, false);   //set timer for owner without a partner
            }
        });
    }

    /**
     * sets the views and the timer for the person who created the session with the initial duration
     * @param session   current session
     * @param hasPartner    session with or without partner
     */
    private void setTimerForOwner(Session session, boolean hasPartner) {
        setViewsForOwner(session, hasPartner);
        if (!timerStarted) {
            setTimer(session.getDuration());
            tasks = session.getOwnerSetting().getTasks();
            setTasksRecyclerView(tasks);
            timerStarted = true;
        }
    }

    /**
     * sets the views and the timer with the session's remaining duration for the the partner
     * @param session current session
     */
    private void setTimerForPartner(Session session) {
        setViewsForPartner(session);
        if (!timerStarted) {
            setTimer(getRemainingDuration(session));
            tasks = session.getPartnerSetting().getTasks();
            setTasksRecyclerView(tasks);
            timerStarted = true;
        }
    }

    /**
     * Puts the fetched tasks into the recycler view via the adapter
     * @param tasks
     */
    private void setTasksRecyclerView(List<SessionTask> tasks) {
        TimerTaskAdapter sessionDetailTaskAdapter = new TimerTaskAdapter(tasks, this);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(sessionDetailTaskAdapter);
    }

    /**
     * calculates the remaining session duration for the joining partner
     * @param session
     * @return
     */
    private int getRemainingDuration(Session session) {
        long currentTimeInMillis = System.currentTimeMillis(); //gets the current system time
        int sessionDurationInMinutes = session.getDuration(); //gets the session duration in minutes
        int sessionDurationInMillis = sessionDurationInMinutes * 60000; //gets the session duration in milliseconds

        long startedAtInSeconds = session.getStartedAt().getSeconds(); //gets the seconds of the timestamp at which the session has started
        long passedTime = currentTimeInMillis - startedAtInSeconds * 1000; //calculates the passed time
        long timeLeftInMillis = sessionDurationInMillis - passedTime;   //calculates the left time
        return (int) TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis);
    }

    /**
     * sets the text views like the category, user name and the partner name if there is a partner
     * @param session
     * @param hasPartner
     */
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

    /**
     * sets the text views for the partner (Category, own user name, owner name)
     * @param session
     */
    private void setViewsForPartner(Session session) {
        timerCategory.setText(StringHelper.capitalize(session.getPartnerSetting().getCategory().toString()));
        timerName.setText(session.getPartnerSetting().getName());
        timerPartner.setText(session.getOwner().getName());
    }

    /**
     * sets the progress bar and the countdown timer to the given duration
     * @param duration
     */
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

    /**
     * sets the text for the countdown timer
     * @param millisUntilFinished
     * @return
     */
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

    /**
     * ends the session (also in firebase) and calls the reflection activity
     */
    private void endSession() {
        timerViewModel.endSession(sessionId).observe(this, finished -> {
            if (finished) {
                startReflectionActivity();
            }
        });
    }

    /**
     * starts the reflection activity (called after the timer has finished)
     */
    private void startReflectionActivity() {
        Intent intent = new Intent(TimerActivity.this, ReflectionQuestActivity.class);
        startActivity(intent);
    }

    /**
     * updates the tasks to "done" in the database if the user checks the checkboxes
     * @param position
     * @param checked
     */
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
