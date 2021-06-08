package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SessionCreationRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);

    public LiveData<Session> createSession(String userId, Session session) {
        MutableLiveData<Session> newSession = new MutableLiveData<>();
        DocumentReference owner = usersRef.document(userId);
        session.setOwner(owner);
        sessionsRef
                .add(session)
                .addOnCompleteListener(sessionTask -> {
                    if (sessionTask.isSuccessful()) {
                        DocumentReference document = sessionTask.getResult();
                        session.setUid(document.getId());
                        newSession.setValue(session);
                    } else {
                        Logger.log(sessionTask.getException().getMessage());
                    }
                });
        return newSession;
    }
}
