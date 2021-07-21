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

/**
 * Class that contains the SessionHistory Activity.
 * This activity contains snapshots of active cloud Firestore Database.
 * It extends the Navigation Activity to have the Bottom Navigation shown.
 * You can view session details in SessionDetails Activity.
 */
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

    /**
     * Initializes all Views
     */
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

    /**
     * initializes the SessionHistoryViewModel and adapting the visibility of the list based on the content of the Session List
     */
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
                SessionHistoryAdapter adapter = new SessionHistoryAdapter(sessions, this, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    /**
     * starts the QuestActivity
     *
     * @see QuestActivity
     */
    private void startQuestionnaireActivity() {
        Intent i = new Intent(SessionHistoryActivity.this, QuestActivity.class);
        this.startActivity(i);
    }

    /**
     * handles the click on a single session in the RecyclerView
     *
     * @param position the position of the clicked session in the list
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, SessionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SESSION_ID, sessions.get(position).getUid());
        if (User.getIdFromPreferences(this).equals(sessions.get(position).getOwner().getUid())) {
            bundle.putString(Constants.SESSION_NAME, sessions.get(position).getOwnerSetting().getName());
        } else {
            bundle.putString(Constants.SESSION_NAME, sessions.get(position).getPartnerSetting().getName());
        }
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
