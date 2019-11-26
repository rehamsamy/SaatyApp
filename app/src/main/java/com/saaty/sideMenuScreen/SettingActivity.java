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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saaty.R;
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
        }else  if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
            arrowArabic.setVisibility(View.GONE);
            arrowEnglish.setVisibility(View.VISIBLE);
        }

        toolbarTxt.setText(getString(R.string.setting));
    }

    @OnClick(R.id.language_layout_id)
    void changeLanguage(){
     showLanguageDialog();
    }

    private void showLanguageDialog() {
        String [] items={"Arabic","English"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_language));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    lang_selected = "ar";
                    PreferenceHelper.setValue(getApplicationContext(),lang_selected);
                    setConfig(getApplicationContext(),lang_selected);
                    languageSelected.setText("Arabic");
                } else if (which == 1) {
                    lang_selected = "en";
                    PreferenceHelper.setValue(getApplicationContext(),lang_selected);
                    setConfig(getApplicationContext(),lang_selected);
                    languageSelected.setText("English");
                }
                Intent intent=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.create();
        builder.show();


    }

    private  void setConfig(Context context,String lang){
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
