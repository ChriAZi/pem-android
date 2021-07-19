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

/**
 * Class that contains the Questionnaire Activity.
 * This activity handles the first steps for starting a session and
 * holds and organises the swiping through fragments that act as the questions.
 * It extends the Navigation Activity to have the Bottom Navigation shown.
 */
public class QuestActivity extends NavigationActivity {

    private boolean joining = false;
    private ViewPager viewPager;
    private int currentPage;
    private boolean taskAdded = false;

    /**
     * When activity is created, it is checked whether the user is joining a existing session or creating his/her own.
     * Depending on this, some questions are asked or not, accordingly the ViewPager is set up.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            joining = (boolean) extras.get(Constants.JOINING);
            if (joining) {
                setHasPartner(true, true);
            } else {
                setupViewPager();
            }
        } else {
            setupViewPager();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (joining) {
            setHasPartner(false, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (joining) {
            setHasPartner(true, true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (joining) {
            setHasPartner(false, false);
        }
    }

    /**
     * Gives information about the state of already having a partner to the Viewmodel.
     * @param hasPartner
     * @param showPager
     */
    private void setHasPartner(boolean hasPartner, boolean showPager) {
        QuestionnaireViewModel questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);
        questionnaireViewModel.isJoining(Session.getIdFromPreferences(this), hasPartner).observe(this, joining -> {
            if (joining && showPager) {
                setupViewPager();
            }
        });
    }

    /**
     * Sets up the ViewPager, which enables Swiping through the pages.
     * The page you are on is indicated by the dots in the bottom of the page.
     * It implements a listener which listens for onPageScrolled, onPageSelected, onPageScrollStateChange.
     * The first triggers the validate function to check if input is given.
     */
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

    /**
     * Validates whether input and the right input type are given per Question.
     * Each Question is its own Fragment.
     * If there is no input given at the according Fragment, you cannot page forward.
     * The CurrentItem variable (which means the page you turn to) is set to the page you were on, so you stay on your page.
     * You are always able to go back though.
     * @param position
     * @param currentPage
     */
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
