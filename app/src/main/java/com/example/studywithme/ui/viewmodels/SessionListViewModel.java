package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionListRepository;

import java.util.List;

public class SessionListViewModel extends AbstractViewModel {

    private final SessionListRepository sessionListRepository;
    private LiveData<List<Session>> sessions;

    public SessionListViewModel() {
        sessionListRepository = new SessionListRepository();
    }

    public LiveData<List<Session>> getPublicSessions() {
        sessions = sessionListRepository.getPublicSessions();
        return sessions;
    }
}
