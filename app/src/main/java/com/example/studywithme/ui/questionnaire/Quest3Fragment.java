package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quest3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quest3Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<SessionTask> tasks;
    private ArrayList<String> taskList;
    private ArrayAdapter<String> tasksAdapter;
    private ListView listView;
    private Button submitTodoBtn;

    public Quest3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quest1.
     */
    // TODO: Rename and change types and number of parameters
    public static Quest3Fragment newInstance(String param1, String param2) {
        Quest3Fragment fragment = new Quest3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        taskList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quest3, container, false);
        listView = view.findViewById(R.id.toDoList);
        submitTodoBtn = view.findViewById(R.id.btnSubmit3);

        submitTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodo(view);
            }
        });

        //taskList = new ArrayList<>();
        tasksAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, taskList);
        listView.setAdapter(tasksAdapter);
        setUpListViewListener();

        return view;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                taskList.remove(position);
                tasksAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addTodo(View view) {
        EditText editQuest3 = view.findViewById(R.id.editQuest3);
        String todoText = editQuest3.getText().toString();

        if (!(todoText.equals(""))){
            tasksAdapter.add(todoText);
            editQuest3.setText("");
        } else {
            //TODO
        }
    }

}