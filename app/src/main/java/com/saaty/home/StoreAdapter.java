package com.saaty.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saaty.R;
import com.saaty.models.DataItem;
import com.saaty.util.OnItemClickInterface;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.OVER_SCROLL_NEVER;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.Holder> {
    Context mContext;
    List<DataItem> storesList;
    OnItemClickInterface mOnItemClick;

    public StoreAdapter(Context mContext, List<DataItem> list,OnItemClickInterface onItemClickInterface) {
        this.mContext = mContext;
        this.storesList=list;
        this.mOnItemClick=onItemClickInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.store_list_item,parent,false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder,final int position) {
       DataItem dataItem= storesList.get(position);
        TextView storeName=holder.itemView.findViewById(R.id.store_name_id);
        storeName.setText(dataItem.getStoreArName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.v(TAG,"stores size"+storesList.size());
        return storesList.size();

    }

    public class Holder  extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
