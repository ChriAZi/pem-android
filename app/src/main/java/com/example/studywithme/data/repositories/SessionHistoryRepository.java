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

public class SessionHistoryRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);

    public LiveData<List<Session>> getPastSessions(String userId) {
        MutableLiveData<List<Session>> sessions = new MutableLiveData<>();
        sessionsRef
                .whereEqualTo("owner.uid", userId)
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
                            fetchedSessions.add(session);
                        }
                        sessions.setValue(fetchedSessions);
                    } else {
                        Logger.log("No data in collection.");
                    }
                });
        return sessions;
    }
}
