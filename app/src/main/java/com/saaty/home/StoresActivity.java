package com.saaty.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.saaty.R;
import com.saaty.home.StoresProduct.StoresProductsActivity;
import com.saaty.models.CategoryModel;
import com.saaty.models.DataItem;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity implements OnItemClickInterface {

    private static final String TAG = StoresActivity.class.getSimpleName();
    @BindView(R.id.homeSearch_ed_id)
    SearchView searchView;
    @BindView(R.id.drawer_layout_id)
    DrawerLayout drawerLayout;
    @BindView(R.id.nv_id)
    NavigationView navigationView;
    @BindView(R.id.tab_layout_id)
    TabLayout tabLayout;
    @BindView(R.id.toolbar_id)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    StoreAdapter adapter;
    NetworkAvailable networkAvailable;
    DailogUtil dailogUtil;
    ApiServiceInterface apiServiceInterface;
    List<DataItem> categoryItem = new ArrayList();
    List<DataItem> storesList=new ArrayList<>();
    @BindView(R.id.toolbar_txt_id)TextView toolbarTxt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        dailogUtil = new DailogUtil();
        networkAvailable = new NetworkAvailable(this);
        toolbarTxt.setText(getString(R.string.stores_toolbar));

//        ImageView searchIcon = searchView.findViewById(R.id.search_button);
//        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.search));
//       String x[]={"name","two","store"};
//        for(int i=0;i<=x.length-1;i++){
//
//            //tabLayout.addTab(tabLayout.newTab().setText(x[i]));

//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.setAdapter(adapter);

        getCateogry();

   tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.store)),0);
        getStoreList();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void getStoreList() {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface=ApiClient.getClientService();
           Call<CategoryModel> call= apiServiceInterface.getStoresList();
           call.enqueue(new Callback<CategoryModel>() {
               @Override
               public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                   if(response.body().isSuccess()){
                       storesList=response.body().getData();
                       adapter = new StoreAdapter(getApplicationContext(),storesList,StoresActivity.this);
                       recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                       recyclerView.setAdapter(adapter);
                       Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                   }
               }

               @Override
               public void onFailure(Call<CategoryModel> call, Throwable t) {
                   Toast.makeText(StoresActivity.this,t.getMessage().toString(), Toast.LENGTH_SHORT).show();

               }
           });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCateogry() {
        if(networkAvailable.isNetworkAvailable()){
            ProgressDialog progressDialog=dailogUtil.showProgressDialog(StoresActivity.this,getString(R.string.logging),false);
            apiServiceInterface= ApiClient.getClientService();
            Call<CategoryModel> call=apiServiceInterface.getCatogory();
            call.enqueue(new Callback<CategoryModel>() {
                @Override
                public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                    if(response.body().isSuccess()){
                        Log.v(TAG,"category tabs");
                        Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        categoryItem=response.body().getData();
                        for(int i=0;i<categoryItem.size();i++){
                           tabLayout.addTab(tabLayout.newTab().setText("       " +response.body().getData().get(i).getCategoryArName().toString()+"      "));

                        }
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(StoresActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Log.v(TAG,"category tabs error");
                    progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<CategoryModel> call, Throwable t) {
                    Toast.makeText(StoresActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    Log.v(TAG,"eeeeeeeeeee"+t.getMessage().toString());
               progressDialog.dismiss();
                }
            });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), StoresProductsActivity.class);
        int id = storesList.get(position).getStoreId();
        String name = storesList.get(position).getStoreArName();
        intent.putExtra("store_id", id);
        intent.putExtra("store_name", name);
        startActivity(intent);
    }
}
