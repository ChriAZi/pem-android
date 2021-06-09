package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.data.services.SessionService;
import com.example.studywithme.data.services.UserService;

public abstract class AbstractViewModel extends ViewModel {
    private LiveData<Session> activeSession;
    private LiveData<User> currentUser;

    public LiveData<Session> getActiveSession(String sessionId) {
        activeSession = SessionService.getInstance().getActiveSession(sessionId);
        return activeSession;
    }

    public LiveData<User> getCurrentUser(String userId) {
        currentUser = UserService.getInstance().getCurrentUser(userId);
        return currentUser;
    }
}
