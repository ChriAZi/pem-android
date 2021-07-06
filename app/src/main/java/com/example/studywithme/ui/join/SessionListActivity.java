package com.example.studywithme.ui.join;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.ui.viewmodels.TimerViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.ToastMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SessionListActivity extends NavigationActivity implements SessionListAdapter.ListItemClickListener {
    SessionListViewModel sessionListViewModel;
    ListView sessionList;
    private SessionListAdapter sessionAdapter;
    QuestionnaireViewModel questionnaireViewModel;
    TimerViewModel timerViewModel;
    private RecyclerView recyclerView;
    private TextView hint;
    private FloatingActionButton createSession;
    private final String TAG = "SessionListActivity";
    private Session[] mSession;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.rv_session_list);
        hint = findViewById(R.id.hint1);
        createSession = findViewById(R.id.create_start_questionaire);
        ImageView backgroundImage = findViewById(R.id.iv_join);
        backgroundImage.setImageResource(R.drawable.studying);

        getSupportActionBar().setTitle("Open Sessions");
        initCurrentUser();
        initViewModel();
        checkIfSessionsExist();

        createSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestionaireActivity();
            }
        });

    }

    private void startQuestionaireActivity() {
        Intent i = new Intent(SessionListActivity.this, QuestActivity.class);
        i.putExtra(Constants.USER, user);
        i.putExtra(Constants.SESSION_ID, Session.getIdFromPreferences(this));
        SessionListActivity.this.startActivity(i);
    }

    private void initCurrentUser() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getCurrentUser(User.getIdFromPreferences(this)).observe(this, userCurr -> {
            user = userCurr;
        });
    }

    private void checkIfSessionsExist() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getPublicSessions().observe(this, sessions -> {
            if (sessions.size() == 0) {
                hint.setText("No open sessions yet.\n Start your own with the Button below.");
            }
        });
    }

    private void initViewModel() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getPublicSessions().observe(this, sessions -> {
            sessionAdapter = new SessionListAdapter(sessions, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(sessionAdapter);
            checkIfSessionsExist();
            hint.setVisibility(View.INVISIBLE);
            mSession = new Session[sessions.size()];
            for (int i = 0; i < sessions.size(); i++) {
                mSession[i] = sessions.get(i);
            }

        });
    }

    /**
     * ends the session
     */
    public void endSession() {
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.endSession(Session.getIdFromPreferences(this)).observe(this, finished -> {
            ToastMaster.showToast(SessionListActivity.this, "Session ended");
        });
    }

    /**
     * Lets the user join the selected session
     *
     * @param position of the session on which the user has clicked
     */
    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, mSession[position].toString(), Toast.LENGTH_SHORT).show();
        questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        questionnaireViewModel.joinSession(mSession[position].getUid(), User.getIdFromPreferences(SessionListActivity.this), mSession[position].getOwnerSetting()).observe(SessionListActivity.this, joined -> {
            if (mSession[position].isActive()) {
                if (mSession[position].getOwner().getName().equals(user.getName())) {
                    ToastMaster.showToast(SessionListActivity.this, "You can not join your own session");
                } else {
                    if (joined) {
                        ToastMaster.showToast(SessionListActivity.this, "Joined Session" + mSession[position].getUid() + " by " + mSession[position].getOwner());
                        mSession[position].setPartner(user);
                    }
                }
            }
        });
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
