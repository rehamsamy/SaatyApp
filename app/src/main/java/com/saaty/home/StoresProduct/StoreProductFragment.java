package com.saaty.home.StoresProduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saaty.R;
import com.saaty.home.StoresProduct.StoreProductAdapter;
import com.saaty.models.DataItem;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.NetworkAvailable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoreProductFragment extends Fragment {
    StoreProductAdapter adapter;
    List<ProductDataItem> storeProductsList;
    RecyclerView recyclerView;
    ApiServiceInterface apiServiceInterface;
    DailogUtil dailogUtil;
    NetworkAvailable networkAvailable;
    String shape_type;
    String type;
    int id;
    List<ProductDataItem> newProducts=new ArrayList<>();
    List<ProductDataItem> oldProducts=new ArrayList<>();

    public StoreProductFragment(String shape_type) {
        this.shape_type=shape_type;
    }

    //    public StoreProductFragment (List<ProductDataItem>newProducts,List<ProductDataItem>oldProducts){
//        this.oldProducts=oldProducts;
//        this.newProducts=newProducts;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.store_products_fragment_layout,container,false);
      networkAvailable=new NetworkAvailable(getActivity());
      dailogUtil=new DailogUtil();
       Intent intent= getActivity().getIntent();
       id=intent.getIntExtra("store_id",1);
        recyclerView=view.findViewById(R.id.recycler_view);
        getStoreProducts();
        return view;
    }

    private void getStoreProducts() {
        if(networkAvailable.isNetworkAvailable()){
            ProgressDialog progressDialog=dailogUtil.showProgressDialog(getActivity(),getString(R.string.logging),false);
            apiServiceInterface= ApiClient.getClientService();
            Call<ProductDataModel> call=apiServiceInterface.gerStoreProducts(1);
            call.enqueue(new Callback<ProductDataModel>() {
                @Override
                public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                    if(response.body().isSuccess()){
                        storeProductsList=response.body().getProductDataModels();
                        adapter=new StoreProductAdapter(getContext(),storeProductsList);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                        recyclerView.setAdapter(adapter);
                        for(int i=0;i<storeProductsList.size();i++){
                            if(storeProductsList.get(i).getShape().equals("New")&&shape_type.equals("New")){
                                 newProducts.add(storeProductsList.get(i));
                                adapter=new StoreProductAdapter(getContext(),newProducts);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                                recyclerView.setAdapter(adapter);
                                Log.v(TAG,"ssss is"+newProducts.size()+shape_type);
                               // shape_type="New";

                            }else if(storeProductsList.get(i).getShape().equals("Old")){
                                oldProducts.add(storeProductsList.get(i));
                                adapter=new StoreProductAdapter(getContext(),oldProducts);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                                recyclerView.setAdapter(adapter);
                                Log.v(TAG,"ddddd is"+oldProducts.size());
                                shape_type="OLd";

                            }
                        }

                        Toast.makeText(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ProductDataModel> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

    }
}
