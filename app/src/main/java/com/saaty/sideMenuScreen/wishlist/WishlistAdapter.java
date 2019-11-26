package com.saaty.sideMenuScreen.wishlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saaty.R;
import com.saaty.models.ProductDataItem;
import com.saaty.util.OnItemClickInterface;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistAdapter  extends RecyclerView.Adapter<WishlistAdapter.Holder> {
    Context mContext;
    List<ProductDataItem> wishlistProducts;
    OnItemClickInterface mOnItemInterface;
    public WishlistAdapter(Context mContext,  List<ProductDataItem> wishlistProducts,OnItemClickInterface mOnItemInterface) {
        this.mContext = mContext;
        this.wishlistProducts=wishlistProducts;
        this.mOnItemInterface=mOnItemInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(mContext).inflate(R.layout.store_product_list_item,parent,false);
       return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ProductDataItem item=wishlistProducts.get(position);
        ImageView heartImage=holder.itemView.findViewById(R.id.wish_list_img);
        heartImage.setImageResource(R.drawable.wishlist_select);
        TextView wishlistPrice=holder.itemView.findViewById(R.id.product_price_id);
        TextView wishlistName=holder.itemView.findViewById(R.id.product_name_id);

        wishlistPrice.setText(String.valueOf(item.getPrice())+" RS");
        wishlistName.setText(item.getArName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemInterface.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishlistProducts.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
