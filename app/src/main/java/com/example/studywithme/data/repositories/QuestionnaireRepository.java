package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionnaireRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);
    private final CollectionReference usersRef = rootRef.collection(Constants.USERS);

    public LiveData<String> startSession(String userId, Session session) {
        MutableLiveData<String> sessionId = new MutableLiveData<>();
        DocumentReference owner = usersRef.document(userId);
        session.setOwner(owner);
        session.setActive(true);
        sessionsRef
                .add(session)
                .addOnCompleteListener(sessionTask -> {
                    if (sessionTask.isSuccessful()) {
                        DocumentReference document = sessionTask.getResult();
                        String documentId = document.getId();
                        FieldValue startedAt = FieldValue.serverTimestamp();
                        sessionsRef
                                .document(documentId)
                                .update("uid", documentId,
                                        "startedAt", startedAt)
                                .addOnCompleteListener(sessionUpdateTask -> {
                                    if (sessionUpdateTask.isSuccessful()) {
                                        sessionId.setValue(documentId);
                                    } else {
                                        Logger.log(sessionUpdateTask.getException().getMessage());
                                    }
                                });
                    } else {
                        Logger.log(sessionTask.getException().getMessage());
                    }
                });
        return sessionId;
    }

    public LiveData<Boolean> joinSession(String sessionId, String userId, SessionSetting settings) {
        MutableLiveData<Boolean> joined = new MutableLiveData<>(false);
        DocumentReference userDocument = usersRef.document(userId);
        DocumentReference sessionDocument = sessionsRef.document(sessionId);
        sessionDocument
                .update(
                        "partner", userDocument,
                        "public", false,
                        "partnerSetting.name", settings.getName(),
                        "partnerSetting.goal", settings.getGoal(),
                        "partnerSetting.categories", settings.getCategories(),
                        "partnerSetting.tasks", settings.getTasks()
                )
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        joined.setValue(true);
                    } else {
                        Logger.log(updateTask.getException().getMessage());
                        joined.setValue(false);
                    }
                });
        return joined;
    }
}