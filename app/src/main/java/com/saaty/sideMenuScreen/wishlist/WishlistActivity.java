package com.saaty.sideMenuScreen.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.saaty.PaginationScrollListener;
import com.saaty.R;
import com.saaty.home.StoresActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.CheckWishlistModel;
import com.saaty.models.DataArrayModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.models.SendCodeModel;
import com.saaty.models.StoreListModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;
import com.saaty.util.OnItemClickRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity  {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_id) ProgressBar progressBar;
    @BindView(R.id.empty_data_txt_id) TextView emptyData;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    @BindView(R.id.homeSearch_ed_id) SearchView searchEditTxt;
    Dialog mDialog;
    List<DataArrayModel> wishlistProducts;
    WishlistAdapter wishlistAdapter;
    ApiServiceInterface apiServiceInterface;
    int current_page;
    int selected_product_id;
    NetworkAvailable networkAvailable;

    private GridLayoutManager layoutManager;

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;

    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;

    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES;

    // indicates the current page which Pagination is fetching.
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mDialog=new Dialog(this);
        ButterKnife.bind(this);
        networkAvailable=new NetworkAvailable(getApplicationContext());
        toolbarTxt.setText(getString(R.string.wishlist));
        searchEditTxt.clearFocus();
        wishlistProducts=new ArrayList<>();
        initializeComponents();
        //buildWishlisrRecycler();
        getWishList();


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
                                     GridLayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
                                     recyclerView.setLayoutManager(layoutManager);
                                     recyclerView.setItemAnimator(new DefaultItemAnimator());
                                     recyclerView.setHasFixedSize(true);
                                     wishlistAdapter=new WishlistAdapter(WishlistActivity.this,wishlistProducts);
                                     recyclerView.setAdapter(wishlistAdapter);
                                     Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();


                                     buildOnClickInterface();
                                     progressBar.setVisibility(View.GONE);
                            }
                        }else if(wishlistProducts.size()==0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyData.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                       // }
                        }
//

                    }
                       else {
                       Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    private void buildOnClickInterface() {
        wishlistAdapter.setOnItemClickListenerRecyclerView(new OnItemClickRecyclerViewInterface() {
            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
               deleteItemFromWishList(wishlistProducts.get(position).getProductId());
            }

            @Override
            public void OnItemClick(int position) {
                Intent intent=new Intent(WishlistActivity.this, ProductDetailsActivity.class);
                DataArrayModel item= wishlistProducts.get(position);
                intent.putExtra(ProductDetailsActivity.WISHLIST_PRODUCTS_DETAILS,item);
                startActivity(intent);
            }
        });
    }

    private void deleteItemFromWishList(int selected_product_id) {
        apiServiceInterface=ApiClient.getClientService();
        Call<SendCodeModel> call=apiServiceInterface.deleteItemFromWishlist(selected_product_id
                ,"application/json"
                ,LoginTraderUserActivity.loginModel.getTokenType()+" "+LoginTraderUserActivity.loginModel.getAccessToken());
        Log.v("TAG","product selected  "+selected_product_id);
        call.enqueue(new Callback<SendCodeModel>() {
            @Override
            public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                if (response.code() == 404) {
                    Toast.makeText(WishlistActivity.this, "error ocuured", Toast.LENGTH_LONG).show();

                } else {
                    if (response.body().isSuccess()) {
                        Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        getWishList();
                        Log.v("TAG", "product selected deleted " + selected_product_id);
                    } else {
                        Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Log.v("TAG", "product selected not deleted " + selected_product_id);
                    }
                }
            }
            @Override
            public void onFailure(Call<SendCodeModel> call, Throwable t) {
                Log.v("TAG","product selected failed "+selected_product_id);

            }
        });
    }

    private void buildWishlisrRecycler() {
        GridLayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
         recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        wishlistAdapter=new WishlistAdapter(WishlistActivity.this,wishlistProducts);
        recyclerView.setAdapter(wishlistAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                current_page=current_page+1;

                    getWishList();


            }
        });

    }


    protected void initializeComponents() {


        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        wishlistAdapter= new WishlistAdapter(getApplicationContext(),wishlistProducts);
        recyclerView.setAdapter(wishlistAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                getWishList();
            }


            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage=1;
        getWishList();
    }

    @OnClick(R.id.nav_filter_id)
    void navFilterDailog(){
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.filter_sort_dialog_layout);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }


}
