package com.example.studywithme.ui.history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionCategory;
import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.viewmodels.SessionDetailViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.DateHelper;
import com.example.studywithme.utils.StringHelper;

import java.util.List;

/**
 * Class that contains the Details of SessionHistory Activity.
 * It extends the Navigation Activity to have the Bottom Navigation shown.
 * In the Activity, you can view session details such as the following:
 * + The ID of the session
 * + Which user connected to an instance through a session(Date)
 * + When the session began and ended(Duration)
 * + The status of the session
 * + Tasks (your Goals)
 * when the session loads, a list of Sessions will be loaded from the database.
 * when the list is displayed we will request whole Information for each session as itś loaded by the Recyclerview.
 */
public class SessionDetailActivity extends NavigationActivity {

    private SessionDetailViewModel sessionDetailViewModel;
    private RecyclerView tasksRecyclerView;
    private RecyclerView distractionsRecyclerView;
    private String sessionName;

    /**
     * Fetches the Extras as passed through the intent
     * Loads all relevant information from the backend to show the details of the requested Session
     *
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initViewModel();
        String sessionId = getIntent().getStringExtra(Constants.SESSION_ID);
        sessionName = getIntent().getStringExtra(Constants.SESSION_NAME);
        super.onCreate(savedInstanceState);
        setupActionBar();

        sessionDetailViewModel.getSession(sessionId).observe(this, session -> {
            /*
            Checks if the user requesting the view is the owner of a session and adapting all view content based on that information
            */
            boolean isOwner = User.getIdFromPreferences(this).equals(session.getOwner().getUid());

            TextView partner = findViewById(R.id.tv_detail_partner);
            if (isOwner && session.getPartner() != null) {
                partner.setText(session.getPartner().getName());
            } else if (!isOwner) {
                partner.setText(session.getOwner().getName());
            } else {
                partner.setText(R.string.private_session);
            }

            TextView feedbackContent = findViewById(R.id.tv_feedback_content);
            SessionCategory category;

            if (isOwner) {
                category = session.getOwnerSetting().getCategory();
                feedbackContent.setText(session.getOwnerReflection().getFeedback());
                setTasksRecyclerView(session.getOwnerSetting().getTasks());
                setDistractionsRecyclerView(session.getOwnerReflection().getDistractions());
            } else {
                category = session.getPartnerSetting().getCategory();
                feedbackContent.setText(session.getPartnerReflection().getFeedback());
                setTasksRecyclerView(session.getPartnerSetting().getTasks());
                setDistractionsRecyclerView(session.getPartnerReflection().getDistractions());
            }

            TextView date = findViewById(R.id.tv_detail_date);
            date.setText(DateHelper.formatDate(session.getStartedAt().toDate().getTime()));

            TextView duration = findViewById(R.id.tv_detail_duration);
            duration.setText(session.getDuration() + " Minuten");

            /*
            Sets stock image based on the SessionCategory
            */
            ImageView headerImage = findViewById(R.id.iv_header_image);
            switch (category) {
                case WORK:
                    headerImage.setImageResource(R.drawable.work_image);
                    break;
                case UNIVERSITY:
                    headerImage.setImageResource(R.drawable.university_image);
                    break;
                case HOBBY:
                    headerImage.setImageResource(R.drawable.hobby_image);
                    break;
            }

            TextView sessionCategory = findViewById(R.id.tv_detail_category);
            sessionCategory.setText(StringHelper.capitalize(category.name()));
        });
    }

    /**
     * initializes the SessionDetailViewModel
     */
    private void initViewModel() {
        sessionDetailViewModel = new ViewModelProvider(this).get(SessionDetailViewModel.class);
    }

    /**
     * Shows the ActionBar and enables the back arrow
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Handles the click on the back arrow
     *
     * @param item the back arrow in this case
     * @return indicating whether the the back arrow was selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent backIntent = new Intent(getApplicationContext(), SessionHistoryActivity.class);
        startActivityForResult(backIntent, 0);
        return true;
    }

    /**
     * Initializes the adapter for the RecyclerView containing the tasks of the requested Session
     *
     * @param tasks the list of tasks to be shown
     */
    private void setTasksRecyclerView(List<SessionTask> tasks) {
        SessionDetailTaskAdapter sessionDetailTaskAdapter = new SessionDetailTaskAdapter(tasks);
        tasksRecyclerView = findViewById(R.id.rv_tasks);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tasksRecyclerView.setAdapter(sessionDetailTaskAdapter);
    }

    /**
     * Initializes the adapter for the RecyclerView containing the distractions of the requested Session
     *
     * @param distractions the list of distractions to be shown
     */
    private void setDistractionsRecyclerView(List<String> distractions) {
        DistractionAdapter distractionsAdapter = new DistractionAdapter(distractions);
        distractionsRecyclerView = findViewById(R.id.rv_distractions);
        distractionsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        distractionsRecyclerView.setAdapter(distractionsAdapter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_session_detail;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_history;
    }

    @Override
    public String getActionBarTitle() {
        return sessionName;
    }
}
