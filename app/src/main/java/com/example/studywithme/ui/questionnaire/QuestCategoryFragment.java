package com.example.studywithme.ui.questionnaire;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

        RadioGroup radioGroup = view.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_uni:
                        setSessionCategory("UNIVERSITY");
                        break;
                    case R.id.radio_work:
                        setSessionCategory("WORK");
                        break;
                    case R.id.radio_hobby:
                        setSessionCategory("HOBBY");
                        break;
                    default:
                        setSessionCategory("HOBBY");
                        break;
                }
            }
        });

        return view;
    }

    private void setSessionCategory(String sessionCategory) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_QUEST_CATEGORY, sessionCategory);
        editor.apply();
    }

    /*public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_uni:
                if (checked)
                    Log.d("holaholahola", "onRadioButtonClicked: ");
                    Toast.makeText(getActivity(), "Title confirmed", Toast.LENGTH_LONG).show();
                break;
            case R.id.radio_work:
                if (checked)
                    break;
            case R.id.radio_hobby:
                if (checked)
                    break;
        }
    }*/
}