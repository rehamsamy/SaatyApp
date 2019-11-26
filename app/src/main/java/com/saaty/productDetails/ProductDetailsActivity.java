package com.saaty.productDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.appbar.AppBarLayout;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.models.ProductDataItem;
import com.saaty.util.OnItemClickInterface;
import com.saaty.util.PreferenceHelper;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.recyclerview.widget.RecyclerView.*;

public class ProductDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    @BindView(R.id.recycler_view_id)RecyclerView recyclerView;
    @BindView(R.id.toolbar_home_id) ImageView toolbarHomeImg;
    @BindView(R.id.product_time_id) TextView productTimeTxt;
    @BindView(R.id.product_name_id)TextView productNameTxt;
    @BindView(R.id.product_price_id) TextView productPriceTxt;
    @BindView(R.id.product_desc_id) TextView productDescTxt;
    @BindView(R.id.producer_name_value_id) TextView ProducerNameTxt;
    @BindView(R.id.store_name_value_id) TextView storeNameTxt;
    @BindView(R.id.producer_phone_value_id) TextView producerPhoneTxt;
    @BindView(R.id.company_email_value_id) TextView companyEmailTxt;
    @BindView(R.id.send_msg_id) ImageView sendMsgImg;


    ProductImagesListAdapter imagesListAdapter;
    private SliderLayout sliderLayout;
    Dialog mDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        imagesListAdapter=new ProductImagesListAdapter(this);
        LayoutManager manager=new LinearLayoutManager(this);
       recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,true));
       recyclerView.setAdapter(imagesListAdapter);
       mDailog=new Dialog(this);

       toolbarHomeImg.setVisibility(GONE);

        Intent intent=getIntent();
        if(intent.hasExtra("wishlist_model")){
            ProductDataItem model=intent.getParcelableExtra("wishlist_model");
            toolbarHomeImg.setVisibility(VISIBLE);
            toolbarHomeImg.setImageResource(R.drawable.nav_delete);
            if(PreferenceHelper.getValue(this).equals("en")){
           productDescTxt.setText(model.getEnDescription());
           productNameTxt.setText(model.getEnName());
            }
            else if(PreferenceHelper.getValue(this).equals("ar")){
                productDescTxt.setText(model.getArDescription());
                productNameTxt.setText(model.getArName());
            }
            productPriceTxt.setText(String.valueOf(model.getPrice()));
            producerPhoneTxt.setText(model.getContactMobile());
            companyEmailTxt.setText(model.getContactEmail());
            ProducerNameTxt.setText(model.getContactName());
            sendMsgImg.setImageResource(R.drawable.send_message);
//            //2019-11-12 14:15:26
//            DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
//           // Date date=new Date("yyyy.MM.dd hh:mm:ss");
//            Date date1 = new java.util.Date("yyyy.MM.dd hh:mm:ss");
//            Date date2 = null;
//            long diff = 0;
//            try {
//                date2 = df.parse("00:00:00_2013.01.01");
//                 diff = (date2.getTime() - date1.getTime())/ 1000 / 60 / 60 / 24 ;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            toolbarHomeImg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder  builder=new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage(getString(R.string.remove_wishlist_item));
                    builder.setTitle(getString(R.string.remove_wishlist_title))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    removeWishlistItem();
                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();




                }
            });

           // productTimeTxt.setText(String.valueOf(diff));

        }

    }

    private void removeWishlistItem() {
    }


    @OnClick(R.id.product_imgaes_layout)
    void showProductImagesSlider(){
        mDailog.setContentView(R.layout.product_imges_slider_layout);
        sliderLayout = mDailog.findViewById(R.id.slider_layout_id);
        int[] x = {R.drawable.splash_photo_3, R.drawable.splash_photo_2, R.drawable.splash_photo_1, R.drawable.bracelets_item1
                , R.drawable.bracelets_item2, R.drawable.watch_item1, R.drawable.watch_item2, R.drawable.setting_photo};
        for (int i = 0; i < x.length; i++) {
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
            textSliderView.image(x[i])
                    .setOnSliderClickListener(ProductDetailsActivity.this)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(ProductDetailsActivity.this);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setCustomIndicator((PagerIndicator)mDailog.findViewById(R.id.custom_indicator));

//        sliderLayout.setCustomAnimation(new DescriptionAnimation());
//        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        sliderLayout.setCustomIndicator((PagerIndicator)mDailog.findViewById(R.id.custom_indicator));
//        sliderLayout.setDuration(4000);
//        sliderLayout.addOnPageChangeListener(ProductDetailsActivity.this);
        mDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDailog.show();

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
