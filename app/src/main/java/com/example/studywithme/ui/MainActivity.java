package com.example.studywithme.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textTimer;
    Button start;
    EditText et_timer;
    CountDownTimer countdownTimer;
    int myProgress = 0;
    int progress;
    int endTime = 250;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.barTimer);
        textTimer = findViewById(R.id.timer_countdown);
        start = findViewById(R.id.start);
        et_timer = findViewById(R.id.session_duration);

        // startTimer(5);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }


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
    public void setProgress(int startTime, int endTime) {
        progressBar.setMax(endTime);
        progressBar.setSecondaryProgress(endTime);
        progressBar.setProgress(startTime);

    }

}