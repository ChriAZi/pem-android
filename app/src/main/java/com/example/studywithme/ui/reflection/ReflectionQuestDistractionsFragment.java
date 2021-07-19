package com.example.studywithme.ui.reflection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This fragment handles the question about the distractions.
 * It provides a method to inform the SharedPreferences about the input.
 * The input can be a Text and will be added to a distraction list.
 */
public class ReflectionQuestDistractionsFragment extends Fragment {

    private ArrayList<String> distractionList = new ArrayList<>();
    private ArrayAdapter<String> distractionsAdapter;
    private ListView listView;
    private ReflectionQuestActivity parentActivity;


    public ReflectionQuestDistractionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The created view holds an Input field for text to enter a distraction,
     * a button to submit the distraction and a list that shows your given inputs.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reflection_quest_distractions, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_distractions);
        backgroundImage.setImageResource(R.drawable.starry_window);

        listView = view.findViewById(R.id.lv_distractions);
        distractionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, distractionList);
        listView.setAdapter(distractionsAdapter);

        EditText editTextTasks = view.findViewById(R.id.et_distractions);

        parentActivity = (ReflectionQuestActivity) getActivity();

        Button submitTasksButton = view.findViewById(R.id.bt_submit_distraction);
        submitTasksButton.setOnClickListener(v -> {
            String taskDescription = editTextTasks.getText().toString();
            addDistractionToListView(taskDescription, parentActivity);
            editTextTasks.setText("");
            setDistractions(distractionList);
        });
        setUpListViewListener(parentActivity);
        return view;
    }

    /**
     * Listens for the state of the list and sets/removes the distractions once one is added/removed.
     * @param parentActivity
     */
    private void setUpListViewListener(ReflectionQuestActivity parentActivity) {
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            parentActivity.setDistractionAdded(false);
            distractionList.remove(position);
            distractionsAdapter.notifyDataSetChanged();
            return true;
        });
    }

    /**
     * Adds a entered Distraction to the list with help of the List Adapter
     * @param distraction
     * @param parentActivity
     */
    private void addDistractionToListView(String distraction, ReflectionQuestActivity parentActivity) {
        if (!(distraction.equals(""))) {
            parentActivity.setDistractionAdded(true);
            distractionsAdapter.add(distraction);
        }
    }

    /**
     * Informs the SharedPreferences about the distraction list.
     * @param distractions
     */
    private void setDistractions(List<String> distractions) {
        Set<String> distractionsSet = new HashSet<>(distractions);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(Constants.SESSION_QUEST_DISTRACTIONS, distractionsSet);
        editor.apply();
    }
}