package com.saaty.sideMenuScreen;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.TextView;

import com.saaty.R;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        toolbarTxt.setText(getString(R.string.change_password));
    }
}