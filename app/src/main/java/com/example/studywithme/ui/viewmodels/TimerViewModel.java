package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.repositories.TimerRepository;

import java.util.List;

/**
 * the viewModel providing the interface to all timer tasks
 */
public class TimerViewModel extends AbstractViewModel {
    private final TimerRepository timerRepository;
    private LiveData<Boolean> finished;

    public TimerViewModel() {
        timerRepository = new TimerRepository();
    }

    /**
     * requests the end of a session
     *
     * @param sessionId the ID of the session to be ended
     * @return LiveData indicating whether the update was successful
     */
    public LiveData<Boolean> endSession(String sessionId) {
        finished = timerRepository.endSession(sessionId);
        return finished;
    }

    /**
     * requests the update of the status of the session tasks
     *
     * @param sessionId the ID of the session to be updated
     * @param userId    the ID of the user requesting the update
     * @param tasks     a list of tasks to update
     */
    public void updateTasks(String sessionId, String userId, List<SessionTask> tasks) {
        timerRepository.updateTasks(sessionId, userId, tasks);
    }
}
