package com.example.studywithme.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.ui.authentication.AuthActivity;
import com.example.studywithme.ui.join.SessionListActivity;
import com.example.studywithme.ui.viewmodels.SplashViewModel;
import com.example.studywithme.utils.Constants;

/**
 * Activity handling the persistence of user authentication
 */
public class SplashActivity extends AppCompatActivity {
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSplashViewModel();
        checkIfUserIsAuthenticated();
    }

    /**
     * inititializes the SplashViewModel
     *
     * @see SplashViewModel
     */
    private void initSplashViewModel() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    /**
     * Requests a check for the authentication status of the user
     */
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

    /**
     * Starts the AuthActivity
     *
     * @see AuthActivity
     */
    private void startAuthActivity() {
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    /**
     * Requests the full User object
     *
     * @param id the ID of the user to fetch
     */
    private void getUserFromDatabase(String id) {
        splashViewModel.getUserFromId(id);
        splashViewModel.getUser().observe(this, user -> {
            startSessionListActivity(user.getUid());
            finish();
        });
    }

    /**
     * starts the SessionListActivity
     *
     * @param userId the ID of the now logged-in user
     * @see SessionListActivity
     */
    private void startSessionListActivity(String userId) {
        Intent intent = new Intent(SplashActivity.this, SessionListActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
    }
}