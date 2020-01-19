package com.saaty.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.saaty.CustomPagerAdapter;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.Data;
import com.saaty.models.UserDataRegisterObject;
import com.saaty.models.UserModel;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.PreferenceHelper;
import com.tbuonomo.viewpagerdotsindicator.BaseDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {


    // @BindView(R.id.slider_layout_id) SliderLayout sliderLayout;
    @BindView(R.id.view_pager_id)
    ViewPager viewPager;
    @BindView(R.id.spring_dots_indicator)
    BaseDotsIndicator springDotsIndicator;
    CustomPagerAdapter adapter;
    NetworkAvailable networkAvailable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (!networkAvailable.isNetworkAvailable())
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();


        //setConfig(PreferenceHelper.getValue(SplashActivity.this), SplashActivity.this);
        SharedPreferences prefs = getSharedPreferences(LoginTraderUserActivity.MY_PREFS_NAME, MODE_PRIVATE);
        String user_data = prefs.getString("user_data", "");
        Gson gson = new Gson();
        UserModel userModel = gson.fromJson(user_data, UserModel.class);

        String registerData=prefs.getString("register_data","");
        Gson gson1=new Gson();
        UserDataRegisterObject userDataRegisterObject=gson1.fromJson(registerData,UserDataRegisterObject.class);

//        String register_data = prefs.getString("register_data", "");
//        Gson gson1 = new Gson();
//        Data data = gson1.fromJson(register_data, Data.class);

        if (user_data != null && !user_data.equals("")) {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            intent.putExtra("user_data", userModel);
            Log.v("TAG","splahhhh");
            startActivity(intent);
            finish();
            // overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }else if(registerData !=null&& ! registerData .equals("")){
            Intent intent = new Intent(SplashActivity.this, LoginTraderUserActivity.class);
           // intent.putExtra("user_data", userModel);
            Log.v("TAG","splahhhh"+userDataRegisterObject.getFullname());
            startActivity(intent);
            finish();
        }


        else {

            List<Integer> x = new ArrayList<>();
            x.add(R.drawable.splash_photo_1);
            x.add(R.drawable.splash_photo_2);
            x.add(R.drawable.splash_photo_3);

            List<String> y = new ArrayList<>();
            y.add(getString(R.string.splash_message1));
            y.add(getString(R.string.splash_message2));
            y.add(getString(R.string.splash_message3));


            adapter = new CustomPagerAdapter(SplashActivity.this, x, y);
            viewPager.setAdapter(adapter);
            springDotsIndicator.setViewPager(viewPager);


            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == 2) {
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent(SplashActivity.this, SplashLanguageActivity.class));

                            }
                        }, 1000);
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

        List<Integer> newWishlist=new ArrayList<>();
        List<Integer> sorted=new ArrayList<>();
        newWishlist.add(50);
        newWishlist.add(100);
        newWishlist.add(200);
        newWishlist.add(20);
        newWishlist.add(10);
        newWishlist.add(300);
        newWishlist.add(80);

        for(int x=0;x<newWishlist.size();x++){
            Log.v("TAG","ss  "+newWishlist.get(x)+"\n");
        }

//          List<Integer>  newSortedList=new ArrayList<>();
//        for(int i=0;i<newWishlist.size();i++){
//            for (int j = i; j > 0; j--) {
//                if(newWishlist.get(i)<newWishlist.get(j-1)){
//                    newSortedList.add(newWishlist.get(i));
//                }
//            }
//        }

        for (int i = 0; i < newWishlist.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < newWishlist.size(); j++) {
                if (newWishlist.get(j) > newWishlist.get(pos))
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            int min = newWishlist.get(pos);
            newWishlist.set(pos, newWishlist.get(i));
            newWishlist.set(i, min);

//            int min = newWishlist.get(pos).getPrice();
//            newWishlist.set(pos, newWishlist.get(i));
//            //newWishlist.set(i, newWishlist.get(min));
//            newSortedList.add(newWishlist.get(i));

        }

        for(int x=0;x<newWishlist.size();x++){
            Log.v("TAG","new sort "+newWishlist.get(x)+"  ");
        }








    }


    public void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
       // context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }



}
