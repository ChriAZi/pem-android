package com.example.studywithme.ui.join;

import android.os.Bundle;

import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.SessionListViewModel;
import com.example.studywithme.utils.ToastMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ListView;
import android.widget.Toast;

import com.example.studywithme.R;

public class SessionsListActivity extends AppCompatActivity implements SessionListAdapter.ListItemClickListener {
    SessionListViewModel sessionListViewModel;
    ListView sessionList;
    private SessionListAdapter sessionAdapter;
    QuestionnaireViewModel questionnaireViewModel;
    private RecyclerView recyclerView;
    private String TAG = "SessionListActivity";
    private Session[] mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_sessions_list);
        setContentView(R.layout.activity_session_history);
        recyclerView = findViewById(R.id.rv_session_list);
        //sessionList = (ListView)findViewById(R.id.listView);
        initViewModel();
    }

    private void initViewModel() {
        sessionListViewModel = new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getPublicSessions().observe(this, sessions -> {
        sessionAdapter = new SessionListAdapter(sessions, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(sessionAdapter);
            mSession = new Session[sessions.size()];
            for(int i = 0; i < sessions.size(); i++){
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
            if (joined) {
                ToastMaster.showToast(SessionsListActivity.this, "Joined Session" + mSession[position].getUid() + " by " + mSession[position].getOwner());
            }}/* else {
                ToastMaster.showToast(SessionsListActivity.this, "Something went wrong with joining the session.");
            }*/
        });
    }}