package com.example.dzakiyh.simpel;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.dzakiyh.simpel.fragments.HelpFragment;
import com.example.dzakiyh.simpel.fragments.HistoryFragment;
import com.example.dzakiyh.simpel.fragments.HomeFragment;
import com.example.dzakiyh.simpel.fragments.MyAccountFragment;

public class HomeActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.home_grey,
            R.drawable.history_grey,
            R.drawable.help_gray,
            R.drawable.myacc_grey
    };

    private int[] tabIconsColourful = {
            R.drawable.home_green,
            R.drawable.history_green,
            R.drawable.help_green,
            R.drawable.myacc_green
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                if(tab.getPosition() == 0){
                    tab.setIcon(tabIconsColourful[0]);
                }

                else if(tab.getPosition() == 1){
                    tab.setIcon(tabIconsColourful[1]);
                }

                else if(tab.getPosition() == 2){
                    tab.setIcon(tabIconsColourful[2]);
                }

                else if(tab.getPosition() == 3){
                    tab.setIcon(tabIconsColourful[3]);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);

                if(tab.getPosition() == 0){
                    tab.setIcon(tabIcons[0]);

                }

                else if(tab.getPosition() == 1){
                    tab.setIcon(tabIcons[1]);
                }

                else if(tab.getPosition() == 2){
                    tab.setIcon(tabIcons[2]);
                }

                else if(tab.getPosition() == 3){
                    tab.setIcon(tabIcons[3]);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIconsColourful[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void addTabs(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFragment(), "Home");
        viewPagerAdapter.addFrag(new HistoryFragment(), "History");
        viewPagerAdapter.addFrag(new HelpFragment(), "Help");
        viewPagerAdapter.addFrag(new MyAccountFragment(), "Account");

        viewPager.setAdapter(viewPagerAdapter);
    }
}
