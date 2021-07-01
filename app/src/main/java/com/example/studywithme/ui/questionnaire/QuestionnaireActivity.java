package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.google.firebase.firestore.DocumentReference;

public class QuestionnaireActivity extends NavigationActivity {

    private Fragment question1, question2, question3, question4;
    private Button btnNext, btnPrev;
    private ViewPager viewPager;
    private Adapter adapter;
    private FragmentManager fm;
    private int page = 0;
    private QuestionnaireViewModel questionnaireViewModel;
    private DocumentReference owner;
    private User user;
    private String session2;
    private Session session;
    private SessionSetting setting;
    private String uID;
    //TextView textTimer,creatorName,partnerName,creatorGoal,partnerGoal,creatorWork,partnerWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tagtagtag", "onCreate: ");
        super.onCreate(savedInstanceState);

        question1 = new Quest1Fragment();
        question2 = new Quest2Fragment();
        question3 = new Quest3Fragment();
        question4 = new Quest4Fragment();

        /*
        getCurrentSession();
        getCurrentUser();
        */

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.questLayout, question1).commit();

        setupViewPager();
        pageChange();

        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setEnabled(false);
        btnNext = findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                if (btnNext.getText() == "Submit") {
                    Log.d("lookhere", "TODO: communicate with firebase");
                    // initViewModel();
                }
            }
        });
    }

    /**
     * observes the current session and sets the variables to the current values
     * but does not work yet
     */
    /*
    private void initViewModel(){
        sessionCreationViewModel.getCurrentSession().observe(this, sessions -> {
            owner = sessions.getOwner();
        });
        SessionTask sessionTask1 = new SessionTask("Subtask1", true);
        SessionTask sessionTask2 = new SessionTask("Subtask2", true);
        setting = new SessionSetting
                ("testgoal",
                new ArrayList<SessionCategory>() {{add(SessionCategory.HOBBY);}},
                        new ArrayList<SessionTask>() {{add(sessionTask1); add(sessionTask2);}},
                        60
                );
        session = new Session(new Timestamp(new Date()), false, owner, setting);
        sessionCreationViewModel = new ViewModelProvider(this).get(SessionCreationViewModel.class);
        sessionCreationViewModel.createSession(user.getUid(), session);
    }

    private void getCurrentUser() {
    }

    private void getCurrentSession() {
        // session = Session.getIdFromPreferences(this);
    } */
    private void pageChange() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                fm.beginTransaction().replace(R.id.questLayout, adapter.list_fragments[position]).addToBackStack(null).commit();

                switch (position) {
                    case 0:
                        btnPrev.setEnabled(false);
                        btnNext.setEnabled(true);
                        btnNext.setText("Next");
                        btnNext.setBackgroundColor(Color.parseColor("#516c8d"));
                        break;
                    case 1:
                        btnPrev.setEnabled(true);
                        btnNext.setEnabled(true);
                        btnNext.setText("Next");
                        btnNext.setBackgroundColor(Color.parseColor("#516c8d"));
                        break;
                    case 2:
                        btnPrev.setEnabled(true);
                        btnNext.setEnabled(true);
                        btnNext.setText("Next");
                        btnNext.setBackgroundColor(Color.parseColor("#516c8d"));
                        break;
                    case 3:
                        btnPrev.setEnabled(true);
                        btnNext.setText("Submit");
                        btnNext.setBackgroundColor(Color.parseColor("#244160"));

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager() {
        adapter = new Adapter(this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_questionnaire;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    private class Adapter extends PagerAdapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        public Fragment[] list_fragments = {
                question1,
                question2,
                question3,
                question4
        };

        @Override
        public int getCount() {
            return list_fragments.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return context;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        }
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "myFragment1", question1);
        getSupportFragmentManager().putFragment(outState, "myFragment2", question2);
        getSupportFragmentManager().putFragment(outState, "myFragment3", question3);
        getSupportFragmentManager().putFragment(outState, "myFragment4", question4);

    }*/

}
