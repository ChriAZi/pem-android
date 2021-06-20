package com.example.studywithme.ui.history;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;

public class SessionHistoryActivity extends AppCompatActivity {
    private SessionHistoryViewModel sessionHistoryViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_list);
        recyclerView = findViewById(R.id.rv_session_list);
        initViewModel();
    }

    private void initViewModel() {
        String userId = User.getIdFromPreferences(this);
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getPastSessions(userId).observe(this, sessions -> {
            SessionHistoryAdapter adapter = new SessionHistoryAdapter(sessions);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(adapter);
        });
    }
}
