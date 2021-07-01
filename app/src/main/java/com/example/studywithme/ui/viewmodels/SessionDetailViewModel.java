package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionDetailRepository;

import java.util.List;


public class SessionDetailViewModel extends AbstractViewModel {
    private final SessionDetailRepository sessionDetailRepository;
    private LiveData<Session> session;

    public SessionDetailViewModel() {
        sessionDetailRepository = new SessionDetailRepository();
    }

    public LiveData<Session> getSession(String sessionId) {
        session = sessionDetailRepository.getSession(sessionId);
        return session;
    }
}
