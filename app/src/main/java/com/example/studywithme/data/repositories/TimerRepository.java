package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TimerRepository {
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference sessionsRef = rootRef.collection(Constants.SESSIONS);

    public LiveData<Boolean> endSession(String sessionId) {
        MutableLiveData<Boolean> finished = new MutableLiveData<>(false);
        DocumentReference sessionDocument = sessionsRef.document(sessionId);
        sessionDocument
                .update("active", false)
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        finished.setValue(true);
                    } else {
                        Logger.log(updateTask.getException().getMessage());
                        finished.setValue(false);
                    }
                });
        return finished;
    }

    public void updateTasks(String sessionId, String userId, List<SessionTask> tasks) {
        sessionsRef.document(sessionId).get().addOnCompleteListener(sessionTask -> {
            if (sessionTask.isSuccessful()) {
                Session session = sessionTask.getResult().toObject(Session.class);
                boolean owner = userId.equals(session.getOwner().getUid());
                sessionsRef.document(sessionId)
                        .update(owner ? "ownerSetting.tasks" : "partnerSetting.tasks", tasks)
                        .addOnCompleteListener(sessionUpdateTask -> {
                            if (!sessionUpdateTask.isSuccessful()) {
                                Logger.log(sessionTask.getException().getMessage());
                            }
                        });
            } else {
                Logger.log(sessionTask.getException().getMessage());
            }
        });
    }
}
