package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.repositories.SessionHistoryRepository;

import java.util.List;


public class SessionHistoryViewModel extends ViewModel {
    private final SessionHistoryRepository sessionHistoryRepository;
    private LiveData<List<Session>> sessions;

    public SessionHistoryViewModel() {
        sessionHistoryRepository = new SessionHistoryRepository();
    }

    public LiveData<List<Session>> getSessions(String userId) {
        sessions = sessionHistoryRepository.getSessions(userId);
        return sessions;
    }
}
