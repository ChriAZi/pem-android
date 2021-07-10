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
