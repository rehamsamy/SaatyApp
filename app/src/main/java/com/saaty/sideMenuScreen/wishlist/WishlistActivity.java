package com.saaty.sideMenuScreen.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.saaty.PaginationScrollListener;
import com.saaty.R;
import com.saaty.database.Database;
import com.saaty.home.HomeActivity;
import com.saaty.home.StoresActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.CheckWishlistModel;
import com.saaty.models.CityModel;
import com.saaty.models.DataArrayModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.models.RoomModel;
import com.saaty.models.SendCodeModel;
import com.saaty.models.StoreListModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickInterface;
import com.saaty.util.OnItemClickRecyclerViewInterface;
import com.saaty.util.PreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity  {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_id) ProgressBar progressBar;
    @BindView(R.id.empty_data_txt_id) TextView emptyData;
    @BindView(R.id.toolbar_txt_id) TextView toolbarTxt;
    @BindView(R.id.search_ed_id)EditText searchEditTxt;
   private String spinnerValue;
   int spinnerIndex;
   SpinnerAdapter spinnerAdapter;
   List<String>cityNames=new ArrayList<>();
   List<DataArrayModel> newList=new ArrayList<>();
    Database database;

    String sort_type;
    Dialog mDialog;
    List<DataArrayModel> wishlistProducts=new ArrayList<>();
    List<DataArrayModel> newWishlist;
    static List<DataArrayModel> newSortedList;
    WishlistAdapter wishlistAdapter;
    ApiServiceInterface apiServiceInterface;
    int current_page=1;
    int selected_product_id;
    NetworkAvailable networkAvailable;
    DataArrayModel model;

    private GridLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mDialog=new Dialog(this);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        networkAvailable=new NetworkAvailable(getApplicationContext());
        toolbarTxt.setText(getString(R.string.wishlist));
        wishlistProducts=new ArrayList<>();
        //initializeComponents();
        database=Database.getInstance(this);

        wishlistProducts.clear();
        emptyData.setVisibility(View.GONE);
        buildWishlisrRecycler();
        getWishList(current_page);

        searchEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
               // Log.v("TAG", "sssssssssss" + adsProducts.size() + s.toString().toLowerCase());
                ArrayList<DataArrayModel> newlist = new ArrayList<>();

                for (DataArrayModel item : wishlistProducts) {
                    Log.v("TAG", "ar name" + item.getArName().contains(newText));
                    if (item.getArName().contains(newText)) {
                        newlist.add(item);
                    }
                }
                wishlistProducts.clear();
                if(newlist.size()>0) {
                    for (int i = 0; i < newlist.size(); i++) {
                        wishlistProducts.add(newlist.get(i));
                        wishlistAdapter.notifyDataSetChanged();

                    }
                }else {
                    wishlistProducts.clear();
                    emptyData.setVisibility(View.VISIBLE);
                    wishlistAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });




    }



    private void getWishList(int current_page) {
        if(networkAvailable.isNetworkAvailable()){
            apiServiceInterface= ApiClient.getClientService();
         //  progressBar.setVisibility(View.VISIBLE);
            Map<String,Object> map=new HashMap<>();
          String token= HomeActivity.access_token;
          int limit=10;
            map.put("page",current_page);
            map.put("limit",limit);
            Call<StoreListModel> call=apiServiceInterface.getWishlist(map,"application/json",token);
                    call.enqueue(new Callback<StoreListModel>() {
                @Override
                public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                    if(response.code()==200){
                        Log.v("TAG","ttttt"+response.body().getDataObjectModel().getDataArrayModelList().size()
                        +"   page"+current_page);
                        if(response.body().isSuccess()) {
                            //shlistProducts=wiresponse.body().getDataObjectModel().getDataArrayModelList();
                                 if(response.body().getDataObjectModel().getDataArrayModelList().size()>0) {
                                    wishlistProducts.addAll(response.body().getDataObjectModel().getDataArrayModelList());
                                    wishlistAdapter.notifyDataSetChanged();
                                     progressBar.setVisibility(View.GONE);
                                     Log.v("TAG","optopn 1   "+ wishlistProducts.size());
                                 }else if(response.body().getDataObjectModel().getDataArrayModelList().size()==0 && current_page==1) {
                                     //recyclerView.setVisibility(View.GONE);
                                     emptyData.setVisibility(View.VISIBLE);
                                     progressBar.setVisibility(View.GONE);
                                     Log.v("TAG","option 2");
                                     // }
                                 }
                        }

                    }
                       else {
                       Toast.makeText(WishlistActivity.this,"error", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                       }
                }

                @Override
                public void onFailure(Call<StoreListModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void buildOnClickInterface() {
        wishlistAdapter.setOnItemClickListenerRecyclerView(new OnItemClickRecyclerViewInterface() {
            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
               deleteItemFromWishList(wishlistProducts.get(position).getProductId());
            }

            @Override
            public void OnItemClick(int position) {
                Intent intent=new Intent(WishlistActivity.this, ProductDetailsActivity.class);
                DataArrayModel item= wishlistProducts.get(position);
                intent.putExtra(ProductDetailsActivity.WISHLIST_PRODUCTS_DETAILS,item);
                startActivity(intent);
            }
        });
    }

    private void deleteItemFromWishList(int selected_product_id) {
        apiServiceInterface=ApiClient.getClientService();
        Call<SendCodeModel> call=apiServiceInterface.deleteItemFromWishlist(selected_product_id
                ,"application/json"
                ,HomeActivity.access_token);
        Log.v("TAG","product selected  "+selected_product_id);
        call.enqueue(new Callback<SendCodeModel>() {
            @Override
            public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                if (response.code() == 404) {
                    Toast.makeText(WishlistActivity.this, "error ocuured", Toast.LENGTH_LONG).show();

                } else {
                    if (response.body().isSuccess()) {
                       // Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        wishlistProducts.clear();
                        buildWishlisrRecycler();
                        getWishList(1);
                        Log.v("TAG", "product selected deleted " + selected_product_id);
                    } else {
                        //Toast.makeText(WishlistActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Log.v("TAG", "product selected not deleted " + selected_product_id);
                    }
                }
            }
            @Override
            public void onFailure(Call<SendCodeModel> call, Throwable t) {
                Log.v("TAG","product selected failed "+selected_product_id);

            }
        });
    }

    private void buildWishlisrRecycler() {
        GridLayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
         recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        wishlistAdapter=new WishlistAdapter(WishlistActivity.this,wishlistProducts);
        Log.v("TAG","ffff"+wishlistProducts.size());
        recyclerView.setAdapter(wishlistAdapter);
           buildOnClickInterface();
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                current_page++;
                    getWishList(current_page);
            }
        });

    }


    protected void initializeComponents() {


        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        wishlistAdapter= new WishlistAdapter(getApplicationContext(),wishlistProducts);
        recyclerView.setAdapter(wishlistAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                current_page++;
                getWishList(current_page);
            }
        });


    }



    @OnClick(R.id.nav_filter_id)
    void navFilterDailog() {
        final boolean[] isChecking = {true};
        final int[] mCheckedId = {R.id.from_high_radio_btn_id};
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.filter_sort_dialog_wishlist_layout);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
        CheckBox newProduct,oldProduct,watchChecked,bracletChecked;
        MaterialButton done = mDialog.findViewById(R.id.login_visitor_btn_id);
        RadioGroup group = mDialog.findViewById(R.id.radio_group);

        newProduct = mDialog.findViewById(R.id.new_product_check_box);
        oldProduct = mDialog.findViewById(R.id.old_product_product_check_box);
        watchChecked=mDialog.findViewById(R.id.watch_check_box);
        bracletChecked=mDialog.findViewById(R.id.braclet_product_check_box);
        Spinner spinner=mDialog.findViewById(R.id.spinner);


        setSpinnerData(spinner);
        //spinner.setSelected(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 spinnerValue = adapterView.getItemAtPosition(i).toString();

                 spinnerIndex=i+1;
                Log.v("TAG","spinnerrrr"+spinnerValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });




        // getWishList();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          List<DataArrayModel> newList=new ArrayList<>();

                int checked_id=group.getCheckedRadioButtonId();
                boolean isCkecked;
                if(checked_id==R.id.from_high_radio_btn_id&&!oldProduct.isChecked()&&!newProduct.isChecked()&&!bracletChecked.isChecked()&&!watchChecked.isChecked()){
                   Log.v("TAG","highhhh");
                    newList=getHighPrice(wishlistProducts);
                }else if(checked_id==R.id.from_low_radio_btn_id&&!oldProduct.isChecked()&&!newProduct.isChecked()&&!bracletChecked.isChecked()&&!watchChecked.isChecked()) {
                    Log.v("TAG","lowww");
                  newList=getLowPice(wishlistProducts);
                }
                else if(newProduct.isChecked()&&oldProduct.isChecked()==false&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                    Log.v("TAG","sussss");
                     newList=getLowPriceNewProducts(wishlistProducts);
                }else if(oldProduct.isChecked()&&newProduct.isChecked()==false&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                    Log.v("TAG","susss2");
                    newList=getHighUsedProducts(wishlistProducts);
                }
                else if(checked_id == R.id.from_high_radio_btn_id && oldProduct.isChecked()&&newProduct.isChecked()==false&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                    List<DataArrayModel> highProducts=getHighPrice(wishlistProducts);
                    newList=getHighUsedProducts(highProducts);
                }else if(checked_id==R.id.from_low_radio_btn_id&&newProduct.isChecked()&&oldProduct.isChecked()==false&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                    List<DataArrayModel> lowProducts=getLowPice(wishlistProducts);
                    newList=getLowPriceNewProducts(lowProducts);
                    Log.v("TAG","exper 1");
                }else if(checked_id==R.id.from_low_radio_btn_id&&oldProduct.isChecked()&&newProduct.isChecked()==false&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                    List<DataArrayModel> lowProducts=getLowPice(wishlistProducts);
                   newList=getLowPriceUsedProducts(lowProducts);
                    Log.v("TAG","exper 2");
                }else if(checked_id == R.id.from_high_radio_btn_id&&newProduct.isChecked()==true&&oldProduct.isChecked()==true&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                    Log.v("TAG","exper 3");
                    newList=getHighPrice(wishlistProducts);

                }else if(checked_id==R.id.from_low_radio_btn_id&&newProduct.isChecked()==true&&oldProduct.isChecked()==true&&watchChecked.isChecked()==false&&bracletChecked.isChecked()==false){
                   newList=getLowPice(wishlistProducts);
                }else if(checked_id==R.id.from_high_radio_btn_id&&watchChecked.isChecked()==true&&oldProduct.isChecked()==false&&newProduct.isChecked()==false&bracletChecked.isChecked()==false){
                    List<DataArrayModel> x1=getHighPrice(wishlistProducts);
                   newList=getWatchesCategory(x1);
                    Log.v("TAG","exper 5");
                }else if(checked_id==R.id.from_high_radio_btn_id&&bracletChecked.isChecked()==true&&oldProduct.isChecked()==false&&newProduct.isChecked()==false&watchChecked.isChecked()==false){
                    List<DataArrayModel> x1=getHighPrice(wishlistProducts);
                   newList=getBracletCategory(x1);
                    Log.v("TAG","exper 6");
                }else if(checked_id==R.id.from_low_radio_btn_id&&watchChecked.isChecked()==true&&oldProduct.isChecked()==false&&newProduct.isChecked()==false&bracletChecked.isChecked()==false){
                    List<DataArrayModel> x1=getLowPice(wishlistProducts);
                   newList=getWatchesCategory(x1);
                    Log.v("TAG","exper 6");
                }else if(checked_id==R.id.from_low_radio_btn_id&&bracletChecked.isChecked()==true&&oldProduct.isChecked()==false&&newProduct.isChecked()==false&watchChecked.isChecked()==false){
                    List<DataArrayModel> x1=getLowPice(wishlistProducts);
                    newList=getBracletCategory(x1);
                    Log.v("TAG","exper 7");
                }else if(checked_id==R.id.from_low_radio_btn_id&&bracletChecked.isChecked()==true&&oldProduct.isChecked()&&newProduct.isChecked()==false&watchChecked.isChecked()==false){
                    List<DataArrayModel> x1=getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes=getBracletCategory(x1);
                    newList=getLowPriceUsedProducts(bracletes);
                    Log.v("TAG","exper 7");
                }else if(checked_id==R.id.from_low_radio_btn_id&&bracletChecked.isChecked()==true&&newProduct.isChecked()&&newProduct.isChecked()==false&watchChecked.isChecked()==false) {
                    List<DataArrayModel> x1 = getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes = getBracletCategory(x1);
                    newList = getHighNewProducts(bracletes);
                    Log.v("TAG", "exper 7");
                }else if(checked_id==R.id.from_low_radio_btn_id&&watchChecked.isChecked()==true&&oldProduct.isChecked()&&newProduct.isChecked()==false&watchChecked.isChecked()==false){
                    List<DataArrayModel> x1=getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes=getWatchesCategory(x1);
                    newList=getLowPriceUsedProducts(bracletes);
                    Log.v("TAG","exper 7");
                }else if(checked_id==R.id.from_low_radio_btn_id&&watchChecked.isChecked()==true&&newProduct.isChecked()&&newProduct.isChecked()==false&watchChecked.isChecked()==false) {
                    List<DataArrayModel> x1 = getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes = getWatchesCategory(x1);
                    newList = getHighNewProducts(bracletes);
                    Log.v("TAG", "exper 7");
                } else if (checked_id == R.id.from_high_radio_btn_id && bracletChecked.isChecked() == true && oldProduct.isChecked() && newProduct.isChecked() == false & watchChecked.isChecked() == false) {
                    List<DataArrayModel> x1 = getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes = getBracletCategory(x1);
                    newList = getLowPriceUsedProducts(bracletes);
                    Log.v("TAG", "exper 7");
                } else if (checked_id == R.id.from_high_radio_btn_id && bracletChecked.isChecked() == true && newProduct.isChecked() && newProduct.isChecked() == false & watchChecked.isChecked() == false) {
                    List<DataArrayModel> x1 = getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes = getBracletCategory(x1);
                    newList = getHighNewProducts(bracletes);
                    Log.v("TAG", "exper 7");
                } else if (checked_id == R.id.from_high_radio_btn_id && watchChecked.isChecked() == true && oldProduct.isChecked() && newProduct.isChecked() == false & watchChecked.isChecked() == false) {
                    List<DataArrayModel> x1 = getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes = getWatchesCategory(x1);
                    newList = getLowPriceUsedProducts(bracletes);
                    Log.v("TAG", "exper 7");
                } else if (checked_id == R.id.from_high_radio_btn_id && watchChecked.isChecked() == true && newProduct.isChecked() && newProduct.isChecked() == false & watchChecked.isChecked() == false) {
                    List<DataArrayModel> x1 = getLowPice(wishlistProducts);
                    List<DataArrayModel> bracletes = getWatchesCategory(x1);
                    newList = getHighNewProducts(bracletes);
                    Log.v("TAG", "exper 7");
            } else if(spinner.getSelectedItem().toString()!=null){
                    Log.v("TAg","filterrrr"+spinnerIndex);
                    newList= filterCity(wishlistProducts,spinnerIndex);


                }

                else {
                    wishlistProducts.clear();
                    wishlistAdapter.notifyDataSetChanged();
                    emptyData.setVisibility(View.VISIBLE);
                    Log.v("TAG","exper else");
                }
                mDialog.dismiss();


                if(newList.size()>0) {
                   // wishlistProducts.clear();
                    wishlistProducts=newList;
                    wishlistAdapter.notifyDataSetChanged();
                }else {
                    wishlistProducts.clear();
                    emptyData.setVisibility(View.VISIBLE);
                    wishlistAdapter.notifyDataSetChanged();
                }

            }
        });


    }

    public static List<DataArrayModel> getHighPrice(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for (int i = 0; i < newWishlist.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < newWishlist.size(); j++) {
                if (newWishlist.get(j).getPrice() > newWishlist.get(pos).getPrice())
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
           DataArrayModel min = newWishlist.get(pos);
            newWishlist.set(pos, newWishlist.get(i));
            newWishlist.set(i, min);
        }
         return newWishlist;
    }

    public static List<DataArrayModel> getHighNewProducts(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for(int i=0;i<newWishlist.size();i++){
            if(newWishlist.get(i).getShape().equals("New")){
                newSortedList.add(newWishlist.get(i));
            }
        }
        return newSortedList;

    }


   public static List<DataArrayModel> getHighUsedProducts(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for(int i=0;i<newWishlist.size();i++){
            if(newWishlist.get(i).getShape().equals("Used")){
                newSortedList.add(newWishlist.get(i));
            }
        }
        return newSortedList;

    }


    public static List<DataArrayModel> getLowPice(List<DataArrayModel> newWishlist){
        for (int i = 0; i < newWishlist.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < newWishlist.size(); j++) {
                if (newWishlist.get(j).getPrice() < newWishlist.get(pos).getPrice())
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            DataArrayModel min = newWishlist.get(pos);
            newWishlist.set(pos, newWishlist.get(i));
            newWishlist.set(i, min);
        }
        return  newWishlist;
    }

    public static List<DataArrayModel> getLowPriceNewProducts(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for(int i=0;i<newWishlist.size();i++){
            if(newWishlist.get(i).getShape().equals("New")){
                newSortedList.add(newWishlist.get(i));
            }

        }
        return newSortedList;
    }


    public static List<DataArrayModel> getLowPriceUsedProducts(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for(int i=0;i<newWishlist.size();i++){
            if(newWishlist.get(i).getShape().equals("Used")){
                newSortedList.add(newWishlist.get(i));
            }

        }
        return newSortedList;
    }

    public static List<DataArrayModel> getWatchesCategory(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for(int i=0;i<newWishlist.size();i++){
            if(newWishlist.get(i).getCategoryId()==1){
                newSortedList.add(newWishlist.get(i));
            }

        }
        return newSortedList;
    }

    public static List<DataArrayModel> getBracletCategory(List<DataArrayModel>newWishlist){
        newSortedList=new ArrayList<>();
        for(int i=0;i<newWishlist.size();i++){
            if(newWishlist.get(i).getCategoryId()==2){
                newSortedList.add(newWishlist.get(i));
            }

        }
        return newSortedList;
    }




    @OnClick(R.id.toolbar_back_left_btn_id)
    void setBackClick(){
        finish();
    }
    @OnClick(R.id.toolbar_home_id)
    void setHomeBack(){
        finish();
    }



    private void setSpinnerData(Spinner spinner) {
        getCityList(spinner);
    }

    private  void getCityList(Spinner spinner){
        apiServiceInterface=ApiClient.getClientService();
        Call<CityModel> call=apiServiceInterface.getCityList();

        call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if(response.body().isSuccess()){
                    if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")){
                        for(int i=0;i<response.body().getCitydatamodel().size();i++){
                            cityNames.add(response.body().getCitydatamodel().get(i).getCityNameAr());
                        }
                        spinnerAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout,cityNames);
                        spinner.setAdapter(spinnerAdapter);
                    }else  if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
                        for(int i=0;i<response.body().getCitydatamodel().size();i++){
                            cityNames.add(response.body().getCitydatamodel().get(i).getCityNameEn());
                            spinnerAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_layout,cityNames);
                            spinner.setAdapter(spinnerAdapter);
                        }
                    }
                    Log.v("TAG","cityes"+response.body().getCitydatamodel().get(0).getCityNameAr());


                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {

            }
        });
    }


    private List<DataArrayModel> filterCity(List<DataArrayModel> wishlistProducts,int spinnerIndex) {
        Log.v("TAG","wwwww"+wishlistProducts.size());
        for(int i=0;i<wishlistProducts.size();i++){
            if(PreferenceHelper.getValue(getApplicationContext()).equals("ar")){
               if(wishlistProducts.get(i).getCityId()==spinnerIndex) {
                    newList.add(wishlistProducts.get(i));
                    Log.v("TAG","city size"+newList.size()+wishlistProducts.get(i).getCityArName());
               }
            }else if(PreferenceHelper.getValue(getApplicationContext()).equals("en")){
                if(wishlistProducts.get(i).getCityId()==spinnerIndex) {
                    newList.add(wishlistProducts.get(i));
                    Log.v("TAG", "city size" + newList.size());
                }
            }
        }
        return newList;


    }

}
