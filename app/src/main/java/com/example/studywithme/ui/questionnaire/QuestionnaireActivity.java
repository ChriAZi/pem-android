package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionCategory;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.SessionTask;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.timer.TimerActivity;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class QuestionnaireActivity extends NavigationActivity
        implements QuestNameFragment.QuestNameFragmentListener,
        Quest1Fragment.Quest1FragmentListener,
        Quest3Fragment.Quest3FragmentListener,
        Quest4Fragment.Quest4FragmentListener,
        QuestPublicFragment.QuestPublicFragmentListener {

    private Fragment questionName, question1, question2, question3, question4, questionPublic;
    private Button btnNext, btnPrev, btnSubmit;
    private ViewPager viewPager;
    private Adapter adapter;
    private FragmentManager fm;
    private int page = 0;
    private QuestionnaireViewModel questionnaireViewModel;
    private DocumentReference owner;
    private User user;
    private String session2;
    private Session session;
    private SessionSetting ownerSetting;
    private String uID;
    private int frduration = 0;
    private String frname, frgoal = "";
    private SessionCategory frcategory;
    private ArrayList<SessionTask> frtasks;
    private boolean frpublic = false;
    //TextView textTimer,creatorName,partnerName,creatorGoal,partnerGoal,creatorWork,partnerWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questionName = new QuestNameFragment();
        question1 = new Quest1Fragment();
        question2 = new Quest2Fragment();
        question3 = new Quest3Fragment();
        question4 = new Quest4Fragment();
        questionPublic = new QuestPublicFragment();

        /*
        getCurrentSession();
        getCurrentUser();
        */

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.questLayout, questionName).commit();

        setupViewPager();
        pageChange();

        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setEnabled(false);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);


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
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

                initViewModel(v);
            }
        });
    }

    /**
     * observes the current session and sets the variables to the current values
     * but does not work yet
     */

    private void initViewModel(View v) {

        frcategory = SessionCategory.UNIVERSITY;

        //receive data from fragment interfaces
        String name = frname;
        String goal = frgoal;
        SessionCategory category = frcategory;
        Log.d("receive", "cat: " + null);
        ArrayList<SessionTask> tasks = frtasks;
        int duration = frduration;
        boolean isPublic = frpublic;


        ownerSetting = new SessionSetting(name, goal, category, tasks);
        session = new Session(120, frpublic, ownerSetting);

        questionnaireViewModel = new ViewModelProvider(this).get(QuestionnaireViewModel.class);


        questionnaireViewModel.startSession(User.getIdFromPreferences(this), session).observe(this, sessionId -> {
            if (sessionId != null) {
                startTimer(v);
            }
        });
    }

    private void startTimer(View view) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("Extra_name", frname);
        intent.putExtra("Extra_goal", frgoal);
        intent.putExtra("Extra_categories", frcategory);
        intent.putExtra("Extra_tasks", frtasks);
        intent.putExtra("Extra_duration", frduration);
        intent.putExtra("Extra_public", frpublic);
        startActivity(intent);
    }

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
                        //btnNext.setEnabled(true);
                        //btnNext.setBackgroundColor(Color.parseColor("#516c8d"));
                        btnNext.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.GONE);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        btnPrev.setEnabled(true);
                        //btnNext.setEnabled(true);
                        btnNext.setText("Next");
                        //btnNext.setBackgroundColor(Color.parseColor("#516c8d"));
                        btnNext.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.GONE);
                        break;
                    case 5:
                        btnPrev.setEnabled(true);
                        btnNext.setVisibility(View.GONE);
                        //btnNext.setBackgroundColor(Color.parseColor("#244160"));
                        btnSubmit.setVisibility(View.VISIBLE);
                        //btnSubmit.setVisibility(View.GONE);
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
    public void getName(String input) {
        frname = input;
    }

    @Override
    public void getGoal(String input) {
        frgoal = input;
    }

    @Override
    public void getTasks(ArrayList<SessionTask> input) {
        frtasks = input;
    }

    @Override
    public void getDuration(int input) {
        frduration = input;
    }

    @Override
    public void getPublic(boolean input) {
        frpublic = input;
    }

    public int getContentViewId() {
        return R.layout.activity_questionnaire;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.heading_questionnaire);
    }

    private class Adapter extends PagerAdapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        public Fragment[] list_fragments = {
                questionName,
                question1,
                question2,
                question3,
                question4,
                questionPublic
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


}
