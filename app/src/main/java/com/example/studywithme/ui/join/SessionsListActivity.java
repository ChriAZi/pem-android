package com.example.studywithme.ui.join;

import android.content.Intent;
import android.os.Bundle;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.MainActivity;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestionnaireActivity;
import com.example.studywithme.ui.timer.TimerActivity;
import com.example.studywithme.ui.viewmodels.AbstractViewModel;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.ToastMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studywithme.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SessionsListActivity extends NavigationActivity implements SessionListAdapter.ListItemClickListener {
    SessionListViewModel sessionListViewModel;
    ListView sessionList;
    private SessionListAdapter sessionAdapter;
    QuestionnaireViewModel questionnaireViewModel;
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
        getSupportActionBar().setTitle("Open Sessions");
        getCurrentUser();
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
        Intent i = new Intent(SessionsListActivity.this, QuestionnaireActivity.class);
        i.putExtra(Constants.USER, user);
        i.putExtra(Constants.SESSION_ID, Session.getIdFromPreferences(this));
        SessionsListActivity.this.startActivity(i);
    }

    private void getCurrentUser() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            user = null;
        } else {
            user = (User) extras.get(Constants.USER);
            Log.d(TAG, String.valueOf(user));

        }
    }

    private void checkIfSessionsExist(){
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getPublicSessions().observe(this, sessions -> {
            if(sessions.size() == 0) {
                hint.setText("No open sessions yet.\n Start your own with the Button below.");
            }});
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
     * Lets the user join the selected session
     * @param position of the session on which the user has clicked
     */
    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, mSession[position].toString(), Toast.LENGTH_SHORT).show();
        questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        questionnaireViewModel.joinSession(mSession[position].getUid(), User.getIdFromPreferences(SessionsListActivity.this), mSession[position].getOwnerSetting()).observe(SessionsListActivity.this, joined -> {
            if(mSession[position].isActive()){
                if(mSession[position].getOwner().getName().equals(user.getName())) {
                    ToastMaster.showToast(SessionsListActivity.this, "You can not join your own session");
                } else {
                    if (joined) {
                        ToastMaster.showToast(SessionsListActivity.this, "Joined Session" + mSession[position].getUid() + " by " + mSession[position].getOwner());
                        mSession[position].setPartner(user);
                    }
                }}
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

}
