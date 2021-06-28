package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.SessionReflection;
import com.example.studywithme.data.repositories.ReflectionRepository;

public class ReflectionViewModel extends AbstractViewModel {
    private final ReflectionRepository reflectionRepository;
    private LiveData<Boolean> added;

    public ReflectionViewModel() {
        reflectionRepository = new ReflectionRepository();
    }

    public LiveData<Boolean> addReflection(String userId, String sessionId, SessionReflection reflection) {
        added = reflectionRepository.addReflection(userId, sessionId, reflection);
        return added;
    }
}
