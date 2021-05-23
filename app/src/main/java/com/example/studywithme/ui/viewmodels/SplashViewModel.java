package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studywithme.data.models.User;
import com.example.studywithme.data.repositories.SplashRepository;

public class SplashViewModel extends ViewModel {
    private final SplashRepository splashRepository;
    LiveData<User> isUserAuthenticated;
    LiveData<User> userLiveData;

    public SplashViewModel() {
        splashRepository = new SplashRepository();
    }

    public void checkIfUserIsAuthenticated() {
        isUserAuthenticated = splashRepository.checkIfUserIsAuthenticatedInFirebase();
    }

    public LiveData<User> getIsUserAuthenticated() {
        return isUserAuthenticated;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void setUserFromUid(String uid) {
        userLiveData = splashRepository.setUserFromUid(uid);
    }
}
