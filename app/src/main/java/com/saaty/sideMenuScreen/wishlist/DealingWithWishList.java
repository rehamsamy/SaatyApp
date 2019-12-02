package com.saaty.sideMenuScreen.wishlist;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saaty.R;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.AdsProductsModel;
import com.saaty.models.DataArrayModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealingWithWishList {

Context mContext;

    public DealingWithWishList(Context mContext) {
        this.mContext = mContext;
    }

    public void addToWishList(DataArrayModel item, ProgressBar progressBar, ImageView imageView){
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface apiServiceInterface= ApiClient.getClientService();
      Call<AdsProductsModel> call= apiServiceInterface.addProuctToWishlist("application/json",
              LoginTraderUserActivity.loginModel.getTokenType()+" "+LoginTraderUserActivity.loginModel.getAccessToken()
        ,item.getProductId());
        call.enqueue(new Callback<AdsProductsModel>() {
            @Override
            public void onResponse(Call<AdsProductsModel> call, Response<AdsProductsModel> response) {
                if(response.body().isSuccess()){
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    imageView.setImageResource(R.drawable.wishlist_select);
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                       progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AdsProductsModel> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}
