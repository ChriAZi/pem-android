package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.User;
import com.example.studywithme.data.repositories.SplashRepository;

/**
 * the viewModel providing the interface to all authentication persistence tasks
 */
public class SplashViewModel extends AbstractViewModel {
    private final SplashRepository splashRepository;
    LiveData<User> isUserAuthenticated;
    LiveData<User> userLiveData;

    public SplashViewModel() {
        splashRepository = new SplashRepository();
    }

    /**
     * requests the check of the authentication status of a user
     */
    public void checkIfUserIsAuthenticated() {
        isUserAuthenticated = splashRepository.checkIfUserIsAuthenticated();
    }

    /**
     * returns the status of the User
     *
     * @return LiveData holding the requested User object
     */
    public LiveData<User> getIsUserAuthenticated() {
        return isUserAuthenticated;
    }

    /**
     * returns the requested User object
     *
     * @return LiveData holding the requested User object
     */
    public LiveData<User> getUser() {
        return userLiveData;
    }

    /**
     * requests the retrieval of user information
     *
     * @param userId the ID of the user to be retrieved
     */
    public void getUserFromId(String userId) {
        userLiveData = splashRepository.getUserFromId(userId);
    }
}
