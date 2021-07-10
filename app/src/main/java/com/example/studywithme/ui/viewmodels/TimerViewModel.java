package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.repositories.TimerRepository;

import java.util.List;


public class TimerViewModel extends AbstractViewModel {
    private final TimerRepository timerRepository;
    private LiveData<Boolean> finished;

    public TimerViewModel() {
        timerRepository = new TimerRepository();
    }

    public LiveData<Boolean> endSession(String sessionId) {
        finished = timerRepository.endSession(sessionId);
        return finished;
    }

    public void updateTasks(String sessionId, String userId, List<SessionTask> tasks) {
        timerRepository.updateTasks(sessionId, userId, tasks);
    }
}
