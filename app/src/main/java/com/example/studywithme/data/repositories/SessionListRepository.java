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
 * Repository for retrieving a list of active Sessions from the backend
 */
public class SessionListRepository {

    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    /**
     * Fetches all active and public sessions that are not owned by the user requesting them
     * Checks also whether a user is currently in the process of joining
     *
     * @param userId the ID of the user requesting the list of sessions
     * @return LiveData holding a list of all relevant sessions
     */
    public LiveData<List<Session>> getPublicSessions(String userId) {
        MutableLiveData<List<Session>> sessions = new MutableLiveData<>();
        sessionsRef
                .whereEqualTo("public", true)
                .whereEqualTo("hasPartner", false)
                .whereEqualTo("active", true)
                .whereNotEqualTo("owner.uid", userId)
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        Logger.log("Listen failed." + exception);
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        List<Session> fetchedDocuments = new ArrayList<>();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Session session = document.toObject(Session.class);
                            fetchedDocuments.add(session);
                        }
                        sessions.setValue(fetchedDocuments);
                    } else {
                        Logger.log("No data in collection.");
                    }
                });
        return sessions;
    }
}