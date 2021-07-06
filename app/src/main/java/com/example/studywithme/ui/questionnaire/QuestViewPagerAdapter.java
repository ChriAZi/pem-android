package com.example.studywithme.ui.questionnaire;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class QuestViewPagerAdapter extends FragmentPagerAdapter {

    public QuestViewPagerAdapter(FragmentManager fragmentManager) {
        // ViewPager is deprecated but we don't care
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        // shitty but works
        return 6;
    }

    @NonNull
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new QuestNameFragment();
                break;
            case 1:
                fragment = new QuestGoalFragment();
                break;
            case 2:
                fragment = new QuestCategoryFragment();
                break;
            case 3:
                fragment = new QuestTaskFragment();
                break;
            case 4:
                fragment = new QuestDurationFragment();
                break;
            case 5:
                fragment = new QuestSessionStartFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }
}