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
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.saaty.R;
import com.saaty.home.StoresProduct.StoresProductsActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.CategoryModel;
import com.saaty.models.DataItem;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.models.UserModel;
import com.saaty.sideMenuScreen.AboutAppActivity;
import com.saaty.sideMenuScreen.AboutUsActivity;
import com.saaty.sideMenuScreen.ContactUsActivity;
import com.saaty.sideMenuScreen.ProfileActivity;
import com.saaty.sideMenuScreen.myAds.EditAdsActivity;
import com.saaty.sideMenuScreen.myAds.MyAdsActivity;
import com.saaty.sideMenuScreen.wishlist.DealingWithWishList;
import com.saaty.sideMenuScreen.wishlist.WishlistActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.PreferenceHelper;

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
     @BindView(R.id.slider_layout_id) SliderLayout sliderLayout;
     @BindView(R.id.constraint_root) ConstraintLayout root;
      @BindView(R.id.english_arrow_id) ImageView englishArrowId;
    @BindView(R.id.english_arrow_id1) ImageView englishArrowId1;
    @BindView(R.id.english_arrow_id2) ImageView englishArrowId2;
    @BindView(R.id.arabic_arrow_id) ImageView arabicArrowId;
    @BindView(R.id.arabic_arrow_id1) ImageView arabicArrowId1;
    @BindView(R.id.arabic_arrow_id2) ImageView arabicArrowId2;
    @BindView(R.id.store_name_id)TextView storeNameId;
    @BindView(R.id.watches_name_id)TextView watchNameId;
    @BindView(R.id.bracletes_name_id)TextView bracletesNameId;
    @BindView(R.id.toolbar_back_left_btn_id) ImageView toolbarBackImg;
    CircleImageView userImg;
    MenuItem logoutItem;

   ApiServiceInterface apiServiceInterface;
   NetworkAvailable networkAvailable;
   UserModel userModel;
public static int user_id;
 List<DataItem> categoryItem=new ArrayList<>();
 List<ProductDataItem> sliderImages=new ArrayList<>();
 MaterialButton addAdsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toolbarBackImg.setVisibility(View.GONE);

      // navigationView.inflateHeaderView(R.layout.visitor_header_layout);

        checkLanguage();

        networkAvailable=new NetworkAvailable(this);
       toggle.syncState();
       toolbarText.setText(R.string.home_page);

       addAdsBtn=navigationView.getHeaderView(0).findViewById(R.id.confirm_btn_id);
       Intent intent=getIntent();
       if(intent.hasExtra("user_model")){
           userModel=intent.getParcelableExtra("user_model");
           user_id=userModel.getId();
       }else if(intent.getAction().equals("login_visitor")){
           hideNavigationItems();
           if (user_id == 0) {
               logoutItem.setTitle(getString(R.string.login_btn));
           } else {
               logoutItem.setTitle(getString(R.string.sign_out));
           }
           ConstraintLayout layout=(ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.visitor_header_layout,null);
           navigationView.addHeaderView(layout);
       }else if(intent.hasExtra("register_user_model")){
           userModel=intent.getParcelableExtra("register_user_model");
          userImg= navigationView.getHeaderView(0).findViewById(R.id.user_img_id);
          userImg.setImageResource(R.drawable.sidemenu_photo);
           user_id=userModel.getId();

       }else if(intent.hasExtra("register_store_model")){
           userModel=intent.getParcelableExtra("register_store_model");
           user_id=userModel.getId();
           userImg= navigationView.getHeaderView(0).findViewById(R.id.user_img_id);
           userImg.setImageResource(R.drawable.sidemenu_photo2);
       }



       addAdsBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent1=new Intent(new Intent(HomeActivity.this, EditAdsActivity.class));
               //intent1.putExtra("ads_model",da)
               intent1.setAction("add_new_ads");
               startActivity(intent1);
           }
       });

       getCateogries();


       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               int id=menuItem.getItemId();
               if(id==R.id.nav_home_page){
                       startActivity(new Intent(HomeActivity.this,HomeActivity.class));

               }else if(id==R.id.nav_my_account){
                   if(user_id!=0){
                       Intent intent1=new Intent(HomeActivity.this, ProfileActivity.class);
                       startActivity(intent1);
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
                        startActivity(new Intent(HomeActivity.this, MyAdsActivity.class));
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
                       startActivity(new Intent(HomeActivity.this, LoginTraderUserActivity.class));


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
      //int [] slideImges={R.drawable.store1,R.drawable.store2,R.drawable.store3,R.drawable.store4};

      getSliderImages();

    }

    private void checkLanguage() {
        if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
            arabicArrowId.setVisibility(View.GONE);
            arabicArrowId1.setVisibility(View.GONE);
            arabicArrowId2.setVisibility(View.GONE);
            englishArrowId.setVisibility(View.VISIBLE);
            englishArrowId1.setVisibility(View.VISIBLE);
            englishArrowId2.setVisibility(View.VISIBLE);

        }else if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")){
            arabicArrowId.setVisibility(View.VISIBLE);
            arabicArrowId1.setVisibility(View.VISIBLE);
            arabicArrowId2.setVisibility(View.VISIBLE);
            englishArrowId.setVisibility(View.GONE);
            englishArrowId1.setVisibility(View.GONE);
            englishArrowId2.setVisibility(View.GONE);
        }

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

                         if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
                             watchNameId.setText(categoryItem.get(0).getCategoryEnName());
                             bracletesNameId.setText(categoryItem.get(1).getCategoryEnName());
                         }else if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")){
                             watchNameId.setText(categoryItem.get(0).getCategoryArName());
                             bracletesNameId.setText(categoryItem.get(1).getCategoryArName());

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
       logoutItem=menu.findItem(R.id.nav_logout);

    }


  private void   getSliderImages(){
        apiServiceInterface=ApiClient.getClientService();
        Call<ProductDataModel> call=apiServiceInterface.getSliderImages();
        call.enqueue(new Callback<ProductDataModel>() {
            @Override
            public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                if(response.body().isSuccess()){
                  sliderImages=response.body().getProductDataModels();
                  Log.v(TAG,"ssssssssss"+sliderImages.size());
                    for(int i=0;i<sliderImages.size();i++){
                        TextSliderView textSliderView=new TextSliderView(HomeActivity.this);
                        String url="https://sa3a.app/"+sliderImages.get(i).getImageLink();
                         textSliderView.image(url)
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

                }else {

                }
            }

            @Override
            public void onFailure(Call<ProductDataModel> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.watches_id)
    void watchesClick() {
        Intent intent1=new Intent(getApplicationContext(), StoresActivity.class);
        intent1.putExtra("category_id1",1);
        startActivity(intent1);
    }

    @OnClick(R.id.bracletes_id)
    void bracletesClick() {
        Intent intent1=new Intent(getApplicationContext(),StoresActivity.class);
        intent1.putExtra("category_id2",2);
        startActivity(intent1);
    }

    @OnClick(R.id.store_id)
    void storesClick(){
        Intent intent1=new Intent(getApplicationContext(),StoresActivity.class);
        startActivity(intent1);
    }

}

