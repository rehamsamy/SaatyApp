package com.saaty.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.saaty.CustomPagerAdapter;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.tbuonomo.viewpagerdotsindicator.BaseDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


   // @BindView(R.id.slider_layout_id) SliderLayout sliderLayout;
    @BindView(R.id.view_pager_id) ViewPager viewPager;
    @BindView(R.id.spring_dots_indicator)
    BaseDotsIndicator springDotsIndicator;
    CustomPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        List<Integer> x=new ArrayList<>();
        x.add(R.drawable.splash_photo_1);
        x.add(R.drawable.splash_photo_2);
        x.add(R.drawable.splash_photo_3);

        List<String> y=new ArrayList<>();
        y.add(getString(R.string.splash_message1));
        y.add(getString(R.string.splash_message2));
        y.add(getString(R.string.splash_message3));




        adapter=new CustomPagerAdapter(SplashActivity.this,x,y);
        viewPager.setAdapter(adapter);
        springDotsIndicator.setViewPager(viewPager);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==2){

                    startActivity(new Intent(getApplicationContext(),SplashLanguageActivity.class));

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }


}
