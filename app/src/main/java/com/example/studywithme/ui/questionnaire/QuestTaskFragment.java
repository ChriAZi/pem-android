package com.example.studywithme.ui.questionnaire;

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

public class QuestTaskFragment extends Fragment {

    private ArrayList<String> taskList = new ArrayList<>();
    private ArrayAdapter<String> tasksAdapter;
    private ListView listView;


    public QuestTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_session_tasks, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_subtasks);
        backgroundImage.setImageResource(R.drawable.choose_re);

        listView = view.findViewById(R.id.lv_tasks);
        tasksAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, taskList);
        listView.setAdapter(tasksAdapter);

        EditText editTextTasks = view.findViewById(R.id.et_tasks);

        Button submitTasksButton = view.findViewById(R.id.bt_submit_tasks);
        submitTasksButton.setOnClickListener(v -> {
            String taskDescription = editTextTasks.getText().toString();
            addTodoToListView(taskDescription);
            editTextTasks.setText("");
            taskList.add(taskDescription);
            setSessionTasks(taskList);
        });

        setUpListViewListener();

        return view;
    }

    private void addTodoToListView(String description) {
        if (!(description.equals(""))) {
            tasksAdapter.add(description);
        }
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            taskList.remove(position);
            tasksAdapter.notifyDataSetChanged();
            return true;
        });
    }

    private void setSessionTasks(List<String> tasks) {
        Set<String> tasksSet = new HashSet<>(tasks);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(Constants.SESSION_QUEST_TASKS, tasksSet);
        editor.apply();
    }

}