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

/**
 * This fragment handles the question about starting a private or starting/joining a private session.
 * You can choose the according button.
 * It provides several methods to get the prior given inputs from SharedPreferences.
 * It also provides a method to start a session with all existing information.
 */
public class QuestSessionStartFragment extends Fragment {

    private final boolean joining;
    private QuestionnaireViewModel questionnaireViewModel;

    public QuestSessionStartFragment(boolean joining) {
        this.joining = joining;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The created view holds the buttons for either joining/creating a public session or starting a private one.
     * Depending on which session option (starting/joining) the user chose in the beginning, buttons are set to in/visible.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_start_session, container, false);

        initViewModel();

        ImageView backgroundImage = view.findViewById(R.id.iv_public);
        backgroundImage.setImageResource(R.drawable.work_time);

        Button submitPublicButton = view.findViewById(R.id.bt_submit_public);
        Button submitPrivateButton = view.findViewById(R.id.bt_submit_private);
        Button submitJoiningButton = view.findViewById(R.id.bt_submit_joining);

        submitPublicButton.setOnClickListener(v -> startSessionForTimer(true, joining));
        submitPrivateButton.setOnClickListener(v -> startSessionForTimer(false, joining));
        submitJoiningButton.setOnClickListener(v -> startSessionForTimer(false, joining));

        if (joining) {
            submitPublicButton.setVisibility(View.GONE);
            submitPrivateButton.setVisibility(View.GONE);
            submitJoiningButton.setVisibility(View.VISIBLE);
        } else {
            submitPublicButton.setVisibility(View.VISIBLE);
            submitPrivateButton.setVisibility(View.VISIBLE);
            submitJoiningButton.setVisibility(View.GONE);
        }
        return view;
    }

    /**
     * Contacts the QuestionnaireViewModel
     */
    private void initViewModel() {
        questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
    }

    /**
     * Starts a new session for the user.
     * First the SessionSetting object is built by calling the Getters from the SharedPreferences.
     * Depending on his decision about a public/joint or private Session, the ViewModel calls the according function
     * It then starts the TimerActivity.
     * @param isPublic
     * @param joining
     */
    private void startSessionForTimer(boolean isPublic, boolean joining) {
        String userId = User.getIdFromPreferences(getContext());
        SessionSetting setting = new SessionSetting(getSessionName(), getSessionGoal(), getSessionCategory() != null ? getSessionCategory() : SessionCategory.UNIVERSITY, getSessionTasks());
        if (joining) {
            String sessionId = Session.getIdFromPreferences(getContext());
            questionnaireViewModel.joinSession(sessionId, userId, setting).observe(getViewLifecycleOwner(), joined -> {
                if (joined) {
                    Session.setIdInPreferences(getContext(), sessionId);
                    resetPreferences();
                    startTimerActivity();
                }
            });
        } else {
            Session session = new Session(getSessionDuration(), isPublic, setting);
            questionnaireViewModel.startSession(userId, session).observe(getViewLifecycleOwner(), sessionId -> {
                if (sessionId != null) {
                    Session.setIdInPreferences(getContext(), sessionId);
                    resetPreferences();
                    startTimerActivity();
                }
            });
        }
    }

    /**
     * Resets the SharedPreferences, as all information is already passed on.
     */
    private void resetPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_NAME, null);
        editor.putString(Constants.SESSION_QUEST_GOAL, null);
        editor.putString(Constants.SESSION_QUEST_CATEGORY, null);
        editor.putString(Constants.SESSION_QUEST_TASKS, null);
        editor.putString(Constants.SESSION_QUEST_DURATION, null);
        editor.apply();
    }

    /**
     * Starts TimerActivity.
     */
    private void startTimerActivity() {
        Intent intent = new Intent(getActivity(), TimerActivity.class);
        startActivity(intent);
    }

    /**
     * Returns Name form SharedPreferences.
     * @return
     */
    private String getSessionName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(Constants.SESSION_QUEST_NAME, null);
    }

    /**
     * Returns Goal form SharedPreferences.
     * @return
     */
    private String getSessionGoal() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(Constants.SESSION_QUEST_GOAL, null);
    }

    /**
     * Returns Category form SharedPreferences.
     * @return
     */
    private SessionCategory getSessionCategory() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sessionCategory = sp.getString(Constants.SESSION_QUEST_CATEGORY, null);
        if (sessionCategory != null) {
            return SessionCategory.valueOf(sessionCategory);
        } else {
            return SessionCategory.HOBBY;
        }
    }

    /**
     * Returns TaskList form SharedPreferences.
     * @return
     */
    private List<SessionTask> getSessionTasks() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> set = sp.getStringSet(Constants.SESSION_QUEST_TASKS, null);
        ArrayList<SessionTask> tasks = new ArrayList<>();
        set.forEach(item -> {
            tasks.add(new SessionTask(item, false));
        });
        return tasks;
    }

    /**
     * Returns Duration form SharedPreferences.
     * @return
     */
    private int getSessionDuration() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getInt(Constants.SESSION_QUEST_DURATION, 0);
    }
}



