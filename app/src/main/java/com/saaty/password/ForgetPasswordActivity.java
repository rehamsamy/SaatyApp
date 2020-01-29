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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.saaty.R;
import com.saaty.models.SendCodeModel;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.NetworkAvailable;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final String TAG = ForgetPasswordActivity.class.getSimpleName();
    @BindView(R.id.phone_number_input_id)
    TextInputEditText emailInput;
    @BindView(R.id.confirm_btn_id) MaterialButton confirmBtn;
    NetworkAvailable available;
    ApiServiceInterface apiServiceInterface;
    DailogUtil dailogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        available=new NetworkAvailable(this);
        dailogUtil=new DailogUtil();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Log.v(TAG,"edit ttt"+emailInput.getText().toString());
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendCodeToMobile();
            }
        });
    }

//    @OnClick(R.id.confirm_btn_id)
//    void confirmClick(){
//
//
//
//          }
//


    private void sendCodeToMobile() {
        if(available.isNetworkAvailable()){
            if(!FUtilsValidation.isEmpty(emailInput,getString(R.string.field_required))
            && FUtilsValidation.isValidEmail(emailInput,getString(R.string.error_email_msg))){
             apiServiceInterface= ApiClient.getClientService();
          String name=   emailInput.getText().toString().trim();

               // Log.v(TAG,"edit ttt"+phoneNumberInput.toString());
                Log.v(TAG,"name is"+name);
                ProgressDialog progressDialog= dailogUtil.showProgressDialog(ForgetPasswordActivity.this,getString(R.string.logging),false);
             Call<SendCodeModel> codeModelCall=apiServiceInterface.sendMobileCode(emailInput.getText().toString());
             codeModelCall.enqueue(new Callback<SendCodeModel>() {
                 @Override
                 public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                     Log.v(TAG,"sucess");
                     if(response.code()==200){
                     if(response.body().isSuccess()){
                         Log.v(TAG,"sucess1");
                         Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                         Intent intent=new Intent(getApplicationContext(),VerificationCodeActivity.class);
                         intent.putExtra("phone",emailInput.getText().toString());
                         startActivity(intent);
                         progressDialog.dismiss();
                     }else {
                         Log.v(TAG,"sucess2");
                         Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                         progressDialog.dismiss();
                     }
                 }else if(response.code()==404){
                         Toast.makeText(ForgetPasswordActivity.this, getString(R.string.error_email_msg), Toast.LENGTH_LONG).show();
                         progressDialog.dismiss();

                     }
                 }


                 @Override
                 public void onFailure(Call<SendCodeModel> call, Throwable t) {
                     Log.v(TAG,"fail fail");
                 }
             });
            }
        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

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
