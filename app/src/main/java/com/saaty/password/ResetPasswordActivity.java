package com.saaty.password;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.saaty.R;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.SendCodeModel;
import com.saaty.sideMenuScreen.TermsActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.NetworkAvailable;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.new_password_input)
    TextInputEditText newPasswordInput;
    @BindView(R.id.confirm_new_password_input)
    TextInputEditText confirmPasswordInput;
    @BindView(R.id.accept_terms_id)
    CheckBox acceptTermsCheckBox;
    @BindView(R.id.toolbar_txt_id)
    TextView toolbarTxt;
    DailogUtil dailogUtil;
    NetworkAvailable networkAvailable;
    ApiServiceInterface apiServiceInterface;
    String token, phoneNumber;
    @BindView(R.id.accept_terms_txt)TextView acceptTermsTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        dailogUtil = new DailogUtil();
        networkAvailable = new NetworkAvailable(this);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        phoneNumber = intent.getStringExtra("phone");
        toolbarTxt.setText(getString(R.string.reset_password));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        acceptTermsTxt.setClickable(true);
        // acceptTermsTxt.setText("");
        acceptTermsTxt.setPaintFlags(acceptTermsTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(acceptTermsTxt, Linkify.ALL);
        acceptTermsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LoginActivity", "Sign Up Activity activated.");
                // this is where you should start the new Activity
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);

            }
        });


    }

    @OnClick(R.id.confirm_btn_id)
    void confirmBtn() {
        if(acceptTermsCheckBox.isChecked()){
            resetPassword();
        }else {
            Toast.makeText(this, getString(R.string.check_box), Toast.LENGTH_SHORT).show();
        }

    }

    private void resetPassword() {

        if (networkAvailable.isNetworkAvailable()) {

            if (!FUtilsValidation.isEmpty(newPasswordInput, getString(R.string.field_required)) &&
                    !FUtilsValidation.isEmpty(confirmPasswordInput, getString(R.string.field_required))
                    && FUtilsValidation.isPasswordEqual(confirmPasswordInput, newPasswordInput, getString(R.string.password_equal_error))) {

                Drawable drawable=getResources().getDrawable(R.drawable.signup_confirm_password);
                confirmPasswordInput.setCompoundDrawables(drawable,null,null,null);

                ProgressDialog progressDialog = dailogUtil.showProgressDialog(ResetPasswordActivity.this, getString(R.string.logging), false);
                apiServiceInterface = ApiClient.getClientService();
                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                map.put("email", phoneNumber);
                map.put("password", newPasswordInput.getText().toString());
                map.put("password_confirmation", confirmPasswordInput.getText().toString());
                Call<SendCodeModel> call = apiServiceInterface.resetPassword(map);
                call.enqueue(new Callback<SendCodeModel>() {
                    @Override
                    public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                        if (response.body().isSuccess()) {
                            Toast.makeText(ResetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), LoginTraderUserActivity.class));
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendCodeModel> call, Throwable t) {

                        Toast.makeText(ResetPasswordActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.toolbar_back_left_btn_id)
    void backClick(){
        finish();
    }
}
