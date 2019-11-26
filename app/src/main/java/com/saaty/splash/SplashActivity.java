package com.saaty.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.saaty.R;
import com.saaty.home.HomeActivity;

public class SplashActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {


    @BindView(R.id.slider_layout_id) SliderLayout sliderLayout;
    @BindView(R.id.splash_msg) TextView splashMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        int []x={R.drawable.splash_photo_1,R.drawable.splash_photo_2,R.drawable.splash_photo_3};

        for(int i=0;i<x.length;i++){
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.image(x[i])
            .setScaleType(BaseSliderView.ScaleType.CenterInside)
            .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle())
            ;
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setDuration(5000);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.addOnPageChangeListener(this);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());


        startActivity(new Intent(this,SplashLanguageActivity.class));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        switch (position){
            case 0:
                splashMsg.setText(R.string.splash_message1);
            case 1:
                splashMsg.setText(R.string.splash_message2);
            case 2:
                splashMsg.setText(R.string.splash_message3);
            default:
                return;

        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                splashMsg.setText(R.string.splash_message1);
            case 1:
                splashMsg.setText(R.string.splash_message2);
            case 2:
                splashMsg.setText(R.string.splash_message3);
                default:
                    return;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
