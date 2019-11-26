package com.saaty.home.StoresProduct;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saaty.R;
import com.saaty.models.DataItem;
import com.saaty.models.ProductDataItem;
import com.saaty.util.PreferenceHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoreProductAdapter extends RecyclerView.Adapter<StoreProductAdapter.Holder> {

    private static final String TAG =StoreProductAdapter.class.getSimpleName() ;
    Context mContext;
    List<ProductDataItem> storeProductsList;

    public StoreProductAdapter(Context mContext, List<ProductDataItem> storeProductsList) {
        this.mContext = mContext;
        this.storeProductsList = storeProductsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.store_product_list_item,parent,false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
       ProductDataItem item=  storeProductsList.get(position);
        TextView productName=holder.itemView.findViewById(R.id.product_name_id);
        TextView  productPrice=holder.itemView.findViewById(R.id.product_price_id);
        if(PreferenceHelper.getSharedPreference(mContext).equals("en")){
            productName.setText(item.getEnName());
            productPrice.setText(item.getPrice());
        }else {
            productName.setText(item.getArName());
            productPrice.setText(String.valueOf(item.getPrice()));
        }

    }

    @Override
    public int getItemCount() {
        Log.v(TAG,"storessss size"+storeProductsList.size());
        return storeProductsList.size();
          }

    public class Holder  extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}