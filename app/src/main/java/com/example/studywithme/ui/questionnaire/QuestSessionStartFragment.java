package com.example.studywithme.ui.questionnaire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionCategory;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.timer.TimerActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuestSessionStartFragment extends Fragment {

    public QuestSessionStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_start_session, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_public);
        backgroundImage.setImageResource(R.drawable.work_time);

        Button submitPublic = view.findViewById(R.id.btnSubmitPublic);
        Button submitPrivate = view.findViewById(R.id.btnSubmitPrivate);

        submitPublic.setOnClickListener(v -> startSessionForTimer(true));

        submitPrivate.setOnClickListener(v -> startSessionForTimer(false));

        return view;
    }

    private void startSessionForTimer(boolean isPublic) {
        SessionSetting ownerSetting = new SessionSetting(getSessionName(), getSessionGoal(), SessionCategory.HOBBY, getSessionTasks());
        Session session = new Session(getSessionDuration(), isPublic, ownerSetting);

        QuestionnaireViewModel questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        questionnaireViewModel.startSession(User.getIdFromPreferences(getContext()), session).observe(getViewLifecycleOwner(), sessionId -> {
            if (sessionId != null) {
                startTimerActivity();
            }
        });
    }

    private void startTimerActivity() {
        Intent intent = new Intent(getActivity(), TimerActivity.class);
        startActivity(intent);
    }

    private String getSessionName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(Constants.SESSION_QUEST_NAME, null);
    }

    private String getSessionGoal() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(Constants.SESSION_QUEST_GOAL, null);
    }

    private SessionCategory getSessionCategory() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return SessionCategory.valueOf(sp.getString(Constants.SESSION_QUEST_CATEGORY, null));
    }

    private List<SessionTask> getSessionTasks() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> set = sp.getStringSet(Constants.SESSION_QUEST_TASKS, null);
        ArrayList<SessionTask> tasks = new ArrayList<>();
        set.forEach(item -> {
            tasks.add(new SessionTask(item, false));
        });
        return tasks;
    }

    private int getSessionDuration() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getInt(Constants.SESSION_QUEST_DURATION, 0);
    }
}



