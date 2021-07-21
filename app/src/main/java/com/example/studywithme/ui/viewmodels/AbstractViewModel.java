package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.data.services.SessionService;
import com.example.studywithme.data.services.UserService;

/**
 * the abstract class to be implemented by all ViewModels that whish
 * to request access to the current User or Session information
 */
public abstract class AbstractViewModel extends ViewModel {
    private LiveData<Session> activeSession;
    private LiveData<User> currentUser;

    /**
     * requests the Session from the SessionService
     *
     * @param sessionId the ID of the session to be fetched
     * @return LiveData holding the fetched Session
     */
    public LiveData<Session> getActiveSession(String sessionId) {
        activeSession = SessionService.getInstance().getActiveSession(sessionId);
        return activeSession;
    }

    /**
     * requests the User from the UserService
     *
     * @param userId the ID of the user to be fetched
     * @return LiveData holding the fetched User
     */
    public LiveData<User> getCurrentUser(String userId) {
        currentUser = UserService.getInstance().getCurrentUser(userId);
        return currentUser;
    }
}
