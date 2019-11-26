package com.saaty.splash;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.saaty.R;
import com.saaty.loginAndRegister.LoginTraderUserActivity;

public class SplashLanguageActivity extends AppCompatActivity {

    @BindView(R.id.arabic_btn_id) MaterialButton arabicBtn;
    @BindView(R.id.english_btn_id) MaterialButton englishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_language);
        ButterKnife.bind(this);


    }
    @OnClick(R.id.arabic_btn_id)
    void setArabicBtn(){
        startActivity(new Intent(getApplicationContext(), LoginTraderUserActivity.class));
    }

    @OnClick(R.id.english_btn_id)
    void setEnglishBtn(){
        startActivity(new Intent(getApplicationContext(), LoginTraderUserActivity.class));
    }
}
