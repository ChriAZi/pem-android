package com.example.studywithme.ui.reflection;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ReflectionQuestActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        ViewPager viewPager = findViewById(R.id.view_pager);
        ReflectionViewPagerAdapter reflectionViewPagerAdapter = new ReflectionViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(reflectionViewPagerAdapter);
        dotsIndicator.setViewPager(viewPager);
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
