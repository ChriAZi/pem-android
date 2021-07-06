package com.example.studywithme.ui.reflection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RQuestDistractionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RQuestDistractionsFragment extends Fragment {

    public interface RQuestDistractionsFragmentListener {
        void getDistractions(ArrayList<String> input);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> distList;
    private ArrayAdapter<String> distAdapter;
    private ListView listView;
    private Button submitDistBtn;
    private RQuestDistractionsFragment.RQuestDistractionsFragmentListener listener;


    public RQuestDistractionsFragment() {
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
    public static RQuestDistractionsFragment newInstance(String param1, String param2) {
        RQuestDistractionsFragment fragment = new RQuestDistractionsFragment();
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
        distList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reflection_quest_distractions, container, false);
        ImageView backgroundImage = view.findViewById(R.id.iv_distractions);
        backgroundImage.setImageResource(R.drawable.starry_window);
        listView = view.findViewById(R.id.distList);
        EditText editQuest3 = view.findViewById(R.id.reditQuestDistractions);
        submitDistBtn = view.findViewById(R.id.btnSubmitDist);

        submitDistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDist(view);
                distList.add(editQuest3.getText().toString());
                listener.getDistractions(distList);
            }
        });

        distAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, distList);
        listView.setAdapter(distAdapter);
        setUpListViewListener();

        return view;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                distList.remove(position);
                distAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addDist(View view) {
        EditText editQuest3 = view.findViewById(R.id.reditQuestDistractions);
        String distText = editQuest3.getText().toString();

        if (!(distText.equals(""))) {
            distAdapter.add(distText);
            editQuest3.setText("");
        } else {
            //TODO
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RQuestDistractionsFragment.RQuestDistractionsFragmentListener) {
            listener = (RQuestDistractionsFragment.RQuestDistractionsFragmentListener) context;
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