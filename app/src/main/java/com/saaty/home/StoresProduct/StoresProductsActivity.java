package com.saaty.home.StoresProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.saaty.R;
import com.saaty.models.DataArrayModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.StoreListModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.sideMenuScreen.wishlist.DealingWithWishList;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickRecyclerViewInterface;
import com.saaty.util.PreferenceHelper;
import com.saaty.util.urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoresProductsActivity extends AppCompatActivity {

    private static final String TAG = StoresProductsActivity.class.getSimpleName();
    @BindView(R.id.tab_layout_id) TabLayout tabLayout;
    @BindView(R.id.toolbar_txt_id)TextView toolbarName;
    @BindView(R.id.nav_filter_id) ImageView navFilter;
    @BindView(R.id.store_name_id) TextView storeName;
    @BindView(R.id.store_img_id) CircleImageView storeImgId;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_data_txt_id)TextView emptyData;
    int tab_selected;
    @BindView(R.id.progress_id)
    ProgressBar progressBar;
    String shape_type="New";
    int current_page=1;
    StoreProductsPagerAdaper pagerAdaper;
    DealingWithWishList dealingWithWishList;
    ApiServiceInterface apiServiceInterface;
    List<DataArrayModel> storeProductsList=new ArrayList<>();
    List<DataArrayModel> newProducts=new ArrayList<>();
    NetworkAvailable networkAvailable;
    StoreProductAdapter adapter;
    Dialog mDialog;
    String store_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_products);
        ButterKnife.bind(this);
        //tabLayout.setupWithViewPager(viewPager);
        networkAvailable=new NetworkAvailable(this);
       // pagerAdaper=new StoreProductsPagerAdaper(getSupportFragmentManager());
       // viewPager.setAdapter(pagerAdaper);
        dealingWithWishList=new DealingWithWishList(this);
        mDialog=new Dialog(this);
        Intent intent=getIntent();
        if (intent.getAction().equals("store_products_details")) {
          if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")) {
              store_name = intent.getStringExtra("store_name_ar");
              toolbarName.setText(store_name);
              storeName.setText(store_name);
          }else if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
              store_name = intent.getStringExtra("store_name_en");
              toolbarName.setText(store_name);
              storeName.setText(store_name);
          }
            Picasso.with(getApplicationContext()).load(urls.base_url+"/"+intent.getStringExtra("store_img")).into(storeImgId);
        }
       if(networkAvailable.isNetworkAvailable()){
           tabLayout.addTab(tabLayout.newTab().setText("         New       "),0);
           //newProducts=new ArrayList<>();
          // storeProductsList=new ArrayList<>();
           newProducts.clear();
           buildRecyclerViewForCategory();
           getStoreProducts(1,"New");
           tabLayout.addTab(tabLayout.newTab().setText("          Used      "),1);

       }else {
           Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
       }

       tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               tab_selected=tab.getPosition();
               if(tab.getPosition()==0){
                   shape_type="New";
                   Log.v(TAG,"shape selected"+tab.getPosition());
                  // buildRecyclerViewForCategory();
                 //  getStoreProducts(current_page,shape_type);
               }else if(tab.getPosition()==1){
                   shape_type="Used";
                   Log.v(TAG,"shape selected"+tab.getPosition());
               }
               newProducts.clear();
               current_page=1;
               buildRecyclerViewForCategory();
               getStoreProducts(current_page,shape_type);
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });





    }

    @OnClick(R.id.nav_filter_id)
    void navFilterClick(){
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.filter_sort_dialog_layout);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }



    private void getStoreProducts(int current_page,String shape_type) {
        if(networkAvailable.isNetworkAvailable()){
            // ProgressDialog progressDialog=dailogUtil.showProgressDialog(getActivity(),getString(R.string.logging),false);
            progressBar.setVisibility(View.VISIBLE);
            apiServiceInterface= ApiClient.getClientService();
            Map<String,Object> map=new HashMap<>();
            map.put("page",1);
            map.put("limit",10);
            Call<StoreListModel> call=apiServiceInterface.gerStoreProducts(1,map);
            call.enqueue(new Callback<StoreListModel>() {
                @Override
                public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                    if(response.body().isSuccess()){
                        storeProductsList.addAll(response.body().getDataObjectModel().getDataArrayModelList());

                        for(int i=0;i<storeProductsList.size();i++) {
                            Log.v(TAG,"not matchhhhh \n"+storeProductsList.get(i).getShape());

                            if(storeProductsList.get(i).getShape().equals("New")&&shape_type.equals("New")){
                                newProducts.add(storeProductsList.get(i));
                                adapter.notifyDataSetChanged();
                                Log.v(TAG,"new list"+newProducts.size());

                            }else if(storeProductsList.get(i).getShape().equals("Used")&&shape_type.equals("Used")){
                                newProducts.add(storeProductsList.get(i));
                                adapter.notifyDataSetChanged();
                            }else {
                                emptyData.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }

                        }


                        //emptyData.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        Log.v(TAG,"dddddddddd"+storeProductsList.size());
                        Toast.makeText(StoresProductsActivity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(StoresProductsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<StoreListModel> call, Throwable t) {
                    Toast.makeText(StoresProductsActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            Toast.makeText(StoresProductsActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

    }



    void buildRecyclerViewForCategory() {
        GridLayoutManager layoutManager=new GridLayoutManager(StoresProductsActivity.this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter =new StoreProductAdapter(StoresProductsActivity.this,newProducts);
        recyclerView.setAdapter(adapter);

        buildOnClickListener();

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(tab_selected==0){
                    current_page++;
                    getStoreProducts(current_page,shape_type);
                }
                 }
        });
    }

        private void buildOnClickListener() {
        adapter.setOnItemClickListenerRecyclerView(new OnItemClickRecyclerViewInterface() {
            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
         dealingWithWishList.addToWishList(newProducts.get(position),progressBar,wishlist_image);
            }

            @Override
            public void OnItemClick(int position) {
              DataArrayModel item=storeProductsList.get(position);
                Intent intent=new Intent(getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.CATEGORY_PRODUCTS_DETAILS,item);
                startActivity(intent);
            }
        });

    }


}

