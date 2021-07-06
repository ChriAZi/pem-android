package com.example.studywithme.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.MainActivity;
import com.example.studywithme.ui.authentication.AuthActivity;
import com.example.studywithme.ui.join.SessionListActivity;
import com.example.studywithme.ui.viewmodels.SplashViewModel;
import com.example.studywithme.utils.Constants;

public class SplashActivity extends AppCompatActivity {
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSplashViewModel();
        checkIfUserIsAuthenticated();
    }

    private void initSplashViewModel() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.getIsUserAuthenticated().observe(this, user -> {
            if (!user.isAuthenticated()) {
                startAuthActivity();
                finish();
            } else {
                getUserFromDatabase(user.getUid());
            }
        });
    }

    private void startAuthActivity() {
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    private void getUserFromDatabase(String id) {
        splashViewModel.setUserFromUid(id);
        splashViewModel.getUser().observe(this, user -> {
            startMainActivity(user.getUid());
            finish();
        });
    }

    private void startMainActivity(String userId) {
        //Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        Intent intent = new Intent(SplashActivity.this, SessionListActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
    }
}