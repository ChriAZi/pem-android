package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionListRepository;

import java.util.List;

/**
 * the viewModel providing the interface to all session list tasks
 */
public class SessionListViewModel extends AbstractViewModel {

    private final SessionListRepository sessionListRepository;
    private LiveData<List<Session>> sessions;

    public SessionListViewModel() {
        sessionListRepository = new SessionListRepository();
    }

    /**
     * requests all active and public sessions
     *
     * @param userId the ID of the user requesting the sessions
     * @return LiveData holding a list of the requested sessions
     */
    public LiveData<List<Session>> getPublicSessions(String userId) {
        sessions = sessionListRepository.getPublicSessions(userId);
        return sessions;
    }
}
