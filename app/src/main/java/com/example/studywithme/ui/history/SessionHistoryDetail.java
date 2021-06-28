package com.example.studywithme.ui.history;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studywithme.R;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;

public class SessionHistoryDetail  extends AppCompatActivity {

    private SessionHistoryViewModel sessionHistoryViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessionhistory_detail);

        initViewModel();

    }

    private void initViewModel() {
        String userId = User.getIdFromPreferences(this);
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);

    }
}
