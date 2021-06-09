package com.example.studywithme.data.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SessionService {
    private static SessionService INSTANCE = null;
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    private SessionService() {
    }

    public static SessionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionService();
        }
        return (INSTANCE);
    }

    public LiveData<Session> getActiveSession(String sessionId) {
        DocumentReference sessionDocument = sessionsRef.document(sessionId);
        MutableLiveData<Session> session = new MutableLiveData<>();
        sessionDocument.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    session.setValue(document.toObject(Session.class));
                } else {
                    Logger.log("No such document");
                }
            } else {
                Logger.log("Error fetching data " + task.getException());
            }
        });
        return session;
    }

}
