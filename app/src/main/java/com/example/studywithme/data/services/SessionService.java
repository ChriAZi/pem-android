package com.example.studywithme.data.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A service to fetch the currently active session
 * Can be used in all views
 */
public class SessionService {
    private static SessionService INSTANCE = null;
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    private SessionService() {
    }

    /**
     * Creating a Singelton Instance of the service
     *
     * @return the existing or a new SessionService
     */
    public static SessionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionService();
        }
        return (INSTANCE);
    }

    /**
     * Fetches the currently active Session information from the backend
     *
     * @param sessionId the ID of the session to fetch information for
     * @return LiveData holding the information about the requested Session
     */
    public LiveData<Session> getActiveSession(String sessionId) {
        MutableLiveData<Session> session = new MutableLiveData<>();
        sessionsRef
                .whereEqualTo("uid", sessionId)
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        Logger.log("Listen failed." + exception);
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        List<Session> fetchedDocuments = new ArrayList<>();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Session fetchedSession = document.toObject(Session.class);
                            fetchedDocuments.add(fetchedSession);
                        }
                        session.setValue(fetchedDocuments.get(0));
                    } else {
                        Logger.log("No data in collection.");
                    }
                });
        return session;
    }

}
