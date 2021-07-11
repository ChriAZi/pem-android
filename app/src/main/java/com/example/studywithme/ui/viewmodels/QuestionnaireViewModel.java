package com.example.studywithme.ui.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.repositories.QuestionnaireRepository;


public class QuestionnaireViewModel extends AbstractViewModel {
    private final QuestionnaireRepository questionnaireRepository;
    private LiveData<String> sessionId;
    private LiveData<Boolean> isJoining;
    private LiveData<Boolean> joined;

    public QuestionnaireViewModel() {
        questionnaireRepository = new QuestionnaireRepository();
    }

    public LiveData<String> startSession(String userId, Session newSession) {
        sessionId = questionnaireRepository.startSession(userId, newSession);
        return sessionId;
    }

    public LiveData<Boolean> isJoining(String sessionId, boolean hasPartner) {
        isJoining = questionnaireRepository.isJoining(sessionId, hasPartner);
        return isJoining;
    }

    public LiveData<Boolean> joinSession(String sessionId, String userId, SessionSetting settings) {
        joined = questionnaireRepository.joinSession(sessionId, userId, settings);
        return joined;
    }
}
