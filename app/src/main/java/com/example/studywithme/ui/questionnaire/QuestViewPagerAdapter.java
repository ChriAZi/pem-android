package com.example.studywithme.ui.questionnaire;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * This class handles the fragments that are holding the questions to be in order.
 */
public class QuestViewPagerAdapter extends FragmentPagerAdapter {

    private final boolean joining;

    public QuestViewPagerAdapter(FragmentManager fragmentManager, boolean joining) {
        super(fragmentManager);
        this.joining = joining;
    }

    @Override
    public int getCount() {
        if (joining) {
            return 5;
        } else {
            return 6;
        }
    }

    /**
     * Returns the Fragment on the according position.
     * Which fragments (questions) are part of the list depends on whether you are joining an existing session
     * or start your own session.
     * @param position
     * @return
     */
    @NonNull
    public Fragment getItem(int position) {
        Fragment fragment;
        if (joining) {
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
                    fragment = new QuestSessionStartFragment(true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        } else {
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
                    fragment = new QuestSessionStartFragment(joining);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        }
        return fragment;
    }
}