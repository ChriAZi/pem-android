package com.example.studywithme.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.User;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Repository for managing persistence of authentication tasks
 */
public class SplashRepository {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final User user = new User();
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);

    /**
     * Checks if the user requesting to log in is already authenticated
     *
     * @return LiveData holding the relevant user
     */
    public MutableLiveData<User> checkIfUserIsAuthenticated() {
        MutableLiveData<User> isUserAuthenticatedInFirebase = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            user.setAuthenticated(false);
        } else {
            user.setUid(firebaseUser.getUid());
            user.setAuthenticated(true);
        }
        isUserAuthenticatedInFirebase.setValue(user);
        return isUserAuthenticatedInFirebase;
    }

    /**
     * Creates a User object from the data provided by the backend
     *
     * @param userId the ID of the
     * @return LiveData holding the fetched User object
     */
    public MutableLiveData<User> getUserFromId(String userId) {
        MutableLiveData<User> user = new MutableLiveData<>();
        usersRef.document(userId).get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                DocumentSnapshot document = userTask.getResult();
                if (document.exists()) {
                    user.setValue(document.toObject(User.class));
                } else {
                    Logger.log("Error getting User from database.");
                }
            } else {
                Logger.log(userTask.getException().getMessage());
            }
        });
        return user;
    }
}
