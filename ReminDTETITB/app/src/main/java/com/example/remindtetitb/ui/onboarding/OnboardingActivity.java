package com.example.remindtetitb.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.remindtetitb.ui.main.MainActivity;
import com.example.remindtetitb.R;
import com.example.remindtetitb.helper.SharedPrefManager;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout dotsLinearLayout;
    private int[] slides;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefManager = new SharedPrefManager(this);
        if(!sharedPrefManager.isFirstLaunch()){
            launchMainActivity();
            finish();
        }

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.vp_onboarding);
        dotsLinearLayout = findViewById(R.id.ll_onboarding_dots);
        TextView tvLewati = findViewById(R.id.tv_onboarding_lewati);
        tvLewati.setPaintFlags(tvLewati.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        slides = new int[]{
                R.layout.onboarding_slide1,
                R.layout.onboarding_slide2,
                R.layout.onboarding_slide3
        };

        addBottomDots(0);

        changeStatusBarColor();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        tvLewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSlide = getItem(+1);
                if(currentSlide < slides.length){
                    viewPager.setCurrentItem(currentSlide);
                } else{
                    launchMainActivity();
                }
            }
        });
    }

    private void addBottomDots(int currentPage){
        TextView[] dots = new TextView[slides.length];

        int[] activeDots = getResources().getIntArray(R.array.array_active_dots);
        int[] inactiveDots = getResources().getIntArray(R.array.array_inactive_dots);

        dotsLinearLayout.removeAllViews();
        for(int i = 0; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(inactiveDots[currentPage]);
            dotsLinearLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[currentPage].setTextColor(activeDots[currentPage]);
        }
    }

    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    private void launchMainActivity(){
        sharedPrefManager.setIsFirstLaunch(false);

        Intent toMainIntent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(toMainIntent);
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addBottomDots(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }



    public class ViewPagerAdapter extends PagerAdapter{
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(slides[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return slides.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
