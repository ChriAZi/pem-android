package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.User;
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
        DocumentReference ownerReference = usersRef.document(userId);
        ownerReference.get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                session.setOwner(userTask.getResult().toObject(User.class));
                session.setActive(true);
                session.setHasPartner(false);
                sessionsRef
                        .add(session)
                        .addOnCompleteListener(sessionTask -> {
                            if (sessionTask.isSuccessful()) {
                                DocumentReference reference = sessionTask.getResult();
                                String documentId = reference.getId();
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
            } else {
                Logger.log(userTask.getException().getMessage());
            }
        });
        return sessionId;
    }

    public LiveData<Boolean> isJoining(String sessionId) {
        MutableLiveData<Boolean> isJoining = new MutableLiveData<>(false);
        sessionsRef.document(sessionId)
                .update("hasPartner", true)
                .addOnCompleteListener(sessionUpdateTask -> {
                    if (sessionUpdateTask.isSuccessful()) {
                        isJoining.setValue(true);
                    } else {
                        isJoining.setValue(false);
                        Logger.log(sessionUpdateTask.getException().getMessage());
                    }
                });
        return isJoining;
    }

    public LiveData<Boolean> joinSession(String sessionId, String userId, SessionSetting settings) {
        MutableLiveData<Boolean> joined = new MutableLiveData<>(false);
        DocumentReference partnerReference = usersRef.document(userId);
        DocumentReference sessionReference = sessionsRef.document(sessionId);
        partnerReference.get().addOnCompleteListener(partnerTask -> {
            if (partnerTask.isSuccessful()) {
                sessionReference
                        .update(
                                "partner", partnerTask.getResult().toObject(User.class),
                                "public", false,
                                "partnerSetting", settings
                        )
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                joined.setValue(true);
                            } else {
                                Logger.log(updateTask.getException().getMessage());
                                joined.setValue(false);
                            }
                        });
            } else {
                Logger.log(partnerTask.getException().getMessage());
            }
        });
        return joined;
    }
}
