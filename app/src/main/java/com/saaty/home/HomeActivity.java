package com.saaty.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;
import com.saaty.R;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.CategoryModel;
import com.saaty.models.DataItem;
import com.saaty.models.UserModel;
import com.saaty.sideMenuScreen.AboutAppActivity;
import com.saaty.sideMenuScreen.AboutUsActivity;
import com.saaty.sideMenuScreen.ContactUsActivity;
import com.saaty.sideMenuScreen.ProfileActivity;
import com.saaty.sideMenuScreen.wishlist.WishlistActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.NetworkAvailable;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.ConstraintSet.*;
import static androidx.constraintlayout.widget.ConstraintSet.BOTTOM;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener ,  ViewPagerEx.OnPageChangeListener {

    private static final String TAG =HomeActivity.class.getSimpleName() ;
    @BindView(R.id.nv_id)
     NavigationView navigationView;
     @BindView(R.id.drawer_layout_id) DrawerLayout drawerLayout;
     @BindView(R.id.toolbar_id) Toolbar toolbar;
     @BindView(R.id.toolbar_txt_id) TextView toolbarText;
     @BindView(R.id.store_id) CardView cardView;
     @BindView(R.id.slider_layout_id) SliderLayout sliderLayout;
     @BindView(R.id.constraint_root) ConstraintLayout root;
   ApiServiceInterface apiServiceInterface;
   NetworkAvailable networkAvailable;
   UserModel userModel;
public static int user_id;
 List<DataItem> categoryItem=new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
      // navigationView.inflateHeaderView(R.layout.visitor_header_layout);

        networkAvailable=new NetworkAvailable(this);
       toggle.syncState();
       toolbarText.setText(R.string.home_page);

       Intent intent=getIntent();
       if(intent.hasExtra("user_model")){
           userModel=intent.getParcelableExtra("user_model");
           user_id=userModel.getId();
       }else if(intent.getAction().equals("login_user")){
           hideNavigationItems();

           ConstraintLayout layout=(ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.visitor_header_layout,null);
           navigationView.addHeaderView(layout);
       }


       getCateogries();
       cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),StoresActivity.class));
           }
       });


       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               int id=menuItem.getItemId();
               if(id==R.id.nav_home_page){
                  // if(user_id !=0){
                       startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                  // }
//                   else {
//                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
//                   }
               }else if(id==R.id.nav_my_account){
                   if(user_id!=0){
                       startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                       Log.v(TAG,"my account");
                   }else {
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                       Log.v(TAG,"login activit");
                   }
               }else if(id==R.id.nav_wishlist){
                   if(user_id!=0){
                       startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                   }else {
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                   }
               }else if(id==R.id.nav_my_ads){
                   if(user_id!=0){
                       // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                   }else {
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                   }
               }else if(id==R.id.nav_messages){
                   if(user_id!=0){
                       // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                   }else {
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                   }
               }else if(id==R.id.nav_setting){
                   if(user_id!=0){
                       // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                   }else {
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                   }

               }else if(id==R.id.nav_about_app){

                   startActivity(new Intent(HomeActivity.this, AboutAppActivity.class));


               }else if(id==R.id.nav_about_us){
                   startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));

               }else if(id==R.id.nav_contact_us){
                   startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));

               }else if(id==R.id.nav_logout){
                   if(user_id!=0){
                       // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
                   }else {
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                   }

               }
               return true;

           }
       });
      int [] slideImges={R.drawable.store1,R.drawable.store2,R.drawable.store3,R.drawable.store4};

      for(int i=0;i<=slideImges.length-1;i++){
          TextSliderView textSliderView=new TextSliderView(this);
          textSliderView.image(slideImges[i])
          .setScaleType(BaseSliderView.ScaleType.Fit)
          .setOnSliderClickListener(HomeActivity.this);
          textSliderView.bundle(new Bundle());
          sliderLayout.addSlider(textSliderView);
      }

      sliderLayout.setCustomAnimation(new DescriptionAnimation());
      sliderLayout.setDuration(3000);
      sliderLayout.addOnPageChangeListener(HomeActivity.this);
      sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
      sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);


    }


    private void getCateogries() {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface= ApiClient.getClientService();
            Call<CategoryModel> call=apiServiceInterface.getCatogory();
            call.enqueue(new Callback<CategoryModel>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                    if(response.body().isSuccess()){
                        Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        categoryItem=response.body().getData();
                        for(int i=0;i<categoryItem.size();i++){
                         CardView cardView1=new CardView(getApplicationContext());
                         TextView name=new TextView(getApplicationContext());
                         name.setText(categoryItem.get(i).getCategoryArName().toString());
                         cardView1.addView(name, Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
                         cardView1.setBackgroundColor(R.color.white);
                          //  ConstraintSet set=new ConstraintSet();
//                            set.clone(root);
//                            set.connect(cardView1.getId(), TOP,cardView.getId(), BOTTOM,20);
//                            set.applyTo(root);
                            ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,60);
                         layoutParams.setMargins(20,20,20,20);

                         root.addView(cardView1,layoutParams);
                        }
                    }else {
                        Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<CategoryModel> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
         if(id==R.id.nav_home_page){
             if(user_id !=0){
                 startActivity(new Intent(HomeActivity.this,HomeActivity.class));
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
             }
         }else if(id==R.id.nav_my_account){
             if(user_id!=0){
                 startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                 Log.v(TAG,"my account");
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
                 Log.v(TAG,"login activit");
             }
         }else if(id==R.id.nav_wishlist){
             if(user_id!=0){
                 startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
             }
         }else if(id==R.id.nav_my_ads){
             if(user_id!=0){
                // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
             }
         }else if(id==R.id.nav_messages){
             if(user_id!=0){
                 // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
             }
         }else if(id==R.id.nav_setting){
             if(user_id!=0){
                 // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
             }

         }else if(id==R.id.nav_about_app){

                 startActivity(new Intent(HomeActivity.this, AboutAppActivity.class));


         }else if(id==R.id.nav_about_us){
                 startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));

         }else if(id==R.id.nav_contact_us){
                 startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));

         }else if(id==R.id.nav_logout){
             if(user_id!=0){
                 // startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
             }else {
                 startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));
             }

         }
             return true;

    }



    private void hideNavigationItems() {
        Menu menu=navigationView.getMenu();
        menu.findItem(R.id.nav_my_account).setVisible(false);
        menu.findItem(R.id.nav_wishlist).setVisible(false);
        menu.findItem(R.id.nav_my_ads).setVisible(false);
        menu.findItem(R.id.nav_messages).setVisible(false);
        navigationView.getHeaderView(0).setVisibility(View.GONE);

    }

}
