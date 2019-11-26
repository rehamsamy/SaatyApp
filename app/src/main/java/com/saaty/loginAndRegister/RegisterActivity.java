package com.saaty.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.saaty.R;

public class RegisterActivity extends AppCompatActivity {
@BindView(R.id.tab_layout_id) TabLayout tabLayout;
@BindView(R.id.view_pager_id) ViewPager viewPager;
RegisterPagerAdaper adaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        tabLayout.setupWithViewPager(viewPager);
        adaper=new RegisterPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(adaper);



    }
}
