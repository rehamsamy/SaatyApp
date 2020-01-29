package com.saaty.sideMenuScreen.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.models.MessageArrayModel;
import com.saaty.models.MessageObjectModel;
import com.saaty.models.RebliesArrayItem;
import com.saaty.util.OnItemClickRecyclerViewInterface;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class RebliesAdapter  extends RecyclerView.Adapter<RebliesAdapter.ViewHolder>implements MessageActivity.OnMessageDeleteInterface {
    List<RebliesArrayItem> messageArrayModelList;
    OnItemClickRecyclerViewInterface mInterface;
    ImageView emptyBox,checkedBox;
    Context mContext;
    int position;
    int flag;

    public RebliesAdapter(List<RebliesArrayItem> messageArrayModelList, Context mContext,int flag) {
        this.messageArrayModelList = messageArrayModelList;
        this.mContext = mContext;
        this.flag=flag;
    }

    public  void setOnClick(OnItemClickRecyclerViewInterface mInterface){
        this.mInterface=mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.message_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.position=position;
        RebliesArrayItem model=messageArrayModelList.get(position);
        TextView name=holder.itemView.findViewById(R.id.message_sent_receive_name);
        TextView time=holder.itemView.findViewById(R.id.message_time);
        TextView message=holder.itemView.findViewById(R.id.message_body);
        emptyBox=holder.itemView.findViewById(R.id.empty_box);
        checkedBox=holder.itemView.findViewById(R.id.checked_box);

        time.setText(model.getCreatedAt());
        message.setText(model.getReply());
        name.setText(HomeActivity.userModel.getFullname());






    }


    @Override
    public int getItemCount() {
        return messageArrayModelList.size();
    }

    @Override
    public void OnClick() {
        emptyBox.setVisibility(View.VISIBLE);
        checkedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedBox.setVisibility(View.VISIBLE);
                mInterface.OnItemClick(position);
            }
        });

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }
}
