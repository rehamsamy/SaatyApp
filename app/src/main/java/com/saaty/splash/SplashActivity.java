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
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
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
import com.saaty.loginAndRegister.TraderRegisterFragment;
import com.saaty.loginAndRegister.UserRegisterFragment;
import com.saaty.models.Data;
import com.saaty.models.LoginModel;
import com.saaty.models.UserDataRegisterObject;
import com.saaty.models.UserModel;
import com.saaty.password.VerificationCodeActivity;
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
        LoginModel userModel = gson.fromJson(user_data, LoginModel.class);
        // Log.v("TAG","fffff   "+userModel.getType().toString());


        SharedPreferences prefsRegisterTrader = getSharedPreferences(TraderRegisterFragment.MY_PREFS_NAME, MODE_PRIVATE);
        String registerDataTrader = prefsRegisterTrader.getString("register_data", "");
        Gson gson2 = new Gson();
        Data userDataRegisterObject2 = gson2.fromJson(registerDataTrader, Data.class);


        SharedPreferences prefsRegisterUser = getSharedPreferences(UserRegisterFragment.MY_PREFS_NAME, MODE_PRIVATE);
        String registerDataUser = prefsRegisterUser.getString("register_data", "");
        Gson gson1 = new Gson();
        Data userDataRegisterObject1 = gson1.fromJson(registerDataUser, Data.class);



        SharedPreferences prefsVistor = getSharedPreferences("vistor", MODE_PRIVATE);
        String value = prefsVistor.getString("loginVistor", "");
        Log.v("TAG", "fffff" + prefsVistor.toString());


        SharedPreferences prefsV=getSharedPreferences("check1",MODE_PRIVATE);
        int v=prefsV.getInt("user",0);

        SharedPreferences prefV2=getSharedPreferences("check2",MODE_PRIVATE);
        int v2=prefV2.getInt("trader",0);

        //Log.v("TAG","ddddd   "+prefsV.getString("user",""));
        if(prefsV!=null){
            Log.v("TAG","ddddd   "+prefsV.getInt("user",0));
        }


        if(prefsV.contains("user")){
            Log.v("TAG","uuuu  xxxx  "+prefs.getString("user",""));
        }



        SharedPreferences prefsVerify = getSharedPreferences(VerificationCodeActivity.VERIFY_CHECK, MODE_PRIVATE);
        String verify = prefsVerify.getString("token","");


        if (user_data != null && !user_data.equals("")) {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            intent.putExtra("user_data", userModel);
            Log.v("TAG", "splahhhh 1"+userModel.getUserModel().get(0).getEmail());
            startActivity(intent);
            finish();

        } else if (registerDataUser != null && !registerDataUser.equals("") && v!=0 ) {
            Intent intent = new Intent(SplashActivity.this, LoginTraderUserActivity.class);
            Log.v("TAG", "splahhhh 2" + userDataRegisterObject1.getUserDataRegisterObject().getType());
            startActivity(intent);
            finish();
        }else if (registerDataUser != null && !registerDataUser.equals("") && v==0) {
            Intent intent = new Intent(SplashActivity.this, VerificationCodeActivity.class);
            intent.putExtra("register_user", userDataRegisterObject1);
            Log.v("TAG", "splahhhh 3" + userDataRegisterObject1.getUserDataRegisterObject().getFullname());
            startActivity(intent);

        } else if (registerDataTrader != null && !registerDataTrader.equals("") && v2!=0) {
            Intent intent = new Intent(SplashActivity.this, LoginTraderUserActivity.class);
            Log.v("TAG", "splahhhh 3 " + userDataRegisterObject2.getUserDataRegisterObject().getFullname());
            startActivity(intent);
            finish();
        } else if (registerDataTrader != null && !registerDataTrader.equals("") &&v2==0) {
            Intent intent = new Intent(SplashActivity.this, VerificationCodeActivity.class);
            Log.v("TAG", "splahhhh 4" + userDataRegisterObject2.getUserDataRegisterObject().getType());
            intent.putExtra("register_trader",userDataRegisterObject2);
            startActivity(intent);
        }else if(value!=null && !value.equals("")){
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            intent.putExtra("login_visitor", userModel);
            Log.v("TAG","exper visitor"+value);
            Log.v("TAG", "splahhhh 5" );
            startActivity(intent);
            finish();

        }

        else {
              Log.v("TAG","exper  first ");
            Log.v("TAG", "splahhhh 5" );
            setConfig(getApplicationContext(),PreferenceHelper.getValue(SplashActivity.this));
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
//                        handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {

                                startActivity(new Intent(SplashActivity.this, SplashLanguageActivity.class));

//                            }
//                        }, 1000);
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

//        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
//                .putBoolean("isFirstRun", false).commit();



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
       }

        for(int x=0;x<newWishlist.size();x++){
            Log.v("TAG","new sort "+newWishlist.get(x)+"  ");
        }


    }




    @Override
    protected void onStart() {
        super.onStart();
        setConfig(getApplicationContext(),PreferenceHelper.getValue(getApplicationContext()));
        Log.v("TAG","langgg "+PreferenceHelper.getValue(getApplicationContext()));

    }


    public static void setConfig(Context context, String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }






}

