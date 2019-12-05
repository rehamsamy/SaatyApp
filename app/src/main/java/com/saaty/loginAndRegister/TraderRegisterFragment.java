package com.saaty.loginAndRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.util.DialogUtils;
import com.fourhcode.forhutils.FUtilsValidation;
import com.fourhcode.forhutils.Futils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.models.RegisterModel;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.NetworkAvailable;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class TraderRegisterFragment extends Fragment {

    ApiServiceInterface serviceInterface;
    NetworkAvailable networkAvailable;
    Context mContext;
    TextInputEditText nameInput, phoneInput, emailInput, shopType, password, confirmPssword;
    MaterialButton registerBtn;
     DailogUtil dailogUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.register_trader_fragment_layout, container, false);

        nameInput = view.findViewById(R.id.user_name_input_id);
        phoneInput = view.findViewById(R.id.phone_input_id);
        emailInput = view.findViewById(R.id.email_input_id);
        shopType = view.findViewById(R.id.store_type_input_id);
        password = view.findViewById(R.id.password_input_id);
        confirmPssword = view.findViewById(R.id.confirm_password_input_id);

        registerBtn = view.findViewById(R.id.confirm_btn_id);
        networkAvailable=new NetworkAvailable(getActivity());
        dailogUtil=new DailogUtil();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "clicked k");
                registerTrader();
            }
        });

        return view;

    }

    private void registerTrader() {

        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(nameInput, getString(R.string.field_required))
                    && !FUtilsValidation.isEmpty(emailInput, getString(R.string.field_required))
                    && !FUtilsValidation.isEmpty(phoneInput, getString(R.string.field_required))
                    && !FUtilsValidation.isEmpty(password, getString(R.string.field_required))
                    && !FUtilsValidation.isEmpty(confirmPssword, getString(R.string.field_required))
                    && !FUtilsValidation.isEmpty(shopType, getString(R.string.field_required))
                    && FUtilsValidation.isPasswordEqual(password, confirmPssword, getString(R.string.password_equal_error))
                    && FUtilsValidation.isValidEmail(emailInput, getString(R.string.error_email_msg))) {

                if (password.getText().length() < 6) {
                    password.setError(getString(R.string.weak_password));
                    password.requestFocus();
                    return;
                }


                Map<String, Object> map = new HashMap<>();
                map.put("fullname", nameInput.getText().toString());
                map.put("email", emailInput.getText().toString());
                map.put("mobile", phoneInput.getText().toString());
                map.put("type", "store");
                map.put("password", password.getText().toString());
                map.put("c_password", confirmPssword.getText().toString());
                map.put("store_name", shopType.getText().toString());


                ProgressDialog progressDialog=DailogUtil.showProgressDialog(getContext(),getString(R.string.logging),false);
                serviceInterface = ApiClient.getClientService();
                Call<RegisterModel> registerModelCall = serviceInterface.registerUser(map);
                registerModelCall.enqueue(new Callback<RegisterModel>() {
                    @Override
                    public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {

                       // Log.v("TraderRegisterFragment", "dddddddddddd" + response.body().toString());
                    if (response.body().getSuccess()==true) {
                        Log.v(TAG,"valid error1 ");
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra("register_store_model",response.body().getUserDataRegisterObject());
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                    else if(response.body().getSuccess()==false){
                        Log.v(TAG,"valid error2 ");
                        Toast.makeText(getContext(), response.body().getMessage()+response.body().getData().getEmail() + "\n" +
                                response.body().getData().getEmail() + "\n" , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else if(response.code()==404) {
                        Toast.makeText(getContext(), response.body().getMessage() + response.body().getData().getEmail() + "\n" +
                                response.body().getData().getEmail() + "\n", Toast.LENGTH_SHORT).show();
                    }
                    }

                    @Override
                    public void onFailure(Call<RegisterModel> call, Throwable t) {
                        Log.v(TAG, "fail fail");
                        Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }else {
            Toast.makeText(getContext(),getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
