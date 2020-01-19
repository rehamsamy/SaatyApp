package com.saaty.sideMenuScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.saaty.R;
import com.saaty.models.AdvsModel;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.NetworkAvailable;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {

    @BindView(R.id.about_app_txt) TextView aboutAppTxt;
    @BindView(R.id.progress_id)
    ProgressBar progressBar;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    ApiServiceInterface apiServiceInterface;
    NetworkAvailable networkAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
        networkAvailable=new NetworkAvailable(this);
        toolbarTxt.setText(getString(R.string.terms_conditions));
        if (networkAvailable.isNetworkAvailable()) {
            getTerms();
        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();

    }


    }


    private void getTerms() {
        apiServiceInterface = ApiClient.getClientService();
        Map<String, Object> map = new HashMap<>();
        map.put("key", "terms");
        Call<AdvsModel> call = apiServiceInterface.getTerms(map);
        call.enqueue(new Callback<AdvsModel>() {
            @Override
            public void onResponse(Call<AdvsModel> call, Response<AdvsModel> response) {
                if (response.body().isSuccess()) {
                    AdvsModel model = response.body();
                    Log.v("TAG","wwwww"+model.getAdvData().get(0).getKey());
                    aboutAppTxt.setText(model.getAdvData().get(0).getValue1());


                }
            }

            @Override
            public void onFailure(Call<AdvsModel> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.toolbar_back_left_btn_id)
    void backClick(){
        finish();
    }

    @OnClick(R.id.toolbar_home_id)
    void homeClick(){
        finish();
    }


}
