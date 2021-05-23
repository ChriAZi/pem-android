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

public class SplashRepository {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final User user = new User();
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);

    public MutableLiveData<User> checkIfUserIsAuthenticatedInFirebase() {
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

    public MutableLiveData<User> setUserFromUid(String uid) {
        MutableLiveData<User> user = new MutableLiveData<>();
        usersRef.document(uid).get().addOnCompleteListener(userTask -> {
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
