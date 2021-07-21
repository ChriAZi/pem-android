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

/**
 * Activity that shows all the currently open sessions from other users
 * extends the Navigation and implements the Adapter for the Session List
 */
public class SessionListActivity extends NavigationActivity implements SessionListAdapter.ItemViewHolder.OnItemClickListener {
    SessionListViewModel sessionListViewModel;
    private RecyclerView recyclerView;
    private ImageView backgroundImage;
    private TextView hint;
    private List<Session> sessions = new ArrayList<>();
    private String userId;

    /**
     * When this activity is created, this method
     * - calls the functions to load the current user,the views and the active sessions
     * - calls the function that fetches the viewmodel
     * @param savedInstanceState
     */
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

    /**
     * gets the currently logged in user from the intent (given from AuthActivity) and saves the user ID to the preferences
     */
    private void setUserIdFromIntent() {
        if (getIntent().hasExtra(Constants.USER_ID)) {
            userId = (String) getIntent().getSerializableExtra(Constants.USER_ID);
            User.setIdInPreferences(userId, this);
        }
        userId = User.getIdFromPreferences(this);
    }

    /**
     * sets all the view & layout items
     */
    private void setupViews() {
        backgroundImage = findViewById(R.id.iv_list_studying);
        backgroundImage.setImageResource(R.drawable.studying);
        recyclerView = findViewById(R.id.rv_session_list);
        hint = findViewById(R.id.tv_list_no_sessions);
    }

    /**
     * fetches the viewmodel from the sessionlistviewmodel class
     */
    private void initViewModel() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
    }

    /**
     * observes all public sessions from firebase and displays them in the recycler view
     */
    private void loadOpenSessions() {
        sessionListViewModel.getPublicSessions(User.getIdFromPreferences(this)).observe(this, sessions -> {
            if (sessions.isEmpty()) {   //if there are no open sessions
                hint.setVisibility(View.VISIBLE);   // show hint
                backgroundImage.setVisibility(View.VISIBLE); //show background image
            } else {
                hint.setVisibility(View.GONE);
                backgroundImage.setVisibility(View.GONE);
                SessionListAdapter sessionAdapter = new SessionListAdapter(sessions, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(sessionAdapter); //set adapter
                this.sessions = sessions;
            }
        });
    }

    /**
     * starts the Questionaire Activity for the clicked session (sets the clicked session in the preferences as current session)
     * @param position of clicked item
     */
    @Override
    public void onItemClick(int position) {
        Session.setIdInPreferences(this, sessions.get(position).getUid());
        startQuestionnaireActivity(true);
    }

    /**
     * starts the Questionaire Activity
     * @param joining to report to the other user that the partner is currently joining the session
     */
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
