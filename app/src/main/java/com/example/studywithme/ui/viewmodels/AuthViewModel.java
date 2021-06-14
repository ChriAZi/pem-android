package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.User;
import com.example.studywithme.data.repositories.AuthRepository;
import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends AbstractViewModel {
    private final AuthRepository authRepository;
    private LiveData<User> authenticatedUser;
    private LiveData<User> createdUser;

    public AuthViewModel() {
        authRepository = new AuthRepository();
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUser = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    public LiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public LiveData<User> getCreatedUser() {
        return createdUser;
    }

    public void createUser(User authenticatedUser) {
        createdUser = authRepository.createUserIfNotExists(authenticatedUser);
    }
}
