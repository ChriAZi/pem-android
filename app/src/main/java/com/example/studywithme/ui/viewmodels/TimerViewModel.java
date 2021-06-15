package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.repositories.TimerRepository;


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
}
