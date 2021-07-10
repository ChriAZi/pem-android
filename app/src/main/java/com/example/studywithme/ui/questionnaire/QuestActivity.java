package com.example.studywithme.ui.questionnaire;

import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.utils.Constants;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class QuestActivity extends NavigationActivity {

    private boolean joining = false;
    private ViewPager viewPager;
    private int currentPage;
    private boolean taskAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            joining = (boolean) extras.get(Constants.JOINING);
            if (joining) {
                setHasPartner();
            } else {
                setupViewPager();
            }
        } else {
            setupViewPager();
        }
    }

    private void setHasPartner() {
        QuestionnaireViewModel questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        questionnaireViewModel.isJoining(Session.getIdFromPreferences(this)).observe(this, joining -> {
            if (joining) {
                setupViewPager();
            }
        });
    }

    private void setupViewPager() {
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        viewPager = findViewById(R.id.view_pager);
        QuestViewPagerAdapter questViewPagerAdapter = new QuestViewPagerAdapter(getSupportFragmentManager(), joining);
        viewPager.setAdapter(questViewPagerAdapter);
        dotsIndicator.setViewPager(viewPager);

        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                validate(questViewPagerAdapter.getItem(position), currentPage);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        };
        viewPager.addOnPageChangeListener(listener);
    }

    private void validate(Fragment position, int currentPage) {
        if (position instanceof QuestNameFragment) {
            EditText etName = findViewById(R.id.et_name);
            if (etName.getText().toString().trim().length() == 0) {
                viewPager.setCurrentItem(currentPage);
            }
        } else if (position instanceof QuestGoalFragment) {
            EditText etGoal = (EditText) findViewById(R.id.et_goal);
            if (etGoal.getText().toString().trim().length() == 0) {
                viewPager.setCurrentItem(currentPage);
            }
        } else if (position instanceof QuestTaskFragment) {
            if (!taskAdded) {
                viewPager.setCurrentItem(currentPage);
            }
        } else if (position instanceof QuestDurationFragment) {
            EditText etDuration = (EditText) findViewById(R.id.et_duration);
            if (etDuration.getText().toString().trim().length() == 0) {
                viewPager.setCurrentItem(currentPage);
            }
        }
    }

    public void setTaskAdded(boolean taskAdded) {
        this.taskAdded = taskAdded;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_quest;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_questionnaire);
    }
}
