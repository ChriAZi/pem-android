package com.example.studywithme.ui.questionnaire;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionSetting;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.SessionCreationViewModel;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;

import org.jetbrains.annotations.NotNull;

public class QuestActivity extends AppCompatActivity {

    private Fragment question1, question2, question3, question4;
    private Button btnNext, btnPrev;
    private ViewPager viewPager;
    private Adapter adapter;
    private FragmentManager fm;
    private int page = 0;
    private SessionCreationViewModel sessionCreationViewModel;
    private User user;
    private String session2;
    private Session session;
    private SessionSetting setting;
    private String uID;
    TextView textTimer,creatorName,partnerName,creatorGoal,partnerGoal,creatorWork,partnerWork;
    String[] allValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tagtagtag", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        question1 = new Quest1Fragment();
        question2 = new Quest2Fragment();
        question3 = new Quest3Fragment();
        question4 = new Quest4Fragment();

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            question1 = getSupportFragmentManager().getFragment(savedInstanceState, "myFragment1");
            question2 = getSupportFragmentManager().getFragment(savedInstanceState, "myFragment2");
            question3 = getSupportFragmentManager().getFragment(savedInstanceState, "myFragment3");
            question4 = getSupportFragmentManager().getFragment(savedInstanceState, "myFragment4");

        }

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.questLayout, question1).commit();

        setupViewPager();
        pageChange();

        partnerGoal = findViewById(R.id.partnerGoal);
        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setEnabled(false);
        btnNext = findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
            }
        });

        /*sessionCreationViewModel.getCurrentSession();
        getCurrentUser();
        initViewModel();*/
    }

    /**
     * observes the current session and sets the variables to the current values
     * but does not work yet
     */
    private void initViewModel(){
        sessionCreationViewModel = new ViewModelProvider(this).get(SessionCreationViewModel.class);
        sessionCreationViewModel.getCurrentSession().observe(this, sessions -> {
            textTimer.setText(sessions.getDuration());
            creatorName.setText((CharSequence) sessions.getOwner());
            creatorWork.setText((CharSequence) sessions.getOwnerSetting().getCategories());
            creatorGoal.setText(sessions.getOwnerSetting().getGoal());
            partnerName.setText((CharSequence) sessions.getPartner());
            partnerWork.setText((CharSequence) sessions.getPartnerSetting().getCategories());
            partnerGoal.setText(sessions.getPartnerSetting().getGoal());

        });
    }

    /*private void initViewModels() {
        sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);
        sessionHistoryViewModel.getSessions(user.getUid()).observe(this, sessions -> {
            partnerGoal.setText(session2);
        });
        creatorName.setText(user.getName());
    }*/

    private void getCurrentUser() {
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

    private class Adapter extends PagerAdapter {
        Context context;
        //Fragment question1, question2, question3, question4;
        //LayoutInflater inflater;

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
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return false;
        }

        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            return context;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        }
    }

    /*@Override
    protected void onSaveInstanceState (Bundle outState) {
        Log.d("tagtagtag", allValues.toString());
        outState.putStringArray("allValues", allValues);
        super.onSaveInstanceState(outState);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "myFragment1", question1);
        getSupportFragmentManager().putFragment(outState, "myFragment2", question2);
        getSupportFragmentManager().putFragment(outState, "myFragment3", question3);
        getSupportFragmentManager().putFragment(outState, "myFragment4", question4);

    }

}
