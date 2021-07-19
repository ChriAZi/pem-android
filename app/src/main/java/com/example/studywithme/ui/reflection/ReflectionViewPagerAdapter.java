package com.example.studywithme.ui.reflection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * This class handles the fragments that are holding the questions to be in order.
 */
public class ReflectionViewPagerAdapter extends FragmentPagerAdapter {

    public ReflectionViewPagerAdapter(FragmentManager fragmentManager) {
        // ViewPager is deprecated but we don't care
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        // shitty but works
        return 3;
    }

    /**
     * Returns the Fragment on the according position.
     * @param position
     * @return
     */
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
            case 2:
                fragment = new ReflectionQuestSessionEndFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }
}
