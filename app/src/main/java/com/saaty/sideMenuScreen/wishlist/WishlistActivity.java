package com.saaty.sideMenuScreen.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saaty.R;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;

import java.util.List;

public class WishlistActivity extends AppCompatActivity implements OnItemClickInterface {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_id) ProgressBar progressBar;
    @BindView(R.id.empty_data_txt_id) TextView emptyData;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    Dialog mDialog;
    List<ProductDataItem> wishlistProducts;
    WishlistAdapter wishlistAdapter;
    ApiServiceInterface apiServiceInterface;
    NetworkAvailable networkAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mDialog=new Dialog(this);
        ButterKnife.bind(this);
        networkAvailable=new NetworkAvailable(getApplicationContext());
        toolbarTxt.setText(getString(R.string.wishlist));
        getWishList();

    }

    private void getWishList() {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface= ApiClient.getClientService();
            progressBar.setVisibility(View.VISIBLE);
            Call<ProductDataModel> call=apiServiceInterface.getWishlist("application/json","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjI3ZDg2MjYyZjE4MDU1Y2FlNGMzNmU5YTcxZDg3ZTc3MThmMjdlOTI2NmE1ZjFkNDE4ZWI5ODNlY2RhMGZmOWNmMDkwMzkxYzdjNzVjYTQzIn0.eyJhdWQiOiIyIiwianRpIjoiMjdkODYyNjJmMTgwNTVjYWU0YzM2ZTlhNzFkODdlNzcxOGYyN2U5MjY2YTVmMWQ0MThlYjk4M2VjZGEwZmY5Y2YwOTAzOTFjN2M3NWNhNDMiLCJpYXQiOjE1NzQyNDgzMzEsIm5iZiI6MTU3NDI0ODMzMSwiZXhwIjoxNjA1ODcwNzMxLCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.AOzPvH32Mc4Nq_ierPEy2W2zGqMBWxkSwxpGu3Uigs324eInnQ5DvjssxOyCMxHX8JDaxJ3GQSKTTwjGSz7xp99SKNf43hrCv1sLd3d4EiCC_pHsHceiAzK1I3Veg0wj_YPRh_DBbreycpmdw9pz8TAjmJp47L1JUbS90uVc-WjHZIPQ2xAWkurSblxcP8fryAdkJ8e3S7P9iTU9-pFGQ5DqllO0ORJZGF76dMxJIWuW7VcWxYUtWd-z5c27S_HGVmzee_ZVt3MpL1-1H947FmEb30ZR7FCT3uSbY4VgDMg0ttem_IWqUQJKJirxdO4fWxXwyKcxpxEHkGE9LCcONjKpTLbqVnyeNrpGFuNOSvTOHtjLaf8N0IM4d_jFNrOLcNEOQvtCDaDIG-Q81PhF9aYWHMw86fXJWXRWCWcbgV-5MPOmcYnvfM6GQaLHKM25HOoJ8RS2VYikGeCZaVFPQrMe4bS07ybJFQtD5WfmsMm26mZZpGGNexMa9qbN_77BJB49NtSTZKMgrRiEwYWwT9_FwURo-m483J-8q5DpBR_CfQXjuBD_H08VjuL3DbC63r3RHnJICKh4HIVgeBYyw-Xua-7q-DBfXdyva8G9oAs6enuEnzg9pKi3leLAE9NINzQBk_NGRw0LzD7VPqQkUEVDrJpfr94F2phov23wQPA");
            call.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    if(response.body().isSuccess()){
                        if(response.body().getProductDataModels().size()>0){
                            Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            wishlistProducts=response.body().getProductDataModels();
                            wishlistAdapter=new WishlistAdapter(WishlistActivity.this,wishlistProducts,WishlistActivity.this);
                            recyclerView.setLayoutManager(new GridLayoutManager(WishlistActivity.this,2));
                            recyclerView.setAdapter(wishlistAdapter);
                            progressBar.setVisibility(View.GONE);
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            emptyData.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }


                    }else {
                        Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ProductDataModel> call, Throwable t) {

                }
            });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
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
       ProductDataItem item= wishlistProducts.get(position);
        intent.putExtra("wishlist_model",item);
        startActivity(intent);
    }
}
