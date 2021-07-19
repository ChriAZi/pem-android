package com.example.studywithme.ui.reflection;

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
 * This fragment handles the question about the feedback.
 * It provides a method to inform the SharedPreferences about the input.
 * The input can be a text.
 */
public class ReflectionQuestFeedbackFragment extends Fragment {

    public ReflectionQuestFeedbackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The created view holds an EditText for text to enter the feedback.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reflection_quest_feedback, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_feedback);
        backgroundImage.setImageResource(R.drawable.celebration);

        EditText editQuestName = view.findViewById(R.id.et_feedback);
        editQuestName.addTextChangedListener(new ReflectionFeedbackTextWatcher());

        return view;
    }

    /**
     * Informs the SharedPreferences about the entered feedback.
     * @param feedback
     */
    private void setReflectionFeedback(String feedback) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_FEEDBACK, feedback);
        editor.apply();
    }

    /**
     * This class watches the state of the input field and sets the feedback once it is changed.
     */
    private class ReflectionFeedbackTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setReflectionFeedback(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}



