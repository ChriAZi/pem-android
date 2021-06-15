package com.example.studywithme.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.Logger;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
