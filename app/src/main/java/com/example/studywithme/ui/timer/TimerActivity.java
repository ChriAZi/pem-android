package com.example.studywithme.ui.timer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.ToastMaster;
import com.google.firebase.Timestamp;

public class TimerActivity extends NavigationActivity {
    ProgressBar progressBar;
    TextView textTimer, creatorName, partnerName, creatorGoal, partnerGoal, creatorCategory, partnerCategory, started, sessionNameCreator, sessionNamePartner, subtask1Creator, subtask2Creator, subtask1Partner, subtask2Partner;
    Button start;
    Button stop;
    EditText et_timer;
    CountDownTimer countdownTimer;
    AlertDialog.Builder builder;
    private Timestamp sessionStart;
    private int myProgress = 0;
    private int progress;
    private int endTime = 250;
    private String newtime;
    private String TAG = "TimerActivity";
    private TimerViewModel timerViewModel;
    private TimerViewModel timerViewModel1;
    public SessionListViewModel sessionListViewModel;
    public SessionListViewModel sessionListViewModel1;
    private QuestionnaireViewModel questionnaireViewModel;
    private User user;
    private String session2;
    private Session session;
    private String uID;
    private boolean active = false;
    private String sessionId;
    private int layoutId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionId = Session.getIdFromPreferences(this);
        builder = new AlertDialog.Builder(this);
        getCurrentUser();
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
        Button startSessionButton = findViewById(R.id.bt_empty_start_session);
        startSessionButton.setOnClickListener(view -> startActivity(new Intent(this, QuestActivity.class)));
    }

    private void initTimerLayout() {
        sessionNameCreator = findViewById(R.id.sessionNameCreator);
        creatorName = findViewById(R.id.sessionCreator);
        subtask1Creator = findViewById(R.id.subtaskCreator1);
        subtask2Creator = findViewById(R.id.subtaskCreator2);
        creatorCategory = findViewById(R.id.creatorCategory);
        creatorGoal = findViewById(R.id.creatorGoal);

        sessionNamePartner = findViewById(R.id.sessionNamePartner);
        partnerName = findViewById(R.id.partnerName);
        subtask1Partner = findViewById(R.id.subtaskPartner1);
        subtask2Partner = findViewById(R.id.subtaskPartner2);
        partnerCategory = findViewById(R.id.partnerCategory);
        partnerGoal = findViewById(R.id.partnerGoal);


        progressBar = findViewById(R.id.barTimer);
        textTimer = findViewById(R.id.timer_countdown);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        et_timer = findViewById(R.id.session_duration);
        started = findViewById(R.id.started);

        initViewModel();

        //start.setOnClickListener(v -> startTimer());
        stop.setOnClickListener(v -> stopTimer());
    }

    private void stopTimer() {
        try {
            countdownTimer.cancel();
            active = false;
            endSession();
            partnerGaveUp();

        } catch (Exception e) {

        }
        textTimer.setText("Stopped!");
        started.setText("Session stopped!");
    }

    private void getCurrentUser() {
        sessionListViewModel1 = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel1.getCurrentUser(User.getIdFromPreferences(this)).observe(this, userCurr -> {
            user = userCurr;
        });

    }

    private void partnerGaveUp() {
        timerViewModel1 = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel1.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session -> {
            if (!session.isPublic()) {
                //do nothing
            } else {
                if (session.getOwner().getName().equals(user.getName())) {
                    ToastMaster.showToast(this, "You gave up. I know you can do better.");
                } else {
                    if (session.getPartner().getName() == user.getName() && session.getPartner() != null) {
                        ToastMaster.showToast(this, "Your partner gave up! They owe you a beer now!");
                    }
                }

            }
        });
    }

    /**
     * ends the session via TimerviewModel
     */
    private void endSession() {
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.endSession(Session.getIdFromPreferences(this)).observe(this, finished -> {
            if (finished) {
                Toast.makeText(getApplicationContext(), "Session finished", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * observes the current session and sets the variables(creator & partner name, work & goal) to the current values
     */
    private void initViewModel() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session -> {

            textTimer.setText(String.valueOf(session.getDuration()));
            sessionStart = session.getStartedAt();
            sessionNameCreator.setText(session.getOwnerSetting().getName());
            creatorName.setText(session.getOwner().getName());
            creatorCategory.setText(session.getOwnerSetting().getCategory().toString());
            creatorGoal.setText(session.getOwnerSetting().getGoal());
            subtask1Creator.setText(session.getOwnerSetting().getTasks().get(0).getDescription());
           /* if(session.getOwnerSetting().getTasks().get(1).getDescription() != null){
                subtask2Creator.setText(session.getOwnerSetting().getTasks().get(1).getDescription());
            }else{
                subtask2Creator.setVisibility(View.INVISIBLE);
            } */
            if (session.getPartner() != null) {
                setLayoutParams();
                partnerName.setText(session.getPartner().getName());
                partnerCategory.setText(session.getPartnerSetting().getCategory().toString());
                subtask1Partner.setText(session.getPartnerSetting().getTasks().get(0).getDescription());
               /* if(session.getPartnerSetting().getTasks().get(1).getDescription() !=null) {
                    subtask2Partner.setText(session.getPartnerSetting().getTasks().get(1).getDescription());
                }else{
                    subtask2Partner.setVisibility(View.INVISIBLE);
                }*/
                partnerGoal.setText(session.getPartnerSetting().getGoal());

            } else {
                partnerName.setVisibility(View.INVISIBLE);
                partnerCategory.setVisibility(View.INVISIBLE);
                partnerGoal.setVisibility(View.INVISIBLE);
                subtask1Partner.setVisibility(View.INVISIBLE);
                subtask2Partner.setVisibility(View.INVISIBLE);
                sessionNamePartner.setVisibility(View.INVISIBLE);
                creatorName.setGravity(Gravity.CENTER);
                creatorCategory.setGravity(Gravity.CENTER);
                creatorGoal.setGravity(Gravity.CENTER);
                sessionNameCreator.setGravity(Gravity.CENTER);
                subtask1Creator.setGravity(Gravity.CENTER);
                subtask2Creator.setGravity(Gravity.CENTER);

            }
            startTimer();

        });
    }

    private void setLayoutParams() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        ConstraintLayout.LayoutParams params1 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        ConstraintLayout.LayoutParams params2 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        ConstraintLayout.LayoutParams params3 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        ConstraintLayout.LayoutParams params4 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        ConstraintLayout.LayoutParams params5 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(200, 180, 0, 0);
        sessionNameCreator.setLayoutParams(params);
        params1.setMargins(200, 280, 0, 0);
        creatorGoal.setLayoutParams(params1);
        params2.setMargins(200, 380, 0, 0);
        creatorCategory.setLayoutParams(params2);
        params3.setMargins(200, 480, 0, 0);
        subtask1Creator.setLayoutParams(params3);
        if (subtask2Creator != null) {
            params4.setMargins(200, 580, 0, 0);
            subtask2Creator.setLayoutParams(params4);
        }
        params5.setMargins(200, 680, 0, 0);
        creatorName.setLayoutParams(params5);
    }


    /**
     * sets the session active
     */
    private void setActive() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session -> {
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
                    newtime = hours + ":" + minutes + ":" + seconds;

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
                    textTimer.setText("Great, session finished successfully!");
                    countdownTimer.cancel();
                    endSession();
                }
            };
            countdownTimer.start();
            active = true;
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

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_timer);
    }

}
