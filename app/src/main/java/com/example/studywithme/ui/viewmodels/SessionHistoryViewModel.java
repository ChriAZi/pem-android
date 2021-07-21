package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionHistoryRepository;

import java.util.List;

/**
 * the viewModel providing the interface to all session history tasks
 */
public class SessionHistoryViewModel extends AbstractViewModel {
    private final SessionHistoryRepository sessionHistoryRepository;
    private LiveData<List<Session>> sessions;

    public SessionHistoryViewModel() {
        sessionHistoryRepository = new SessionHistoryRepository();
    }

    /**
     * requests a list of all past sessions for a given user
     *
     * @param userId the ID of the user requesting the sessions
     * @return LiveData holding a list of the requested sessions
     */
    public LiveData<List<Session>> getPastSessions(String userId) {
        sessions = sessionHistoryRepository.getPastSessions(userId);
        return sessions;
    }
}
