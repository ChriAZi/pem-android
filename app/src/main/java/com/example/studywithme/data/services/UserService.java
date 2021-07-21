package com.example.studywithme.data.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.User;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A service to fetch the currently active user
 * Can be used in all views through the AbstractViewModel
 *
 * @see com.example.studywithme.ui.viewmodels.AbstractViewModel
 */
public class UserService {
    private static UserService INSTANCE = null;
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);

    private UserService() {
    }

    /**
     * Creating a Singelton Instance of the service
     *
     * @return the existing or a new UserService
     */
    public static UserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserService();
        }
        return (INSTANCE);
    }

    /**
     * Fetches the currently active User information from the backend
     *
     * @param userId the ID of the user to fetch information for
     * @return LiveData holding the information about the requested User
     */
    public LiveData<User> getCurrentUser(String userId) {
        DocumentReference userDocument = usersRef.document(userId);
        MutableLiveData<User> user = new MutableLiveData<>();
        userDocument.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    user.setValue(document.toObject(User.class));
                } else {
                    Logger.log("No such document");
                }
            } else {
                Logger.log("Error fetching data " + task.getException());
            }
        });
        return user;
    }
}
