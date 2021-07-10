package com.example.studywithme.ui.reflection;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.questionnaire.QuestCategoryFragment;
import com.example.studywithme.ui.questionnaire.QuestDurationFragment;
import com.example.studywithme.ui.questionnaire.QuestGoalFragment;
import com.example.studywithme.ui.questionnaire.QuestNameFragment;
import com.example.studywithme.ui.questionnaire.QuestTaskFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ReflectionQuestActivity extends NavigationActivity {

    private ViewPager viewPager;
    private int currentPage;

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

    public void validateReflections(Fragment position, int currentPage) {
        if(position instanceof ReflectionQuestFeedbackFragment){
            EditText etFeedback = findViewById(R.id.et_feedback);
            if(etFeedback.getText().toString().trim().length() == 0) {
                viewPager.setCurrentItem(currentPage);
            }
        }else {
            Log.d("test", "not an instance of anything... ");
        }
    };
}
