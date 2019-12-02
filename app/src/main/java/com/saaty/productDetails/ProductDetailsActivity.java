package com.saaty.productDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.kodmap.app.library.PopopDialogBuilder;
import com.kodmap.app.library.model.BaseItem;
import com.saaty.CustomPagerAdapter;
import com.saaty.MainActivity;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.home.StoresActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.CheckWishlistModel;
import com.saaty.models.DataArrayModel;
import com.saaty.models.GetProductImagesModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductimagesItem;
import com.saaty.sideMenuScreen.myAds.EditAdsActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;
import com.saaty.util.PreferenceHelper;
import com.saaty.util.urls;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.recyclerview.widget.RecyclerView.*;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    public static final String POSITION="position" ;
    public static final String WISHLIST_PRODUCTS_DETAILS ="wishlist_product_details";
    private static final String TAG = ProductDetailsActivity.class.getSimpleName();
    @BindView(R.id.recycler_view_id)RecyclerView recyclerView;
    @BindView(R.id.product_imgaes_layout) ConstraintLayout productImgLayout;
    @BindView(R.id.images_number_id)TextView imgCountTxt;
    @BindView(R.id.toolbar_home_id) ImageView toolbarHomeImg;
    @BindView(R.id.product_time_id) TextView productTimeTxt;
    @BindView(R.id.product_name_id)TextView productNameTxt;
    @BindView(R.id.product_img_id)ImageView productImgId;
    @BindView(R.id.product_price_id) TextView productPriceTxt;
    @BindView(R.id.product_desc_id) TextView productDescTxt;
    @BindView(R.id.producer_name_value_id) TextView producerNameTxt;
    @BindView(R.id.store_name_value_id) TextView storeNameTxt;
    @BindView(R.id.producer_phone_value_id) TextView producerPhoneTxt;
    @BindView(R.id.company_email_value_id) TextView companyEmailTxt;
    @BindView(R.id.send_msg_id) ImageView sendMsgImg;
    @BindView(R.id.toolbar_edit_id) ImageView toolbarEditImg;
    @BindView(R.id.toolbar_txt_id)TextView toolbarTxt;
    @BindView(R.id.empty_data_txt_id) TextView emptyDataTxt;
    @BindView(R.id.progress_id) ProgressBar progressBar;
    @BindView(R.id.wish_list_img)ImageView wishlistImg;
    ApiServiceInterface apiServiceInterface;
    int selected_product_id;
    Intent intent;
    NetworkAvailable networkAvailable;
    public static final String CATEGORY_PRODUCTS_DETAILS="category_products_details";
    //@BindView(R.id.view_pager_id) ViewPager viewPager;
    int position;
   ViewPager viewPager;

   DataArrayModel dataArrayModel;
    ProductDataItem item;

    ProductImagesListAdapter imagesListAdapter;
    List<ProductimagesItem> productimagesItems=new ArrayList<>();

    Dialog mDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mDailog = new Dialog(this);
         networkAvailable=new NetworkAvailable(this);
        toolbarHomeImg.setVisibility(GONE);


         intent = getIntent();
        if (intent.hasExtra(WISHLIST_PRODUCTS_DETAILS)) {

           getProductDetailsFromWishlist();

        } else if (intent.hasExtra("myads_model")) {
            toolbarEditImg.setVisibility(VISIBLE);
            toolbarHomeImg.setImageResource(R.drawable.nav_delete);
            toolbarHomeImg.setVisibility(VISIBLE);
            toolbarHomeImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDailog.setContentView(R.layout.delete_product_dialog_layout);
                    mDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mDailog.show();
                }
            });


            toolbarEditImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), EditAdsActivity.class));
                }
            });

        } else if (intent.hasExtra("store_product_details")) {
            item = intent.getParcelableExtra("store_product_details");
            inializeFieldsFormStoresProductDetails(item);
        } else if (intent.hasExtra(CATEGORY_PRODUCTS_DETAILS)) {

            getProductDetailsFromCategoryStores();
        }


        if(networkAvailable.isNetworkAvailable()){
            getProductImagesList();
            checkProductInWishlist();

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

    }




    private void removeWishlistItem() {
    apiServiceInterface=ApiClient.getClientService();
    Call<CheckWishlistModel> call=apiServiceInterface.deleteItemFromWishlist("application/json",LoginTraderUserActivity.loginModel.getTokenType()+" "+LoginTraderUserActivity.loginModel.getAccessToken(),selected_product_id);
    Log.v(TAG,"product selected  "+selected_product_id);
    call.enqueue(new Callback<CheckWishlistModel>() {
        @Override
        public void onResponse(Call<CheckWishlistModel> call, Response<CheckWishlistModel> response) {
            if(response.body().isSuccess()){
                mDailog.dismiss();
                finish();
                Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                Log.v(TAG,"product selected deleted "+selected_product_id);
            }else {
                Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                Log.v(TAG,"product selected not deleted "+selected_product_id);
            }
        }

        @Override
        public void onFailure(Call<CheckWishlistModel> call, Throwable t) {
            Log.v(TAG,"product selected failed "+selected_product_id);
            mDailog.dismiss();
        }
    });

    }


    @OnClick(R.id.product_imgaes_layout)
    void showProductImagesSlider(){
        mDailog.setContentView(R.layout.product_imges_slider_layout);
        mDailog.findViewById(R.id.spring_dots_indicator);
      viewPager=  mDailog.findViewById(R.id.view_pager_id);
        SpringDotsIndicator springDotsIndicator=mDailog.findViewById(R.id.spring_dots_indicator);
        ProductImagesSliderDialogAdapter adapter=new ProductImagesSliderDialogAdapter(ProductDetailsActivity.this,productimagesItems);
        viewPager.setAdapter(adapter);
        springDotsIndicator.setViewPager(viewPager);

      mDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       mDailog.show();

    }




    private void inializeFieldsFormStoresProductDetails(ProductDataItem item) {
        toolbarHomeImg.setVisibility(GONE);
        toolbarEditImg.setVisibility(GONE);


    }

    private void getProductDetailsFromWishlist(){
        dataArrayModel = intent.getParcelableExtra(WISHLIST_PRODUCTS_DETAILS);
        toolbarHomeImg.setVisibility(VISIBLE);
        toolbarHomeImg.setImageResource(R.drawable.nav_delete);
        selected_product_id=dataArrayModel.getProductId();
        if (PreferenceHelper.getValue(this).equals("en")) {
            productDescTxt.setText(dataArrayModel.getEnDescription());
            productNameTxt.setText(dataArrayModel.getEnName());
            toolbarTxt.setText(dataArrayModel.getEnName());
        } else if (PreferenceHelper.getValue(this).equals("ar")) {
            productDescTxt.setText(dataArrayModel.getArDescription());
            productNameTxt.setText(dataArrayModel.getArName());
            toolbarTxt.setText(dataArrayModel.getArName());
        }
        productPriceTxt.setText(String.valueOf(dataArrayModel.getPrice()));
        producerPhoneTxt.setText(dataArrayModel.getContactMobile());
        companyEmailTxt.setText(dataArrayModel.getContactEmail());
        producerNameTxt.setText(dataArrayModel.getContactName());
        sendMsgImg.setImageResource(R.drawable.send_message);


        toolbarHomeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDailog.setContentView(R.layout.delete_product_layout);
             MaterialButton deleteBtn= mDailog.findViewById(R.id.delete_btn_id);
             deleteBtn.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(networkAvailable.isNetworkAvailable()){
                         removeWishlistItem();
                     }else {
                         Toast.makeText(ProductDetailsActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                     }

                 }
             });

                mDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mDailog.show();

            }
        });

    }

    private void getProductDetailsFromCategoryStores() {
        dataArrayModel = intent.getParcelableExtra(CATEGORY_PRODUCTS_DETAILS);
        if (PreferenceHelper.getValue(this).equals("ar")) {
            toolbarTxt.setText(dataArrayModel.getArName());
            productNameTxt.setText(dataArrayModel.getArName());
            productDescTxt.setText(dataArrayModel.getArDescription());
        } else if (PreferenceHelper.getValue(this).equals("en")) {
            toolbarTxt.setText(dataArrayModel.getEnName());
            productNameTxt.setText(dataArrayModel.getEnName());
            productDescTxt.setText(dataArrayModel.getEnDescription());
        }
        productPriceTxt.setText(String.valueOf(dataArrayModel.getPrice()));
        position = intent.getIntExtra(POSITION, 0);
        selected_product_id=dataArrayModel.getProductId();
        producerPhoneTxt.setText(dataArrayModel.getContactMobile());
        companyEmailTxt.setText(dataArrayModel.getContactEmail());
        producerNameTxt.setText(dataArrayModel.getContactName());
    }


    void getProductImagesList(){
        progressBar.setVisibility(VISIBLE);
         apiServiceInterface= ApiClient.getClientService();
         Call<GetProductImagesModel> call=apiServiceInterface.getProductImagesList(selected_product_id);
         call.enqueue(new Callback<GetProductImagesModel>() {
             @Override
             public void onResponse(Call<GetProductImagesModel> call, Response<GetProductImagesModel> response) {
                 if(response.body().isSuccess()){
                     productimagesItems=response.body().getProductimagesItems();

                     if(productimagesItems.size()>0){
                         Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                         imagesListAdapter=new ProductImagesListAdapter(ProductDetailsActivity.this,productimagesItems);
                         recyclerView.setAdapter(imagesListAdapter);
                         progressBar.setVisibility(GONE);
                         imgCountTxt.setText(String.valueOf(productimagesItems.size()));
                     }else {
                         productImgLayout.setVisibility(GONE);
                         emptyDataTxt.setVisibility(VISIBLE);
                         progressBar.setVisibility(GONE);
                     }

                 }else {
                     Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                     emptyDataTxt.setVisibility(VISIBLE);
                     progressBar.setVisibility(GONE);
                 }
             }

             @Override
             public void onFailure(Call<GetProductImagesModel> call, Throwable t) {
                 emptyDataTxt.setVisibility(VISIBLE);
                 progressBar.setVisibility(GONE);
             }
         });
    }


    void checkProductInWishlist(){
    apiServiceInterface=ApiClient.getClientService();
    Call<CheckWishlistModel> call=apiServiceInterface.checkWishlistProduct(selected_product_id,"application/json", LoginTraderUserActivity.loginModel.getTokenType()+" "+LoginTraderUserActivity.loginModel.getAccessToken());
    call.enqueue(new Callback<CheckWishlistModel>() {
        @Override
        public void onResponse(Call<CheckWishlistModel> call, Response<CheckWishlistModel> response) {
            if(response.body().isSuccess()){
                Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                wishlistImg.setImageResource(R.drawable.wishlist_select);
            }else {
                wishlistImg.setImageResource(R.drawable.wishlist_not_select);
                Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<CheckWishlistModel> call, Throwable t) {

        }
    });
    }

    @OnClick(R.id.toolbar_back_left_btn_id)
    void backBtnClick(){
        startActivity(new Intent(this, StoresActivity.class));
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
}
