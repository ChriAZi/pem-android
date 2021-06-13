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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.AbstractViewModel;
import com.example.studywithme.ui.viewmodels.AbstractViewModelFactory;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.studywithme.data.models.User.getIdFromPreferences;

public class TimerActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textTimer,creatorName,partnerName,creatorGoal,partnerGoal,creatorWork,partnerWork,started;
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
    public AbstractViewModel abstractViewModel;
    private User user;
    private String session2;
    private Session session;
    private String uID;
    private boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


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



        getCurrentSession();
        getCurrentUser();
        //initAbstractViewModel();
        initViewModel();


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

    private void getCurrentSession() {
        // session = Session.getIdFromPreferences(this);
    }

    private void getCurrentUser() {
       // user = User.getIdFromPreferences(this);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            user= null;
            session2 = null;
        } else {
            user= (User) extras.get(Constants.USER);
            session2 = (String) extras.get(Constants.SESSION_ID);
            Log.d(TAG, String.valueOf(user));
            Log.d(TAG,String.valueOf(session));

        }
    }

    private void stopTimer() {
        try {
            countdownTimer.cancel();
            active = false;

        } catch (Exception e) {

        }
        textTimer.setText("Stopped!");
    }



    /**
     * observes the current session and sets the variables to the current values
     */
    private void initViewModel(){
        abstractViewModel = new ViewModelProvider(this).get(AbstractViewModel.class);
        abstractViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session -> {

            textTimer.setText(String.valueOf(session.getDuration()));
            creatorName.setText(user.getName());
            creatorWork.setText(session.getOwnerSetting().getCategories().get(0).toString());
            creatorGoal.setText(session.getOwnerSetting().getGoal().toString());
           // partnerName.setText(session.getPartner().toString());
            partnerWork.setText(session.getPartnerSetting().getCategories().get(0).toString());
            partnerGoal.setText(session.getPartnerSetting().getGoal().toString());

            started.setText(Boolean.toString(session.isActive()));
            if(!active){
                session.setActive(false);
            }else{
                session.setActive(true);
            }

             });
    }

    private void initAbstractViewModel(){
      // abstractViewModel = new ViewModelProviders.of(this, new AbstractViewModelFactory()).get(AbstractViewModel.class);
      //  final Observer<Session> sessionobserver = new Observer<Session>() {
      //      @Override
       //     public void onChanged(Session sessionS) {
        //Session currentSession = abstractViewModel.getActiveSession(session2);
        /*
                creatorName.setText((CharSequence) currentSession.getOwner());
                creatorWork.setText((CharSequence) sessionS.getOwnerSetting().getCategories());
                creatorGoal.setText(session2.getOwnerSetting().getGoal());
      */      }
     //   };
       /* abstractViewModel.getCurrentUser(user.getUid()).observe(this,user ->{
            creatorName.setText((CharSequence) user.getName());
        });
     /*   abstractViewModel.getActiveSession(session2).observe(this, session ->{
            creatorName.setText((CharSequence) session.getOwner());
            creatorWork.setText((CharSequence) session.getOwnerSetting().getCategories());
            creatorGoal.setText(session.getOwnerSetting().getGoal());
        });*/

   // }

    private void initViewModels() {
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getSessions(user.getUid()).observe(this, sessions -> {
            partnerGoal.setText(session2);
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
                   // started.setText("Session finished");
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
        if (et_timer.getText().toString().length() > 0 || textTimer.getText() != "00:00" ){
            myProgress = 0;

            try {
                countdownTimer.cancel();

            } catch (Exception e) {

            }
            String timeInterval = "";
            if(et_timer.getText().toString().length() >0){
            timeInterval = et_timer.getText().toString();}
            else{
                timeInterval = textTimer.getText().toString();
            }
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
                   // started.setText("Session inactive");
                }
            };
            countdownTimer.start();
            active = true;
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmmss");
            String dateString = formatter.format(new Date(tsLong));
            Log.d("Timestamp", dateString);
           // started.setText("Session active");
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