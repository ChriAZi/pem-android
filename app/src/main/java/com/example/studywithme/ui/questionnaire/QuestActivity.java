package com.example.studywithme.ui.questionnaire;

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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.studywithme.R;
import org.jetbrains.annotations.NotNull;

public class QuestActivity extends AppCompatActivity {

    private Fragment question1, question2, question3;
    private Button btnNext, btnPrev;
    private ViewPager viewPager;
    private Adapter adapter;
    private FragmentManager fm;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        question1 = new Quest1Fragment();
        question2 = new Quest2Fragment();
        question3 = new Quest3Fragment();

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.questLayout, question1).commit();

        setupViewPager();
        pageChange();

        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setVisibility(View.GONE);
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
                        btnPrev.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        btnPrev.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        btnPrev.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.GONE);
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
        Log.d("tagtagtag", "adapter set, viewpager set" + adapter.list_fragments[0]);
    }

    private class Adapter extends PagerAdapter {
        Context context;
        Fragment question1, question2, question3;
        //LayoutInflater inflater;

        public Adapter(Context context) {
            this.context = context;
        }

        public Fragment[] list_fragments = {
                question1 = new Quest1Fragment(),
                question2 = new Quest2Fragment(),
                question3 = new Quest3Fragment()
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
}
