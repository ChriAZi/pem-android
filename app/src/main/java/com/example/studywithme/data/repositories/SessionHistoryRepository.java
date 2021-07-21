package com.example.studywithme.data.repositories;

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
 * Repository for retrieving a list of past Sessions from the backend
 */
public class SessionHistoryRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    /**
     * Fetches all past sessions for a given user
     *
     * @param userId the ID of the user for whom to fetch the sessions
     * @return LiveData holding a list of relevant Sessions
     */
    public LiveData<List<Session>> getPastSessions(String userId) {
        MutableLiveData<List<Session>> sessions = new MutableLiveData<>();
        sessionsRef
                .whereEqualTo("active", false)
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        Logger.log("Listen failed." + exception);
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        List<Session> fetchedSessions = new ArrayList<>();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Session session = document.toObject(Session.class);
                            if (session != null) {
                                if (session.getOwner().getUid().equals(userId)) {
                                    fetchedSessions.add(session);
                                } else if (session.getPartner() != null && session.getPartner().getUid().equals(userId)) {
                                    fetchedSessions.add(session);
                                }
                            }
                        }
                        sessions.setValue(fetchedSessions);
                    } else {
                        Logger.log("No data in collection.");
                    }
                });
        return sessions;
    }
}
