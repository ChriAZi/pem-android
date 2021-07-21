package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.User;
import com.example.studywithme.data.repositories.AuthRepository;
import com.google.firebase.auth.AuthCredential;

/**
 * the viewModel providing the interface to all authentication tasks
 */
public class AuthViewModel extends AbstractViewModel {
    private final AuthRepository authRepository;
    private LiveData<User> authenticatedUser;
    private LiveData<User> createdUser;

    public AuthViewModel() {
        authRepository = new AuthRepository();
    }

    /**
     * requests the sign in of a user using GoogleSignIn
     *
     * @param googleAuthCredential the authenticationCredentials provided by Google
     */
    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUser = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    /**
     * Returns the fetched User
     *
     * @return LiveData holding the fetched User
     */
    public LiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Returns the created User
     *
     * @return LiveData holding the created User
     */
    public LiveData<User> getCreatedUser() {
        return createdUser;
    }

    /**
     * requests the storage of a new User object
     *
     * @param authenticatedUser the User to be stored
     */
    public void createUser(User authenticatedUser) {
        createdUser = authRepository.createUserIfNotExists(authenticatedUser);
    }
}
