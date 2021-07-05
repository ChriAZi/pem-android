package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quest4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quest4Fragment extends Fragment {

    public interface Quest4FragmentListener {
        void getDuration(int input);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText editQuest4;
    private Quest4Fragment.Quest4FragmentListener listener;

    public Quest4Fragment() {
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
    public static Quest4Fragment newInstance(String param1, String param2) {
        Quest4Fragment fragment = new Quest4Fragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_quest4, container, false);

        ImageView backgroundImage = view.findViewById(R.id.iv_duration);
        backgroundImage.setImageResource(R.drawable.season_change);
        editQuest4 = view.findViewById(R.id.editQuest4);
        editQuest4.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int dur = 0;
                if(editQuest4.getText().toString()!=""){
                    dur = Integer.parseInt(editQuest4.getText().toString());
                } else {
                    dur = 0;
                }
                listener.getDuration(dur);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Quest4Fragment.Quest4FragmentListener) {
            listener = (Quest4Fragment.Quest4FragmentListener) context;
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