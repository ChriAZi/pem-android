package com.example.studywithme.ui.questionnaire;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.utils.Constants;

/**
 * This fragment handles the question about the goal.
 * It provides a method to inform the SharedPreferences about the input.
 * The input can be a text.
 */
public class QuestGoalFragment extends Fragment {

    public QuestGoalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The created view holds an EditText for text to enter the goal.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_goal, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_goals);
        backgroundImage.setImageResource(R.drawable.counting_stars);

        EditText editSessionGoal = view.findViewById(R.id.et_goal);
        editSessionGoal.addTextChangedListener(new QuestionGoalTextWatcher());

        return view;
    }

    /**
     * Informs the SharedPreferences about the entered goal.
     * @param sessionGoal
     */
    private void setSessionGoal(String sessionGoal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_GOAL, sessionGoal);
        editor.apply();
    }

    /**
     * This class watches the state of the input field and sets the goal once it is changed.
     */
    private class QuestionGoalTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setSessionGoal(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

}