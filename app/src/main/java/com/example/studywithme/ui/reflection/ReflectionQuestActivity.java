package com.example.studywithme.ui.reflection;

import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

/**
 * Class that contains the Reflection Activity.
 * This activity handles the reflection questions after a session and
 * holds and organises the swiping through fragments that act as the questions.
 * It extends the Navigation Activity to have the Bottom Navigation shown.
 */
public class ReflectionQuestActivity extends NavigationActivity {

    private ViewPager viewPager;
    private int currentPage;
    private boolean distractionAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
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
        ReflectionViewPagerAdapter reflectionViewPagerAdapter = new ReflectionViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(reflectionViewPagerAdapter);
        dotsIndicator.setViewPager(viewPager);

        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                validateReflections(reflectionViewPagerAdapter.getItem(position), currentPage);
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
    private void validateReflections(Fragment position, int currentPage) {
        if (position instanceof ReflectionQuestFeedbackFragment) {
            EditText etFeedback = findViewById(R.id.et_feedback);
            if (etFeedback.getText().toString().trim().length() == 0) {
                viewPager.setCurrentItem(currentPage);
            }
        } else if (position instanceof ReflectionQuestDistractionsFragment) {
            if(!distractionAdded) {
                viewPager.setCurrentItem(currentPage);
            }
        }
    }

    public void setDistractionAdded(boolean distractionAdded) {
        this.distractionAdded = distractionAdded;
    }

    @Override

    public int getContentViewId() {
        return R.layout.activity_reflection;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_reflection);
    }

}
