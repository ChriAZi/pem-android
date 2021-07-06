package com.example.studywithme.ui.questionnaire;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.utils.Constants;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class QuestActivity extends NavigationActivity {

    private boolean joining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
        Bundle extras = getIntent().getExtras();
        joining = (boolean) extras.get(Constants.JOINING);
    }

    private void setupViewPager() {
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        ViewPager viewPager = findViewById(R.id.view_pager);
        QuestViewPagerAdapter questViewPagerAdapter = new QuestViewPagerAdapter(getSupportFragmentManager(), joining);
        viewPager.setAdapter(questViewPagerAdapter);
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
