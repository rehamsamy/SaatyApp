package com.saaty.home.StoresProduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saaty.R;
import com.saaty.home.StoresProduct.StoreProductAdapter;
import com.saaty.models.DataArrayModel;
import com.saaty.models.DataItem;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.models.StoreListModel;
import com.saaty.productDetails.ProductDetailsActivity;
import com.saaty.sideMenuScreen.wishlist.DealingWithWishList;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.DailogUtil;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;
import com.saaty.util.OnItemClickRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.ViewGroup.getChildMeasureSpec;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoreUsedProductFragment extends Fragment {
    StoreProductAdapter adapter;
    List<DataArrayModel> storeProductsList=new ArrayList<>();
    RecyclerView recyclerView;
    ApiServiceInterface apiServiceInterface;
    DailogUtil dailogUtil;
    NetworkAvailable networkAvailable;
    String shape_type;
    String type;
    int store_id;
    String store_name;
    List<DataArrayModel> newProducts=new ArrayList<>();
    List<DataArrayModel> oldProducts=new ArrayList<>();
    DealingWithWishList dealingWithWishList;
    TextView emptyData;
    ProgressBar progressBar;
    int current_page=1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.store_products_fragment_layout,container,false);
      networkAvailable=new NetworkAvailable(getActivity());
      dealingWithWishList=new DealingWithWishList(getContext());
      progressBar=view.findViewById(R.id.progress_id);
      dailogUtil=new DailogUtil();
      emptyData=view.findViewById(R.id.empty_data_txt_id);
       Intent intent= getActivity().getIntent();
        if (intent.getAction().equals("store_products_details")) {
          store_id=intent.getIntExtra("store_id",1);
        }

        recyclerView=view.findViewById(R.id.recycler_view);
        if(networkAvailable.isNetworkAvailable()){
           // newProducts.clear();
            //storeProductsList.clear();
            buildRecyclerViewForCategory();
            getStoreProducts(current_page,shape_type);
        }else {
            Toast.makeText(getContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void getStoreProducts(int current_page,String shape_type) {
        if(networkAvailable.isNetworkAvailable()){
           // ProgressDialog progressDialog=dailogUtil.showProgressDialog(getActivity(),getString(R.string.logging),false);
            progressBar.setVisibility(View.VISIBLE);
            apiServiceInterface= ApiClient.getClientService();
            Map<String,Object> map=new HashMap<>();
            map.put("page",current_page);
            map.put("limit",10);
            Call<StoreListModel> call=apiServiceInterface.gerStoreProducts(store_id,map);
            call.enqueue(new Callback<StoreListModel>() {
                @Override
                public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {
                    if(response.body().isSuccess()){
                        storeProductsList.addAll(response.body().getDataObjectModel().getDataArrayModelList());

                        for(int i=0;i<storeProductsList.size();i++) {
                            Log.v(TAG,"not matchhhhh \n"+storeProductsList.get(i).getShape());

                            if(storeProductsList.get(i).getShape().equals("Used")){
                                newProducts.add(storeProductsList.get(i));

                                if(storeProductsList.size()<=0){
                                    emptyData.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }
                                Log.v(TAG,"new list"+newProducts.size());

                            }

                        }

                        adapter.notifyDataSetChanged();

                        Log.v(TAG,"dddddddddd"+storeProductsList.size());
                        Toast.makeText(getContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                       progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<StoreListModel> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            Toast.makeText(getContext(), getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }

    }



    void buildRecyclerViewForCategory() {
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter =new StoreProductAdapter(getContext(),newProducts);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                current_page++;
               getStoreProducts(current_page,shape_type);
            }
        });
    }



//    private void buildOnClickListener() {
//        adapter.setOnItemClickListenerRecyclerView(new OnItemClickRecyclerViewInterface() {
//            @Override
//            public void OnWishListClick(int position, ImageView wishlist_image) {
//         dealingWithWishList.addToWishList(storeProductsList.get(position),progressBar,wishlist_image);
//            }
//
//            @Override
//            public void OnItemClick(int position) {
//              ProductDataItem item=storeProductsList.get(position);
//                Intent intent=new Intent(getContext(), ProductDetailsActivity.class);
//                //intent.putExtra("store_product_details",item);
//                startActivity(intent);
//            }
//        });
//
//    }
}
