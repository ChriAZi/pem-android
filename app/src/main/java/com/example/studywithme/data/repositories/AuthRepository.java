package com.example.studywithme.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.User;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Repository for managing authenticating tasks with the backend
 */
public class AuthRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /**
     * signs in a new user to the application using Google Authentication
     *
     * @param googleAuthCredential the authentication credentials as provided by Google
     * @return LiveData including the signed in user
     */
    public MutableLiveData<User> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        MutableLiveData<User> authenticatedUser = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    User user = new User(uid, name, email);
                    user.setNew(isNewUser);
                    authenticatedUser.setValue(user);
                }
            } else {
                Logger.log(authTask.getException().getMessage());
            }
        });
        return authenticatedUser;
    }

    /**
     * creates a user in the database if the user is not yet stored there
     * @param authenticatedUser the User object to be stored
     * @return LiveData including the created User
     */
    public MutableLiveData<User> createUserIfNotExists(User authenticatedUser) {
        MutableLiveData<User> newUser = new MutableLiveData<>();
        DocumentReference uidReference = usersRef.document(authenticatedUser.getUid());
        uidReference.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();
                if (!document.exists()) {
                    uidReference.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            authenticatedUser.setCreated(true);
                            newUser.setValue(authenticatedUser);
                        } else {
                            Logger.log(userCreationTask.getException().getMessage());
                        }
                    });
                } else {
                    newUser.setValue(authenticatedUser);
                }
            } else {
                Logger.log(uidTask.getException().getMessage());
            }
        });
        return newUser;
    }
}
