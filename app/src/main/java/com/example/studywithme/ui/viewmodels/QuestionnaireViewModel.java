package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.repositories.QuestionnaireRepository;

/**
 * the viewModel providing the interface to all questionnaire tasks
 */
public class QuestionnaireViewModel extends AbstractViewModel {
    private final QuestionnaireRepository questionnaireRepository;
    private LiveData<String> sessionId;
    private LiveData<Boolean> isJoining;
    private LiveData<Boolean> joined;

    public QuestionnaireViewModel() {
        questionnaireRepository = new QuestionnaireRepository();
    }

    /**
     * requests the start of a session
     *
     * @param userId     the ID of the user owning the session
     * @param newSession the Session to be started
     * @return LiveData holding the ID of the created Session from the backend
     */
    public LiveData<String> startSession(String userId, Session newSession) {
        sessionId = questionnaireRepository.startSession(userId, newSession);
        return sessionId;
    }

    /**
     * requests information about the joining status of a session
     *
     * @param sessionId  the ID of the relevant session
     * @param hasPartner indicating whether a user is currently starting or leaving the join process
     * @return LiveData indicating the joining status
     */
    public LiveData<Boolean> isJoining(String sessionId, boolean hasPartner) {
        isJoining = questionnaireRepository.isJoining(sessionId, hasPartner);
        return isJoining;
    }

    /**
     * requests the joining of a session
     *
     * @param sessionId the ID of the session to be joined
     * @param userId    the ID of the user wanting to join
     * @param settings  the settings as set by the joining user
     * @return LiveData indicating whether the joining process was successful
     */
    public LiveData<Boolean> joinSession(String sessionId, String userId, SessionSetting settings) {
        joined = questionnaireRepository.joinSession(sessionId, userId, settings);
        return joined;
    }
}
