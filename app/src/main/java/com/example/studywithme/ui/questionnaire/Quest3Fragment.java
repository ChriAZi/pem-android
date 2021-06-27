package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quest3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quest3Fragment extends Fragment {

    public interface Quest3FragmentListener {
        void getTasks(ArrayList<SessionTask> input);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<SessionTask> tasks;
    private ArrayList<SessionTask> taskList;
    private ArrayList<String> taskDesc;
    private ArrayAdapter<String> tasksAdapter;
    private ListView listView;
    private Button submitTodoBtn;
    private Quest3Fragment.Quest3FragmentListener listener;


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
        taskDesc = new ArrayList<>();
        taskList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quest3, container, false);
        listView = view.findViewById(R.id.toDoList);
        EditText editQuest3 = view.findViewById(R.id.editQuest3);
        submitTodoBtn = view.findViewById(R.id.btnSubmit3);

        submitTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodo(view);
                taskList.add(new SessionTask(editQuest3.getText().toString(), false));
                listener.getTasks(taskList);
            }
        });

        //taskList = new ArrayList<>();
        tasksAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, taskDesc);
        listView.setAdapter(tasksAdapter);
        setUpListViewListener();

        return view;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                taskList.remove(position);
                taskDesc.remove(position);
                tasksAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addTodo(View view) {
        EditText editQuest3 = view.findViewById(R.id.editQuest3);
        String todoText = editQuest3.getText().toString();

        if (!(todoText.equals(""))) {
            tasksAdapter.add(todoText);
            editQuest3.setText("");
        } else {
            //TODO
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Quest3Fragment.Quest3FragmentListener) {
            listener = (Quest3Fragment.Quest3FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}