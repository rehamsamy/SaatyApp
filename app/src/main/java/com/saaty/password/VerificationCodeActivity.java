package com.saaty.password;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.loginAndRegister.TraderRegisterFragment;
import com.saaty.loginAndRegister.UserRegisterFragment;
import com.saaty.models.Data;
import com.saaty.models.SendCodeModel;
import com.saaty.models.UserDataRegisterObject;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.NetworkAvailable;

import java.util.HashMap;
import java.util.Map;

public class VerificationCodeActivity extends AppCompatActivity {

    private static final String TAG = VerificationCodeActivity.class.getSimpleName();
    public static String VERIFY_CHECK="verify_check";
    @BindView(R.id.verify_code_input_id) TextInputEditText verifyCodeInputId;
    @BindView(R.id.toolbar_txt_id) TextView toolbarText;
    @BindView(R.id.resend_code_txt)TextView resendCodeTxt;
    NetworkAvailable networkAvailable;
    DailogUtil dailogUtil;
    ApiServiceInterface apiServiceInterface;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String phoneNumber;
    Data data;
    String token;
    String type;
    public  static String x;
int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);
        dailogUtil=new DailogUtil();
        networkAvailable=new NetworkAvailable(this);

       //prefs= getSharedPreferences(VerificationCodeActivity.VERIFY_CHECK,MODE_PRIVATE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(getIntent().hasExtra("phone")){
            phoneNumber=getIntent().getStringExtra("phone");
            toolbarText.setText(getString(R.string.reset_password));
            flag=1;
        }else if(getIntent().hasExtra("register_store_model")){
            data=getIntent().getParcelableExtra("register_store_model");
            token=data.getToken();
            toolbarText.setText(getString(R.string.verify_code_toolbar));
            resendCodeTxt.setVisibility(View.GONE);
           flag=2;

        }else if(getIntent().hasExtra("register_user_model")){
            data=getIntent().getParcelableExtra("register_user_model");
            token=data.getToken();
            toolbarText.setText(getString(R.string.verify_code_toolbar));
            resendCodeTxt.setVisibility(View.GONE);
            flag=3;

        }else if(getIntent().hasExtra("register_user")){
           data=getIntent().getParcelableExtra("register_user");
            toolbarText.setText(getString(R.string.verify_code_toolbar));
            resendCodeTxt.setVisibility(View.GONE);
            flag = 4;
        } else if (getIntent().hasExtra("register_trader")) {
            data=getIntent().getParcelableExtra("register_trader");
            toolbarText.setText(getString(R.string.verify_code_toolbar));
            resendCodeTxt.setVisibility(View.GONE);
            flag = 5;
        }

            resendCodeTxt.setVisibility(View.GONE);



    }


    @OnClick(R.id.confirm_btn_id)
    void confirmClick(){
        if(flag==1){
            retreiveConfirmationCode();
        }else if(flag==2){
            getVerificationFromEmailRegisterTrader(data);

        }
        else if(flag==3){
            getVerificationFromEmailRegisterUser(data);

        }else if(flag==4){
            getVerificationFromEmailRegisterUser(data);
        }
        else if(flag==5){
            getVerificationFromEmailRegisterTrader(data);
        }

    }

    private void retreiveConfirmationCode() {
        if(networkAvailable.isNetworkAvailable()){
            if(!FUtilsValidation.isEmpty(verifyCodeInputId,getString(R.string.field_required))){
                Map<String,Object> map=new HashMap<>();
            map.put("token",verifyCodeInputId.getText().toString());
            map.put("email",phoneNumber);

            ProgressDialog progressDialog=dailogUtil.showProgressDialog(VerificationCodeActivity.this,getString(R.string.logging),false);
                apiServiceInterface= ApiClient.getClientService();
                Call<SendCodeModel> call=apiServiceInterface.checkResetCode(map);
                call.enqueue(new Callback<SendCodeModel>() {
                    @Override
                    public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                        if (response.code() == 200) {
                            if (response.body().isSuccess()) {
                                Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                                intent.putExtra("phone", phoneNumber);
                                intent.putExtra("token", verifyCodeInputId.getText().toString());
                                startActivity(intent);
                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }else if(response.code()==404){
                            Toast.makeText(VerificationCodeActivity.this, "this code is invalid", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<SendCodeModel> call, Throwable t) {
                        Toast.makeText(VerificationCodeActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }


    private void sendCodeToMobile(String phoneNumber) {
        if(networkAvailable.isNetworkAvailable()){
                ProgressDialog progressDialog= dailogUtil.showProgressDialog(VerificationCodeActivity.this,getString(R.string.logging),false);
                Call<SendCodeModel> codeModelCall=apiServiceInterface.sendMobileCode(phoneNumber);
                codeModelCall.enqueue(new Callback<SendCodeModel>() {
                    @Override
                    public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                        Log.v(TAG,"sucess");
                        if(response.code()==200){
                            if(response.body().isSuccess()){
                                Log.v(TAG,"sucess1");
                                Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),VerificationCodeActivity.class);
                                intent.putExtra("phone",phoneNumber);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }else {
                                Log.v(TAG,"sucess2");


                                progressDialog.dismiss();
                            }
                        }else if(response.code()==404){

                        }
                    }


                    @Override
                    public void onFailure(Call<SendCodeModel> call, Throwable t) {
                        Log.v(TAG,"fail fail");
                    }
                });
            }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }


    private void getVerificationFromEmailRegisterTrader(Data data) {
        if(networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(verifyCodeInputId, getString(R.string.field_required))) {
                Map<String, Object> map = new HashMap<>();
                map.put("token", verifyCodeInputId.getText().toString());
                map.put("verify_type","email");

                ProgressDialog progressDialog = dailogUtil.showProgressDialog(VerificationCodeActivity.this, getString(R.string.logging), false);
                apiServiceInterface = ApiClient.getClientService();
                Call<SendCodeModel> call = apiServiceInterface.verifyCodeFromEmail("application/json","Bearer "+data.getToken(),map);

                call.enqueue(new Callback<SendCodeModel>() {
                    @Override
                    public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                        if(response.body().isSuccess()){
                            Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("register_store_model",data);
                            prefs=getSharedPreferences("check2",MODE_PRIVATE);
                            editor = prefs.edit();
                            editor.putInt("trader",123);
                            editor.commit();
                            editor.apply();

                            progressDialog.dismiss();
                            startActivity(intent);
                        }else {
                            Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<SendCodeModel> call, Throwable t) {

                    }
                });


            }
        }
        else{
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }


    private void getVerificationFromEmailRegisterUser(Data data) {
        if(networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(verifyCodeInputId, getString(R.string.field_required))) {
                Map<String, Object> map = new HashMap<>();
                map.put("token", verifyCodeInputId.getText().toString());
                map.put("verify_type","email");

                ProgressDialog progressDialog = dailogUtil.showProgressDialog(VerificationCodeActivity.this, getString(R.string.logging), false);
                apiServiceInterface = ApiClient.getClientService();
                Call<SendCodeModel> call = apiServiceInterface.verifyCodeFromEmail("application/json","Bearer "+data.getToken(),map);

                call.enqueue(new Callback<SendCodeModel>() {
                    @Override
                    public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                        if(response.body().isSuccess()){
                            Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtra("register_user_model",data);
                            prefs=getSharedPreferences("check1",MODE_PRIVATE);
                            editor = prefs.edit();
                            editor.putInt("user",123);
                            editor.commit();
                            editor.apply();
                            progressDialog.dismiss();
                            startActivity(intent);


                        }else {
                            Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<SendCodeModel> call, Throwable t) {

                    }
                });


            }
        }
        else{
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }





    @OnClick(R.id.resend_code_txt)
    void resendCodeClick(){
        sendCodeToMobile(phoneNumber);
    }

    @OnClick(R.id.toolbar_back_left_btn_id)
    void  backClick(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
