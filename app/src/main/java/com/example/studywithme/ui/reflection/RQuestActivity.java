package com.example.studywithme.ui.reflection;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionReflection;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.navigation.NavigationActivity;
import com.example.studywithme.ui.viewmodels.AbstractViewModel;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.ui.viewmodels.ReflectionViewModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class RQuestActivity extends NavigationActivity
        implements RQuestAchievedFragment.RQuestAchievedFragmentListener,
        RQuestDistractionsFragment.RQuestDistractionsFragmentListener{

    private Fragment questionAchieved, questionDistractions;
    private Button btnNext, btnPrev, btnSubmit;
    private ViewPager viewPager;
    private Adapter adapter;
    private FragmentManager fm;
    private int page = 0;
    private ReflectionViewModel reflectionViewModel;
    private AbstractViewModel abstractViewModel;
    private SessionReflection refSession;
    private DocumentReference owner;
    private User user;
    private String session2;
    private Session session;
    private SessionSetting ownerSetting;
    private String uID;
    private String frAchieved;
    private ArrayList<String> frDistractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflection);

        questionAchieved = new RQuestAchievedFragment();
        questionDistractions = new RQuestDistractionsFragment();

        /*
        getCurrentSession();
        getCurrentUser();
        */

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.rquestLayout, questionAchieved).commit();

        setupViewPager();
        pageChange();

        btnPrev = findViewById(R.id.rbtnPrev);
        btnPrev.setEnabled(false);
        btnNext = findViewById(R.id.rbtnNext);
        btnSubmit = findViewById(R.id.rbtnSubmit);
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
                initViewModel();
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_reflection;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    /**
     * observes the current session and sets the variables to the current values
     * but does not work yet
     */

    private void initViewModel(){

        //receive data from fragment interfaces
        String achieved = frAchieved;
        ArrayList<String> distractions = frDistractions;
        Log.d("receive", "public: "+frDistractions+frAchieved);

        /*abstractViewModel = new ViewModelProvider(this).get(AbstractViewModel.class);
        abstractViewModel.getActiveSession(Session.getIdFromPreferences(this)).observe(this, session -> {
        */
        refSession = new SessionReflection(frAchieved, frDistractions);

        reflectionViewModel = new ViewModelProvider(this).get(ReflectionViewModel.class);

        String sessionid = Session.getIdFromPreferences(this);

        reflectionViewModel.addReflection(User.getIdFromPreferences(this), sessionid, refSession).observe(this, sessionId -> {
            if(sessionId != null) {
                //welche activity starten? "Congrats on your achievements" view?
            }
        });

    }

    private void pageChange() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                fm.beginTransaction().replace(R.id.rquestLayout, adapter.list_fragments[position]).addToBackStack(null).commit();

                switch (position) {
                    case 0:
                        btnPrev.setEnabled(false);
                        //btnNext.setEnabled(true);
                        //btnNext.setBackgroundColor(Color.parseColor("#516c8d"));
                        btnNext.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.GONE);
                        break;
                    case 1:
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
        viewPager = findViewById(R.id.rviewpager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void getAchieved(String input) {
        frAchieved = input;
    }

    @Override
    public void getDistractions(ArrayList<String> input) { frDistractions = input; }


    private class Adapter extends PagerAdapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        public Fragment[] list_fragments = {
                questionAchieved,
                questionDistractions,
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
