package com.saaty.home.StoresProduct;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saaty.R;
import com.saaty.models.DataArrayModel;
import com.saaty.models.DataItem;
import com.saaty.models.ProductDataItem;
import com.saaty.util.OnItemClickRecyclerViewInterface;
import com.saaty.util.PreferenceHelper;
import com.saaty.util.urls;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoreProductAdapter extends RecyclerView.Adapter<StoreProductAdapter.Holder> {

    private static final String TAG =StoreProductAdapter.class.getSimpleName() ;
    Context mContext;
    List<DataArrayModel> storeProductsList;
   OnItemClickRecyclerViewInterface mInterface;

    public StoreProductAdapter(Context mContext, List<DataArrayModel> storeProductsList) {
        this.mContext = mContext;
        this.storeProductsList = storeProductsList;
    }

    public void setOnItemClickListenerRecyclerView(OnItemClickRecyclerViewInterface mInterface){
        this.mInterface=mInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.store_product_list_item,parent,false);
        return new Holder(view,mInterface);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
       DataArrayModel  item=  storeProductsList.get(position);
        TextView productName=holder.itemView.findViewById(R.id.product_name_id);
        TextView  productPrice=holder.itemView.findViewById(R.id.product_price_id);
        ImageView productImg=holder.itemView.findViewById(R.id.store_img_id);
        if(PreferenceHelper.getSharedPreference(mContext).equals("en")){
            productName.setText(item.getEnName());
            productPrice.setText(item.getPrice());
        }else {
            productName.setText(item.getArName());
            productPrice.setText(String.valueOf(item.getPrice()));
        }
        if (item.getProductimages().size() > 0) {
            Picasso.with(mContext).load(urls.base_url+"/"+item.getProductimages().get(0).getImageLink()).placeholder(R.drawable.watch_item2).into(productImg);
        }else {
            productImg.setImageResource(R.drawable.watch_item2);
        }


    }

    @Override
    public int getItemCount() {
        Log.v(TAG,"storessss size"+storeProductsList.size());
        return storeProductsList.size();
          }

    public class Holder  extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView,OnItemClickRecyclerViewInterface mInterface) {
            super(itemView);
            ImageView wishlistImg=itemView.findViewById(R.id.wish_list_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface!=null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mInterface.OnItemClick(position);
                        }
                    }
                }
            });

            wishlistImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mInterface!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mInterface.OnWishListClick(position,wishlistImg);
                        }
                    }
                }
            });


        }
    }
}