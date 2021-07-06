package com.example.studywithme.ui.reflection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ReflectionViewPagerAdapter extends FragmentPagerAdapter {

    public ReflectionViewPagerAdapter(FragmentManager fragmentManager) {
        // ViewPager is deprecated but we don't care
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        // shitty but works
        return 2;
    }

    @NonNull
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ReflectionQuestFeedbackFragment();
                break;
            case 1:
                fragment = new ReflectionQuestDistractionsFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }
}