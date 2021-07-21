package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Repository for retrieving detailed information of past sessions
 */
public class SessionDetailRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    /**
     * Fetches detailed information of a given session from the backend
     *
     * @param sessionId the ID of the session to be fetched
     * @return LiveData holding the fetched Session
     */
    public LiveData<Session> getSession(String sessionId) {
        MutableLiveData<Session> session = new MutableLiveData<>();
        sessionsRef.document(sessionId).get().addOnCompleteListener(sessionTask -> {
            if (sessionTask.isSuccessful()) {
                session.setValue(sessionTask.getResult().toObject(Session.class));
            } else {
                Logger.log(sessionTask.getException().getMessage());
            }
        });
        return session;
    }
}
