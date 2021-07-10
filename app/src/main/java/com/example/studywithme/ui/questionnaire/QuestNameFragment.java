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

public class QuestNameFragment extends Fragment {

    EditText editQuestName;
    Boolean validated;
    protected View nameView;

    public QuestNameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_name, container, false);
        this.nameView = view;

        ImageView backgroundImage = view.findViewById(R.id.iv_name);
        backgroundImage.setImageResource(R.drawable.startup_life);

        editQuestName = view.findViewById(R.id.et_name);
        editQuestName.addTextChangedListener(new QuestionNameTextWatcher());

        return view;
    }

    private void setSessionName(String sessionName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_NAME, sessionName);
        editor.apply();
    }

    private class QuestionNameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setSessionName(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}



