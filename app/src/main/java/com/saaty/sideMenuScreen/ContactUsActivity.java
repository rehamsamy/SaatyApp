package com.saaty.sideMenuScreen;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saaty.R;
import com.saaty.models.ProductDataModel;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.NetworkAvailable;

import java.util.HashMap;
import java.util.Map;

public class ContactUsActivity extends AppCompatActivity {

    ApiServiceInterface apiServiceInterface;
    NetworkAvailable networkAvailable;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    @BindView(R.id.phone_number_value_txt) TextView phoneNumTxt;
    @BindView(R.id.address_value_txt) TextView addressTxt;
    @BindView(R.id.email_value_txt) TextView emailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        networkAvailable=new NetworkAvailable(this);
        toolbarTxt.setText(getString(R.string.contact_us));
        getAboutAppData();

       // getContactDetails();

    }



    private void getAboutAppData() {
        if (networkAvailable.isNetworkAvailable()) {
            apiServiceInterface = ApiClient.getClientService();
            Map<String, Object> map = new HashMap<>();
            map.put("key","contactus");
            Call<ProductDataModel> call = apiServiceInterface.getAboutAppData(map);
            call.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    if (response.body().isSuccess()) {
                       // Toast.makeText(ContactUsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                          phoneNumTxt.setText(response.body().getProductDataModels().get(0).getValue1());
                        emailTxt.setText(response.body().getProductDataModels().get(0).getValue2());
                        addressTxt.setText(response.body().getProductDataModels().get(0).getValue3());

                    } else {
                        Toast.makeText(ContactUsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ProductDataModel> call, Throwable t) {

                }
            });


        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
    }



    private void getContactDetails() {

        if (networkAvailable.isNetworkAvailable()) {
            apiServiceInterface = ApiClient.getClientService();
            Map<String, Object> map = new HashMap<>();
            map.put("key","contactus");
            Call<ProductDataModel> call = apiServiceInterface.getContactDetails("contactus");
            call.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    if (response.body().isSuccess()) {
                        //Toast.makeText(ContactUsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ContactUsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ProductDataModel> call, Throwable t) {

                }
            });


        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

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
