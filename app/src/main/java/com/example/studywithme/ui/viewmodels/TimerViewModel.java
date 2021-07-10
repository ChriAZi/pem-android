package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.repositories.TimerRepository;

import java.util.List;


public class TimerViewModel extends AbstractViewModel {
    private final TimerRepository timerRepository;
    private LiveData<Boolean> finished;
    private LiveData<Boolean> updatedTasks;

    public TimerViewModel() {
        timerRepository = new TimerRepository();
    }

    public LiveData<Boolean> endSession(String sessionId) {
        finished = timerRepository.endSession(sessionId);
        return finished;
    }

    public LiveData<Boolean> updateTasks(String sessionId, String userId, List<SessionTask> tasks) {
        updatedTasks = timerRepository.updateTasks(sessionId, userId, tasks);
        return updatedTasks;
    }
}
