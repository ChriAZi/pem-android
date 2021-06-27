package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionTask;

/**
z * Use the {@link QuestPublicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestPublicFragment extends Fragment {

    public interface QuestPublicFragmentListener {
        void getPublic(boolean input);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private QuestPublicFragmentListener listener;
    public Button submitPublic, submitPrivate;

    public QuestPublicFragment() {
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
    public static QuestPublicFragment newInstance(String param1, String param2) {
        QuestPublicFragment fragment = new QuestPublicFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quest_public, container, false);
        //editQuestPublic = view.findViewById(R.id.editQuestPublic);

        submitPublic = view.findViewById(R.id.btnSubmitPublic);
        submitPrivate = view.findViewById(R.id.btnSubmitPrivate);

        submitPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getPublic(true);
            }
        });

        submitPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getPublic(false);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestPublicFragmentListener) {
            listener = (QuestPublicFragmentListener) context;
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



