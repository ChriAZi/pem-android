package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionReflection;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReflectionRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    public LiveData<Boolean> addReflection(String userId, String sessionId, SessionReflection reflection) {
        MutableLiveData<Boolean> added = new MutableLiveData<>();
        sessionsRef.document(sessionId).get().addOnCompleteListener(sessionTask -> {
            if (sessionTask.isSuccessful()) {
                Session session = sessionTask.getResult().toObject(Session.class);
                boolean owner = userId.equals(session.getOwner().getUid());
                sessionsRef.document(sessionId)
                        .update(owner ? "ownerReflection" : "partnerReflection", reflection)
                        .addOnCompleteListener(sessionUpdateTask -> {
                            if (sessionUpdateTask.isSuccessful()) {
                                added.setValue(true);
                            } else {
                                added.setValue(false);
                                Logger.log(sessionTask.getException().getMessage());
                            }
                        });
            } else {
                Logger.log(sessionTask.getException().getMessage());
            }
        });
        return added;
    }
}
