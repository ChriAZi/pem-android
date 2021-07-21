package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.SessionReflection;
import com.example.studywithme.data.repositories.ReflectionRepository;

/**
 * the viewModel providing the interface to all session reflection tasks
 */
public class ReflectionViewModel extends AbstractViewModel {
    private final ReflectionRepository reflectionRepository;
    private LiveData<Boolean> added;

    public ReflectionViewModel() {
        reflectionRepository = new ReflectionRepository();
    }

    /**
     * requests the update of a session to add a new reflection
     *
     * @param userId     the ID of the user adding the reflection
     * @param sessionId  the ID of the relevant session
     * @param reflection the reflection information as provided by the user
     * @return LiveData indicating whether the update was successful
     */
    public LiveData<Boolean> addReflection(String userId, String sessionId, SessionReflection reflection) {
        added = reflectionRepository.addReflection(userId, sessionId, reflection);
        return added;
    }
}
