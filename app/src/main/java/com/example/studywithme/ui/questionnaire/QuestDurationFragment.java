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

public class QuestDurationFragment extends Fragment {

    private EditText editDuration;

    public QuestDurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_duration, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_duration);
        backgroundImage.setImageResource(R.drawable.season_change);

        editDuration = view.findViewById(R.id.et_duration);
        editDuration.addTextChangedListener(new QuestionDurationTextWatcher());

        return view;
    }

    private void setSessionDuration(int sessionDuration) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constants.SESSION_QUEST_DURATION, sessionDuration);
        editor.apply();
    }

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