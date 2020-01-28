package com.saaty.sideMenuScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.util.PreferenceHelper;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    String lang_selected;
    @BindView(R.id.language_value_id) TextView languageSelected;
    @BindView(R.id.english_arrow_id) ImageView arrowEnglish;
    @BindView(R.id.arabic_arrow_id) ImageView arrowArabic;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")){
            arrowArabic.setVisibility(View.VISIBLE);
            arrowEnglish.setVisibility(View.GONE);
            languageSelected.setText("العربية");
        }else  if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
            arrowArabic.setVisibility(View.GONE);
            arrowEnglish.setVisibility(View.VISIBLE);
            languageSelected.setText("English");
        }

        toolbarTxt.setText(getString(R.string.setting));
    }

    @OnClick(R.id.language_layout_id)
    void changeLanguage(){
     showLanguageDialog();
    }

    private void showLanguageDialog() {
        String [] items={getString(R.string.english),getString(R.string.arabic)};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_language));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // English Item is Slected
                    lang_selected = "en";
                    PreferenceHelper.setValue(SettingActivity.this, lang_selected);
                    setConfig( SettingActivity.this,lang_selected);
                    Log.v("TAG","lang_selceted"+lang_selected);
                    languageSelected.setText("English");

                } else if (which == 1) {
                    // Arabic Item is Selected vvffrer
                    lang_selected = "ar";
                    PreferenceHelper.setValue(SettingActivity.this, lang_selected);
                    setConfig( SettingActivity.this,lang_selected);
                    Log.v("TAG","lang_selceted"+lang_selected);
                    languageSelected.setText("العربية");
                }

                Intent i=new Intent(getApplicationContext(), HomeActivity.class);
                //Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
      //  builder.create();
        builder.show();


    }

    private  void setConfig(Context context,String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    @OnClick(R.id.toolbar_back_left_btn_id)
    void backClick(){
        finish();
    }

    @OnClick(R.id.toolbar_home_id)
    void homeClick(){
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
