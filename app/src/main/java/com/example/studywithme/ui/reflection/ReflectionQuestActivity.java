package com.example.studywithme.ui.reflection;

import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ReflectionQuestActivity extends NavigationActivity {

    private ViewPager viewPager;
    private int currentPage;
    private boolean distractionAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
    }

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
