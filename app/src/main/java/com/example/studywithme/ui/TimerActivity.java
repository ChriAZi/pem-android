package com.example.studywithme.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.SessionCreationViewModel;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.example.studywithme.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.studywithme.data.models.User.getIdFromPreferences;

public class TimerActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textTimer,creatorName,partnerName,creatorGoal,partnerGoal,creatorWork,partnerWork;
  //  FirebaseFirestore db;
  //  FirebaseAuth fAuth;
  //  String userID;
    Button start;
    Button stop;
    EditText et_timer;
    CountDownTimer countdownTimer;
    int myProgress = 0;
    int progress;
    int endTime = 250;
    private String TAG = "TimerActivity";
    private SessionCreationViewModel sessionCreationViewModel;
    private SessionHistoryViewModel sessionHistoryViewModel;
    private User user;
    private String session;
    private String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        creatorName = findViewById(R.id.sessionCreator);
        creatorWork = findViewById(R.id.creatorWork);
        creatorGoal = findViewById(R.id.creatorWork);

        partnerName = findViewById(R.id.partnerName);
        partnerWork = findViewById(R.id.partnerWork);
        partnerGoal = findViewById(R.id.partnerGoal);


        progressBar = findViewById(R.id.barTimer);
        textTimer = findViewById(R.id.timer_countdown);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        et_timer = findViewById(R.id.session_duration);

        getCurrentUser();
        initViewModels();
      //  initViewModel();


      /*  db = FirebaseFirestore.getInstance();

        CollectionReference cities = db.collection("users");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "Max Mustermann");
        data1.put("email", "max@mustermann.de");
        cities.document("SF").set(data1);

        //get current userID and partnerID instead of SF
        DocumentReference docRef2 = db.collection("sessions").document("m2SCORCU5GVC2EtpIHor");
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("duration"));
                        startTimer(document.getData().get("duration").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
            });

        DocumentReference documentReference = db.collection("users").document("6RZPdsF6vSQQHAe4BXwTkTXtowb2");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("name"));
                            creatorName.setText(document.getData().get("name").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });*/

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void getCurrentUser() {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            user= null;
        } else {
            user= (User) extras.get(Constants.USER);
            session = (String) extras.get(Constants.SESSIONS);
            Log.d(TAG, String.valueOf(user));
            Log.d(TAG,String.valueOf(session));

        }
    }

    private void stopTimer() {
        try {
            countdownTimer.cancel();

        } catch (Exception e) {

        }
        textTimer.setText("Stopped!");
    }



    /**
     * observes the current session and sets the variables to the current values
     * but does not work yet
     */
    private void initViewModel(){
        sessionCreationViewModel = new ViewModelProvider(this).get(SessionCreationViewModel.class);
        sessionCreationViewModel.getCurrentSession().observe(this, sessions -> {
            textTimer.setText(sessions.getDuration());
            creatorName.setText((CharSequence) sessions.getOwner());
            creatorWork.setText((CharSequence) sessions.getOwnerSetting().getCategories());
            creatorGoal.setText(sessions.getOwnerSetting().getGoal());
            partnerName.setText((CharSequence) sessions.getPartner());
            partnerWork.setText((CharSequence) sessions.getPartnerSetting().getCategories());
            partnerGoal.setText(sessions.getPartnerSetting().getGoal());

             });
    }

    private void initViewModels() {
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getSessions(user.getUid()).observe(this, sessions -> {
            partnerGoal.setText(session);
        });
        creatorName.setText(user.getName());


    }

    /**
     * starts timer from database timer duration
     */
    private void startTimer(String duration){
        if (duration.length() > 0) {
            myProgress = 0;

            try {
                countdownTimer.cancel();

            } catch (Exception e) {

            }

            progress = 1;
            endTime = Integer.parseInt(duration); // up to finish time

            countdownTimer = new CountDownTimer(60 * endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime*60);
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
                    setProgress(progress, endTime*60);
                    textTimer.setText("Finished");
                }
            };
            countdownTimer.start();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your session time", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * starts countdown & progress bar timer with time given from the input field
     */
    private void startTimer() {
        if (et_timer.getText().toString().length() > 0) {
            myProgress = 0;

            try {
                countdownTimer.cancel();

            } catch (Exception e) {

            }

            String timeInterval = et_timer.getText().toString();
            progress = 1;
            endTime = Integer.parseInt(timeInterval); // up to finish time

            countdownTimer = new CountDownTimer(60 * endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(progress, endTime*60);
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
                    setProgress(progress, endTime*60);
                    textTimer.setText("Finished");
                }
            };
            countdownTimer.start();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your session time", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * sets the circular progress bar correctly
     * @param startTime
     * @param endTime
     */
    public void setProgress(int startTime, int endTime) {
        progressBar.setMax(endTime);
        progressBar.setSecondaryProgress(endTime);
        progressBar.setProgress(startTime);

    }

}