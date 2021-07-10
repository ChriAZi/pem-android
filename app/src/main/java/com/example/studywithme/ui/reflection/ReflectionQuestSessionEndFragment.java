package com.example.studywithme.ui.reflection;

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
import com.example.studywithme.data.models.SessionReflection;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.history.SessionHistoryActivity;
import com.example.studywithme.ui.viewmodels.ReflectionViewModel;
import com.example.studywithme.utils.Constants;

import java.util.ArrayList;
import java.util.Set;

public class ReflectionQuestSessionEndFragment extends Fragment {

    public ReflectionQuestSessionEndFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reflection_quest_end_session, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_session_end);
        backgroundImage.setImageResource(R.drawable.finalize_session);

        Button submitReflectionButton = view.findViewById(R.id.bt_submit_reflection);
        submitReflectionButton.setOnClickListener(v -> {
            finalizeReflectionForSession();
        });

        return view;
    }

    private void finalizeReflectionForSession() {
        SessionReflection reflection = new SessionReflection(getFeedback(), getDistractions());

        ReflectionViewModel reflectionViewModel = new ViewModelProvider(this).get(ReflectionViewModel.class);
        reflectionViewModel.addReflection(User.getIdFromPreferences(getContext()), Session.getIdFromPreferences(getContext()), reflection).observe(getViewLifecycleOwner(), added -> {
            if (added) {
                startSessionHistoryActivity();
            }
        });
    }

    private void startSessionHistoryActivity() {
        Intent intent = new Intent(getActivity(), SessionHistoryActivity.class);
        startActivity(intent);
    }

    private String getFeedback() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(Constants.SESSION_QUEST_FEEDBACK, null);
    }

    private ArrayList<String> getDistractions() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> set = sp.getStringSet(Constants.SESSION_QUEST_DISTRACTIONS, null);
        return new ArrayList<>(set);
    }
}