package com.example.studywithme.ui.questionnaire;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionCategory;
import com.example.studywithme.utils.Constants;

/**
 * This fragment handles the question about the worked for category.
 * It provides a method to inform the SharedPreferences about the input.
 * The input is one of the predefined SessionCategories.
 */
public class QuestCategoryFragment extends Fragment {

    public QuestCategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The created view holds a radio group which provides the options for the category input.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_category, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_categories);
        backgroundImage.setImageResource(R.drawable.right_direction);

        RadioGroup radioGroup = view.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_uni:
                    setSessionCategory(SessionCategory.UNIVERSITY.name());
                    break;
                case R.id.radio_work:
                    setSessionCategory(SessionCategory.WORK.name());
                    break;
                case R.id.radio_hobby:
                    setSessionCategory(SessionCategory.HOBBY.name());
                    break;
                default:
                    setSessionCategory(SessionCategory.UNIVERSITY.name());
                    break;
            }
        });

        return view;
    }

    /**
     * Informs the SharedPreferences about the picked category.
     * @param sessionCategory
     */
    private void setSessionCategory(String sessionCategory) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_CATEGORY, sessionCategory);
        editor.apply();
    }
}