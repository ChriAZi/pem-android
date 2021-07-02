package com.example.studywithme.ui.timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestionnaireActivity;
import com.example.studywithme.ui.viewmodels.AbstractViewModel;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.ToastMaster;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerActivity extends NavigationActivity {
    ProgressBar progressBar;
    TextView textTimer, creatorName, partnerName, creatorGoal, partnerGoal, creatorWork, partnerWork, started;
    Button start;
    Button stop;
    EditText et_timer;
    CountDownTimer countdownTimer;
    private int myProgress = 0;
    private int progress;
    private int endTime = 250;
    private String TAG = "TimerActivity";
    private QuestionnaireViewModel questionnaireViewModel;
    private SessionHistoryViewModel sessionHistoryViewModel;
    public SessionListViewModel sessionListViewModel;
    private User user;
    private String session2;
    private Session session;
    private String uID;
    private boolean active = false;
    private Timestamp sessionStart;
    private String sessionId;
    private int layoutId;


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
        getSupportActionBar().setTitle(R.string.heading_timer);
    }

    private void initEmptyTimerLayout() {
        Button startSessionButton = findViewById(R.id.bt_empty_start_session);
        startSessionButton.setOnClickListener(view -> startActivity(new Intent(this, QuestionnaireActivity.class)));
    }

    private void initTimerLayout() {
        creatorName = findViewById(R.id.sessionCreator);
        creatorWork = findViewById(R.id.creatorWork);
        creatorGoal = findViewById(R.id.creatorGoal);

        partnerName = findViewById(R.id.partnerName);
        partnerWork = findViewById(R.id.partnerWork);
        partnerGoal = findViewById(R.id.partnerGoal);


        progressBar = findViewById(R.id.barTimer);
        textTimer = findViewById(R.id.timer_countdown);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        et_timer = findViewById(R.id.session_duration);
        started = findViewById(R.id.started);

        initViewModel();

        start.setOnClickListener(v -> startTimer());

        stop.setOnClickListener(v -> stopTimer());
    }

    private void stopTimer() {
        try {
            countdownTimer.cancel();
            active = false;
            endSession();
            //Timerviewmodel end Session (bool)

        } catch (Exception e) {

        }
        textTimer.setText("Stopped!");
        started.setText("Session stopped!");
    }

    /**
     * sets the session to inactive and therefore ends it
     */
    private void endSession() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session ->{
            session.setActive(false);
        });
    }


    /**
     * observes the current session and sets the variables(creator & partner name, work & goal) to the current values
     */
    private void initViewModel() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session -> {

            textTimer.setText(String.valueOf(session.getDuration()));
            creatorName.setText(session.getOwner().getName());
            creatorWork.setText(session.getOwnerSetting().getCategories().get(0).toString());
            creatorGoal.setText(session.getOwnerSetting().getGoal());
            if (session.getPartner() != null) {
                partnerName.setText(session.getPartner().getName());
                partnerWork.setText(session.getPartnerSetting().getCategories().get(0).toString());
                partnerGoal.setText(session.getPartnerSetting().getGoal());
            }

        });
    }



    /**
     * starts timer from database timer duration
     */
    private void startTimer(String duration) {
        if (duration.length() > 0) {
            myProgress = 0;

            try {
                countdownTimer.cancel();

            } catch (Exception e) {

            }

            progress = 1;
            endTime = Integer.parseInt(duration); // up to finish time
            setActive(); // set the session active
            countdownTimer = new CountDownTimer(60 * endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime * 60);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    String newtime = hours + ":" + minutes + ":" + seconds;

                    //sets the countdown timer depending on length of seconds, minutes and hours
                    if (newtime.equals("0:0:0")) {
                        textTimer.setText("00:00:00");
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        textTimer.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                        textTimer.setText("0" + hours + ":0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        textTimer.setText("0" + hours + ":" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        textTimer.setText(hours + ":0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(hours).length() == 1) {
                        textTimer.setText("0" + hours + ":" + minutes + ":" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        textTimer.setText(hours + ":0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        textTimer.setText(hours + ":" + minutes + ":0" + seconds);
                    } else {
                        textTimer.setText(hours + ":" + minutes + ":" + seconds);
                    }
                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime * 60);
                    textTimer.setText("Finished");
                    // started.setText("Session finished");
                }
            };
            countdownTimer.start();

        } else {
            ToastMaster.showToast(getApplicationContext(), "Please enter your session time");
        }
    }

    /**
     * sets the session active
     */
    private void setActive() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session ->{
            session.setActive(true);
        });
    }


    /**
     * starts countdown & progress bar timer with time given from the input field
     */
    private void startTimer() {
        if (et_timer.getText().toString().length() > 0 || textTimer.getText() != "00:00") {
            myProgress = 0;

            try {
                countdownTimer.cancel();

            } catch (Exception e) {

            }
            String timeInterval = "";
            if (et_timer.getText().toString().length() > 0) {
                timeInterval = et_timer.getText().toString();
            } else {
                timeInterval = textTimer.getText().toString();
            }
            progress = 1;
            endTime = Integer.parseInt(timeInterval); // up to finish time

            countdownTimer = new CountDownTimer(60 * endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime * 60);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    String newtime = hours + ":" + minutes + ":" + seconds;

                    //sets the countdown timer depending on length of seconds, minutes and hours
                    if (newtime.equals("0:0:0")) {
                        textTimer.setText("00:00:00");
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        textTimer.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                        textTimer.setText("0" + hours + ":0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        textTimer.setText("0" + hours + ":" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        textTimer.setText(hours + ":0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(hours).length() == 1) {
                        textTimer.setText("0" + hours + ":" + minutes + ":" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        textTimer.setText(hours + ":0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        textTimer.setText(hours + ":" + minutes + ":0" + seconds);
                    } else {
                        textTimer.setText(hours + ":" + minutes + ":" + seconds);
                    }
                }

                @Override
                public void onFinish() {
                    setProgress(progress, endTime * 60);
                    textTimer.setText("Finished");
                    // started.setText("Session inactive");
                }
            };
            countdownTimer.start();
            active = true;
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmmss");
            String dateString = formatter.format(new Date(tsLong));
            sessionStart = new Timestamp(new Date());

            Log.d("Timestamp", sessionStart.toString());
            started.setText("Session active");
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your session time", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * sets the circular progress bar correctly
     *
     * @param startTime
     * @param endTime
     */
    public void setProgress(int startTime, int endTime) {
        progressBar.setMax(endTime);
        progressBar.setSecondaryProgress(endTime);
        progressBar.setProgress(startTime);

    }

    @Override
    public int getContentViewId() {
        return layoutId;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_timer;
    }

}
