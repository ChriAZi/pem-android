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
 * This fragment handles the question about the duration.
 * It provides a method to inform the SharedPreferences about the input.
 * The input can be an Integer and stands for the duration in minutes.
 */
public class QuestDurationFragment extends Fragment {

    private EditText editDuration;

    public QuestDurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The created view holds an EditText for numbers to enter a duration in minutes.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_duration, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_duration);
        backgroundImage.setImageResource(R.drawable.season_change);

        editDuration = view.findViewById(R.id.et_duration);
        editDuration.addTextChangedListener(new QuestionDurationTextWatcher());

        return view;
    }

    /**
     * Informs the SharedPreferences about the entered duration in minutes.
     * @param sessionDuration
     */
    private void setSessionDuration(int sessionDuration) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constants.SESSION_QUEST_DURATION, sessionDuration);
        editor.apply();
    }

    /**
     * This class watches the state of the input field and saves the input in a variable once it is changed.
     */
    private class QuestionDurationTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int duration = 0;
            if (!editDuration.getText().toString().equals("")) {
                duration = Integer.parseInt(editDuration.getText().toString());
            }
            setSessionDuration(duration);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}