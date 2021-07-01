package com.example.studywithme.ui.history;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.example.studywithme.utils.Constants;

import java.util.List;

public class SessionHistoryActivity extends NavigationActivity implements SessionHistoryAdapter.ItemViewHolder.OnItemClickListener {
    private SessionHistoryViewModel sessionHistoryViewModel;
    private RecyclerView recyclerView;
    private List<Session> sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.rv_session_list);
        initViewModel();
    }

    private void initViewModel() {
        String userId = User.getIdFromPreferences(this);
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getPastSessions(userId).observe(this, sessions -> {
            this.sessions = sessions;
            SessionHistoryAdapter adapter = new SessionHistoryAdapter(sessions, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SessionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SESSION_ID, sessions.get(0).getUid());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_session_history;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_history;
    }
}
