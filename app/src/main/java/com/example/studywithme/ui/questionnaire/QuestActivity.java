package com.example.studywithme.ui.questionnaire;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class QuestActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        dotsIndicator.setViewPager(viewPager);
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
