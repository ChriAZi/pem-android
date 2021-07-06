package com.example.studywithme.ui.questionnaire;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.utils.Constants;

public class QuestCategoryFragment extends Fragment {

    public QuestCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_category, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_categories);
        backgroundImage.setImageResource(R.drawable.right_direction);
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView autoView = view.findViewById(R.id.ac_category);
        // Get the string array
        String[] categories = getResources().getStringArray(R.array.categories);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categories);
        autoView.setAdapter(adapter);

        autoView.setOnItemClickListener((parent, view1, position, id) -> {
            setSessionCategory(((String) parent.getItemAtPosition(position)));
        });

        return view;
    }

    private void setSessionCategory(String sessionCategory) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_CATEGORY, sessionCategory);
        editor.apply();
    }
}