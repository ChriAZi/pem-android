package com.example.studywithme.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.MainActivity;
import com.example.studywithme.ui.authentication.AuthActivity;
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

    private void getUserFromDatabase(String uid) {
        splashViewModel.setUserFromUid(uid);
        splashViewModel.getUserLiveData().observe(this, user -> {
            startMainActivity(user);
            finish();
        });
    }

    private void startMainActivity(User user) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(Constants.USER, user);
        startActivity(intent);
    }
}