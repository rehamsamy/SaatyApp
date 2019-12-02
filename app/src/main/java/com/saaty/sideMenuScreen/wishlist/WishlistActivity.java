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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.saaty.PaginationScrollListener;
import com.saaty.R;
import com.saaty.home.StoresActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.DataArrayModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.models.StoreListModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity implements OnItemClickInterface {

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
       // buildWishlisrRecycler();
        getWishList();


    }

    private void getWishList() {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface= ApiClient.getClientService();
            progressBar.setVisibility(View.VISIBLE);
            Map<String,Object> map=new HashMap<>();
          String token= LoginTraderUserActivity.loginModel.getTokenType()+" "+LoginTraderUserActivity.loginModel.getAccessToken();
            map.put("page",currentPage);
            map.put("limit",10);
            Call<StoreListModel> call=apiServiceInterface.getWishlist(map,"application/json",token);
                    call.enqueue(new Callback<StoreListModel>() {
                @Override
                public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                    if(response.body().isSuccess()){
                        if(response.body().getDataObjectModel().getDataArrayModelList().size()>0) {
                            wishlistProducts.addAll(response.body().getDataObjectModel().getDataArrayModelList());
                            wishlistAdapter.notifyDataSetChanged();




                            Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                           // wishlistProducts.addAll(response.body().getDataObjectModel().getDataArrayModelList());
//                            wishlistAdapter.notifyItemRangeInserted(wishlistAdapter.getItemCount(),wishlistProducts.size()-1);
//
                           if(wishlistProducts.size()>0) {
                                          progressBar.setVisibility(View.GONE);
                            }
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            emptyData.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                       // }
                        }
//

                    }
                       else {
                       Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    private void buildWishlisrRecycler() {
        GridLayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
         recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        wishlistAdapter=new WishlistAdapter(WishlistActivity.this,wishlistProducts,WishlistActivity.this);
        recyclerView.setAdapter(wishlistAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                current_page=current_page+1;
                if(wishlistProducts.size()<10){
                    getWishList();
                }else {
                    getWishList();
                }

            }
        });

    }


    protected void initializeComponents() {


        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        wishlistAdapter= new WishlistAdapter(getApplicationContext(),wishlistProducts,WishlistActivity.this);
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

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(WishlistActivity.this, ProductDetailsActivity.class);
       DataArrayModel item= wishlistProducts.get(position);
        intent.putExtra(ProductDetailsActivity.WISHLIST_PRODUCTS_DETAILS,item);
        startActivity(intent);
    }
}
