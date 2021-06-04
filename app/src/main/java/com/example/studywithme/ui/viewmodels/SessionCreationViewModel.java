package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionCreationRepository;


public class SessionCreationViewModel extends ViewModel {
    private final SessionCreationRepository sessionCreationRepository;
    private LiveData<Session> session;

    public SessionCreationViewModel() {
        sessionCreationRepository = new SessionCreationRepository();
    }

    public void createSession(String userId, Session newSession) {
        session = sessionCreationRepository.createSession(userId, newSession);
    }

    public LiveData<Session> getCurrentSession() {
        return session;
    }
}
