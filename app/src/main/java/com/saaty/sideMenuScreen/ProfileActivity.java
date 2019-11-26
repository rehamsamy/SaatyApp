package com.saaty.sideMenuScreen;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.saaty.R;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.edit_profile_back) ImageView editProfile;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        //toolbarTxt.setText(getString(R.string.cha));
    }

    @OnClick(R.id.edit_profile_back)
    void editProfile(){
        Intent intent=new Intent(getApplicationContext(),EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.change_password_back)
    void changePassword(){
        Intent intent=new Intent(getApplicationContext(),ChangePasswordActivity.class);
        startActivity(intent);
    }




}
