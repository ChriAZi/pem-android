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

public class AuthRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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

    public MutableLiveData<User> createUserIfNotExists(User authenticatedUser) {
        MutableLiveData<User> newUser = new MutableLiveData<>();
        DocumentReference uidRef = usersRef.document(authenticatedUser.getUid());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();
                if (!document.exists()) {
                    uidRef.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
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
