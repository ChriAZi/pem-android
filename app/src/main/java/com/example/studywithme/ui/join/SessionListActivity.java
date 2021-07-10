package com.example.studywithme.ui.join;

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
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.utils.Constants;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SessionListActivity extends NavigationActivity implements SessionListAdapter.ItemViewHolder.OnItemClickListener {
    SessionListViewModel sessionListViewModel;
    private RecyclerView recyclerView;
    private ImageView backgroundImage;
    private TextView hint;
    private List<Session> sessions = new ArrayList<>();
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUserIdFromIntent();
        setupViews();
        initViewModel();
        loadOpenSessions();

        ExtendedFloatingActionButton createSessionButton = findViewById(R.id.bt_list_create_session);
        createSessionButton.setOnClickListener(view -> startQuestionnaireActivity(false));
    }

    private void setUserIdFromIntent() {
        if (getIntent().hasExtra(Constants.USER_ID)) {
            userId = (String) getIntent().getSerializableExtra(Constants.USER_ID);
            User.setIdInPreferences(userId, this);
        }
        userId = User.getIdFromPreferences(this);
    }

    private void setupViews() {
        backgroundImage = findViewById(R.id.iv_list_studying);
        backgroundImage.setImageResource(R.drawable.studying);
        recyclerView = findViewById(R.id.rv_session_list);
        hint = findViewById(R.id.tv_list_no_sessions);
    }

    private void initViewModel() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
    }

    private void loadOpenSessions() {
        sessionListViewModel.getPublicSessions(User.getIdFromPreferences(this)).observe(this, sessions -> {
            if (sessions.isEmpty()) {
                hint.setVisibility(View.VISIBLE);
                backgroundImage.setVisibility(View.VISIBLE);
            } else {
                hint.setVisibility(View.GONE);
                backgroundImage.setVisibility(View.GONE);
                SessionListAdapter sessionAdapter = new SessionListAdapter(sessions, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(sessionAdapter);
                this.sessions = sessions;
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Session.setIdInPreferences(this, sessions.get(0).getUid());
        startQuestionnaireActivity(true);
    }

    private void startQuestionnaireActivity(boolean joining) {
        Intent i = new Intent(SessionListActivity.this, QuestActivity.class);
        i.putExtra(Constants.JOINING, joining);
        SessionListActivity.this.startActivity(i);
    }

    @Override
    public int getContentViewId() {
        return R.layout.session_join_list;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_session_list);
    }

}
