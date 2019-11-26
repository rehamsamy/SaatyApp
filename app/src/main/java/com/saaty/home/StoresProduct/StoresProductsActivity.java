package com.saaty.home.StoresProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.saaty.R;

public class StoresProductsActivity extends AppCompatActivity {

    private static final String TAG = StoresProductsActivity.class.getSimpleName();
    @BindView(R.id.tab_layout_id) TabLayout tabLayout;
    @BindView(R.id.view_pager_id) ViewPager viewPager;
    StoreProductsPagerAdaper pagerAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_products);
        ButterKnife.bind(this);
        tabLayout.setupWithViewPager(viewPager);
        pagerAdaper=new StoreProductsPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdaper);


        Log.v(TAG,"vvvvvvvvvv"+viewPager.getChildCount());

    }

}
