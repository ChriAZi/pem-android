package com.example.studywithme.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestActivity;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.example.studywithme.utils.Constants;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class SessionHistoryActivity extends NavigationActivity implements SessionHistoryAdapter.ItemViewHolder.OnItemClickListener {
    private SessionHistoryViewModel sessionHistoryViewModel;
    private List<Session> sessions;
    private RecyclerView recyclerView;
    private TextView hint;
    private ImageView backgroundImage;
    private ExtendedFloatingActionButton createSessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        initViewModel();
    }

    private void setupViews() {
        backgroundImage = findViewById(R.id.iv_history_studying);
        backgroundImage.setImageResource(R.drawable.studying);
        hint = findViewById(R.id.tv_history_no_sessions);

        recyclerView = findViewById(R.id.rv_session_list);

        createSessionButton = findViewById(R.id.bt_history_create_session);
        createSessionButton.setOnClickListener(view -> {
            startQuestionnaireActivity();
        });
    }

    private void initViewModel() {
        String userId = User.getIdFromPreferences(this);
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getPastSessions(userId).observe(this, sessions -> {
            if (sessions.isEmpty()) {
                hint.setVisibility(View.VISIBLE);
                backgroundImage.setVisibility(View.VISIBLE);
                createSessionButton.setVisibility(View.VISIBLE);
            } else {
                hint.setVisibility(View.GONE);
                backgroundImage.setVisibility(View.GONE);
                createSessionButton.setVisibility(View.GONE);
                this.sessions = sessions;
                SessionHistoryAdapter adapter = new SessionHistoryAdapter(sessions, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void startQuestionnaireActivity() {
        Intent i = new Intent(SessionHistoryActivity.this, QuestActivity.class);
        this.startActivity(i);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SessionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SESSION_ID, sessions.get(0).getUid());
        bundle.putString(Constants.SESSION_NAME, sessions.get(0).getOwnerSetting().getName());
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

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_history);
    }
}
