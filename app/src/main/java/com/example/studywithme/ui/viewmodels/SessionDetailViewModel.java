package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionDetailRepository;

/**
 * the viewModel providing the interface to all detailed session information tasks
 */
public class SessionDetailViewModel extends AbstractViewModel {
    private final SessionDetailRepository sessionDetailRepository;
    private LiveData<Session> session;

    public SessionDetailViewModel() {
        sessionDetailRepository = new SessionDetailRepository();
    }

    /**
     * requests detailed information of a session
     *
     * @param sessionId the ID of the relevant session
     * @return LiveData holding the information of the requested Session
     */
    public LiveData<Session> getSession(String sessionId) {
        session = sessionDetailRepository.getSession(sessionId);
        return session;
    }
}
