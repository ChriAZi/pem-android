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
        isUserAuthenticated = splashRepository.checkIfUserIsAuthenticated();
    }

    public LiveData<User> getIsUserAuthenticated() {
        return isUserAuthenticated;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void setUserFromUid(String id) {
        userLiveData = splashRepository.setUserFromId(id);
    }
}
