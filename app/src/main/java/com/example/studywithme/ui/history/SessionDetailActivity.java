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
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.viewmodels.SessionDetailViewModel;
import com.example.studywithme.utils.Constants;
import com.example.studywithme.utils.DateHelper;
import com.example.studywithme.utils.StringHelper;

import java.util.List;

public class SessionDetailActivity extends NavigationActivity {

    private SessionDetailViewModel sessionDetailViewModel;
    private RecyclerView tasksRecyclerView;
    private RecyclerView distractionsRecyclerView;
    private String sessionName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initViewModel();
        String sessionId = getIntent().getStringExtra(Constants.SESSION_ID);
        sessionName = getIntent().getStringExtra(Constants.SESSION_NAME);
        super.onCreate(savedInstanceState);
        setupActionBar();

        sessionDetailViewModel.getSession(sessionId).observe(this, session -> {
            TextView date = findViewById(R.id.tv_detail_date);
            date.setText(DateHelper.formatDate(session.getStartedAt().toDate().getTime()));

            SessionCategory category = session.getOwnerSetting().getCategories().get(0);
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

            TextView duration = findViewById(R.id.tv_detail_duration);
            duration.setText(session.getDuration() + " Minuten");

            TextView partner = findViewById(R.id.tv_detail_partner);
            partner.setText(session.getPartner().getName());

            TextView sessionCategory = findViewById(R.id.tv_detail_category);
            sessionCategory.setText(StringHelper.capitalize(session.getOwnerSetting().getCategories().get(0).name()));

            TextView feedbackContent = findViewById(R.id.tv_feedback_content);
            feedbackContent.setText(session.getOwnerReflection().getFeedback());

            setTasksRecyclerView(session.getOwnerSetting().getTasks());
            setDistractionsRecyclerView(session.getOwnerReflection().getDistractions());
        });
    }

    private void initViewModel() {
        sessionDetailViewModel = new ViewModelProvider(this).get(SessionDetailViewModel.class);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent backIntent = new Intent(getApplicationContext(), SessionHistoryActivity.class);
        startActivityForResult(backIntent, 0);
        return true;
    }

    private void setTasksRecyclerView(List<SessionTask> tasks) {
        TaskAdapter taskAdapter = new TaskAdapter(tasks);
        tasksRecyclerView = findViewById(R.id.rv_tasks);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tasksRecyclerView.setAdapter(taskAdapter);
    }

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
