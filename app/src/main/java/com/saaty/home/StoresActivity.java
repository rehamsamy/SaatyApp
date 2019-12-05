package com.saaty.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.saaty.R;
import com.saaty.home.StoresProduct.StoreProductAdapter;
import com.saaty.home.StoresProduct.StoresProductsActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.CategoryModel;
import com.saaty.models.CheckWishlistModel;
import com.saaty.models.DataArrayModel;
import com.saaty.models.DataItem;
import com.saaty.models.StoreListModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.sideMenuScreen.wishlist.DealingWithWishList;
import com.saaty.sideMenuScreen.wishlist.WishlistActivity;
import com.saaty.sideMenuScreen.wishlist.WishlistAdapter;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;
import com.saaty.util.OnItemClickRecyclerViewInterface;
import com.saaty.util.PreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoresActivity extends AppCompatActivity implements OnItemClickInterface {

    private static final String TAG = StoresActivity.class.getSimpleName();
    @BindView(R.id.homeSearch_ed_id)
    SearchView searchView;
    @BindView(R.id.drawer_layout_id)
    DrawerLayout drawerLayout;
    @BindView(R.id.nv_id)
    NavigationView navigationView;
    @BindView(R.id.tab_layout_id)
    TabLayout tabLayout;
    @BindView(R.id.toolbar_id)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    StoreAdapter adapter;
    NetworkAvailable networkAvailable;
    DailogUtil dailogUtil;
    int flag;
    ApiServiceInterface apiServiceInterface;
    List<DataItem> categoryItem = new ArrayList();
    List<DataArrayModel> storesList=new ArrayList<>();
    @BindView(R.id.toolbar_txt_id)TextView toolbarTxt;
    @BindView(R.id.toolbar_back_left_btn_id)
    ImageView toolbarBackImg;
   @BindView(R.id.empty_data_txt_id)TextView emptyDataTxt;
   @BindView(R.id.progress_id) ProgressBar progressBar;
   private int current_page=1;
   private int tab_category_pos;
   StoreProductAdapter storeProductAdapter;
   List<DataArrayModel> categoryProductsList=new ArrayList<>();
   DealingWithWishList dealingWithWishList;
   Intent intent;
   List<DataArrayModel> wishlistProducts;
   List<Integer> ids=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        ButterKnife.bind(this);

        toolbarBackImg.setImageResource(R.drawable.nav_home);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setTitle("");
        toggle.syncState();
        dailogUtil = new DailogUtil();
        networkAvailable = new NetworkAvailable(this);
        dealingWithWishList=new DealingWithWishList(getApplicationContext());
        toolbarTxt.setText(getString(R.string.stores_toolbar));

        //ids=new ArrayList<>();

        intent=getIntent();
        if(intent.hasExtra("category_id1")){
            //TabLayout.Tab tab = tabLayout.getTabAt(1);
            flag=1;

        }else if(intent.hasExtra("category_id2")){
            flag=2;
        }


        if(networkAvailable.isNetworkAvailable()){

            tabLayout.addTab(tabLayout.newTab().setText("    " +  getString(R.string.store)+"   "),0);
            categoryProductsList.clear();
            storesList.clear();
            buildRecyclerViewForStoreList();
            getStoreList(current_page);
            getCateogry();


        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
        if(HomeActivity.user_id!=0){
            getWishList();
        }else if(HomeActivity.user_id==0){
            return;
        }




//       // TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        TabLayout.Tab tab = tabLayout.getTabAt(someIndex);
//        tab.select();




        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               tab_category_pos=tab.getPosition();
               if(tab_category_pos==0){
                   Log.v(TAG,"position"+tab_category_pos);
                   current_page=1;
                   storesList.clear();
                   categoryProductsList.clear();
                   buildRecyclerViewForStoreList();
                   getStoreList(current_page);
               }else {
                   Log.v(TAG,"position"+tab_category_pos);
                   current_page=1;
                   storesList.clear();
                   categoryProductsList.clear();
                   buildRecyclerViewForCategory();
                   getCateogryProductsList(current_page,tab.getPosition());
               }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void getStoreList(int current_page) {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface=ApiClient.getClientService();
            progressBar.setVisibility(View.VISIBLE);
            Map<String,Object> map=new HashMap<>();
            map.put("page",current_page);
            map.put("limit",10);
           Call<StoreListModel> call= apiServiceInterface.getStoresList(map);
           call.enqueue(new Callback<StoreListModel>() {
               @Override
               public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                   if(response.body().isSuccess()){
                       storesList.addAll(response.body().getDataObjectModel().getDataArrayModelList());
                       if(storesList.size()>0) {
                           adapter.notifyDataSetChanged();
                           Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                       }else {
                           emptyDataTxt.setVisibility(View.VISIBLE);
                           progressBar.setVisibility(View.GONE);
                       }
                   }else {
                       Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       progressBar.setVisibility(View.GONE);
                   }
               }

               @Override
               public void onFailure(Call<StoreListModel> call, Throwable t) {
                   Toast.makeText(StoresActivity.this,t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                   progressBar.setVisibility(View.GONE);
               }
           });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }


    private void buildRecyclerViewForStoreList() {
        GridLayoutManager layoutManager=new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new StoreAdapter(getApplicationContext(), storesList, StoresActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               current_page++;
               getStoreList(current_page);
            }
        });

    }



    private void getCateogry() {
        if(networkAvailable.isNetworkAvailable()){
            ProgressDialog progressDialog=dailogUtil.showProgressDialog(StoresActivity.this,getString(R.string.logging),false);
            apiServiceInterface= ApiClient.getClientService();
            Call<CategoryModel> call=apiServiceInterface.getCatogory();
            call.enqueue(new Callback<CategoryModel>() {
                @Override
                public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                    if(response.body().isSuccess()){
                        Log.v(TAG,"category tabs");
                        Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        categoryItem=response.body().getData();
                        for(int i=0;i<categoryItem.size();i++){
                            if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")){
                                tabLayout.addTab(tabLayout.newTab().setText("          " +response.body().getData().get(i).getCategoryArName().toString()+"         "));
                              flag=1;
                            }else if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
                                tabLayout.addTab(tabLayout.newTab().setText("          " +response.body().getData().get(i).getCategoryEnName().toString()+"         "));
                                flag=2;
                            }

                        }
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Log.v(TAG,"category tabs error");
                    progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<CategoryModel> call, Throwable t) {
                    Toast.makeText(StoresActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    Log.v(TAG,"eeeeeeeeeee"+t.getMessage().toString());
               progressDialog.dismiss();
                }
            });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }



    private void getCateogryProductsList(int current_page, int position) {
        if(networkAvailable.isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            apiServiceInterface=ApiClient.getClientService();
            Map<String,Object> map=new HashMap<>();
            map.put("category_id",position);
            map.put("page",current_page);
            map.put("limit",10);
            Call<StoreListModel> call=apiServiceInterface.getCategoryProductsList(map);
            call.enqueue(new Callback<StoreListModel>() {
                @Override
                public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                    if(response.body().isSuccess()){

                        categoryProductsList.addAll(response.body().getDataObjectModel().getDataArrayModelList());
                        storeProductAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }else {
                            if(response.body().getDataObjectModel().getDataArrayModelList().size()==0){
                                progressBar.setVisibility(View.GONE);
                                emptyDataTxt.setVisibility(View.VISIBLE);
                            }
                        Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<StoreListModel> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

    }

    void buildRecyclerViewForCategory() {

       GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        storeProductAdapter =new StoreProductAdapter(this,categoryProductsList);

      if(wishlistProducts.size()>0) {
          storeProductAdapter.setIds(ids);
      }

       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(layoutManager);
       recyclerView.setAdapter(storeProductAdapter);

       buildOnClickListener();
       recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
           @Override
           public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               current_page++;
               getCateogryProductsList(current_page,tab_category_pos);
           }
       });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), StoresProductsActivity.class);
        intent.setAction("store_products_details");
        int id = storesList.get(position).getStoreId();
        String arName = storesList.get(position).getStoreArName();
        String enName=storesList.get(position).getStoreEnName();
        intent.putExtra("store_id", id);
        intent.putExtra("store_name_ar", arName);
        intent.putExtra("store_name_en", enName);
        intent.putExtra("store_img",storesList.get(position).getStoreLogo());
        startActivity(intent);
    }



    private void buildOnClickListener() {
        storeProductAdapter.setOnItemClickListenerRecyclerView(new OnItemClickRecyclerViewInterface() {
            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
                Log.v(TAG,"pos"+categoryProductsList.get(position).getArName());
                dealingWithWishList.addToWishList(categoryProductsList.get(position),progressBar,wishlist_image);
            }

            @Override
            public void OnItemClick(int position) {
                DataArrayModel item=categoryProductsList.get(position);
                Intent intent=new Intent(getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.CATEGORY_PRODUCTS_DETAILS,item);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.toolbar_back_left_btn_id)
    void toolbarbackClick(){
        finish();
    }

    private void getWishList() {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface= ApiClient.getClientService();
            progressBar.setVisibility(View.VISIBLE);
            Map<String,Object> map=new HashMap<>();
            String token= LoginTraderUserActivity.loginModel.getTokenType()+" "+LoginTraderUserActivity.loginModel.getAccessToken();
//            map.put("page",currentPage);
            map.put("page",1);
            map.put("limit",100);
            Call<StoreListModel> call=apiServiceInterface.getWishlist(map,"application/json",token);
            call.enqueue(new Callback<StoreListModel>() {
                @Override
                public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                    if(response.body().isSuccess()){
                        if(response.body().isSuccess()) {
                            wishlistProducts=response.body().getDataObjectModel().getDataArrayModelList();
                            if(wishlistProducts.size()>0) {
                                Log.v("TAG","ssss"+wishlistProducts.size());

                                for(int i=0;i<wishlistProducts.size();i++){
                                    ids.add(wishlistProducts.get(i).getProductId());
                                    Log.v("TAG","iddddddddd"+ids.size());

                                }


                            }
                        }else if(wishlistProducts.size()==0) {
                            recyclerView.setVisibility(View.GONE);
                            //emptyData.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            // }
                        }
//

                    }
                    else {
                        //Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<StoreListModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
    }

}
