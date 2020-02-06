package com.saaty.sideMenuScreen.messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.saaty.R;
import com.saaty.home.HomeActivity;
import com.saaty.loginAndRegister.LoginTraderUserActivity;
import com.saaty.models.DeleteMessageModel;
import com.saaty.models.MessageArrayModel;
import com.saaty.models.MessageModel;
import com.saaty.models.RebliesArrayItem;
import com.saaty.util.ApiClient;
import com.saaty.util.ApiServiceInterface;
import com.saaty.util.BaseActivity;
import com.saaty.util.DailogUtil;
import com.saaty.util.EndlessRecyclerViewScrollListener;
import com.saaty.util.NetworkAvailable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class MessageSendDetailsActivity extends BaseActivity {
    public static final String MESSAGE_MODEL_SEND = "message_model_send";
    @BindView(R.id.user_name_id)
    TextView userName;
    @BindView(R.id.message_body_id)
    TextView messageBody;
    @BindView(R.id.delete_btn_id)
    MaterialButton deleteId;
    @BindView(R.id.toolbar_txt_id)
    TextView toolarTxt;
    NetworkAvailable networkAvailable;
    ApiServiceInterface apiServiceInterface;
    DailogUtil dialogUtils;
    MessageArrayModel model;
    int user_message_id;
    Dialog mDailog;
    int current_page=1;

    RebliesAdapter rebliesAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_data_txt_id)TextView emptyData;
    List<RebliesArrayItem> messageRebliesList=new ArrayList<>();
    @BindView(R.id.progress_id)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_send_details);
        ButterKnife.bind(this);

        dialogUtils = new DailogUtil();
        mDailog = new Dialog(this);
        networkAvailable = new NetworkAvailable(this);
        Intent intent = getIntent();
        if (intent.hasExtra(MESSAGE_MODEL_SEND)) {
            model = MessageActivity.model;
            userName.setText(model.getRecepientData().getFullname());
            messageBody.setText(model.getMessage());
            toolarTxt.setText(model.getRecepientData().getFullname());
            user_message_id = model.getUsmId();
            Log.v("TAG", "messs" + user_message_id);
        }

        if (networkAvailable.isNetworkAvailable()) {
            buildReceivedMessageRecycler();
            getMessageReplies(current_page);
        } else {
            Toast.makeText(MessageSendDetailsActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.delete_btn_id)
    void setDeleteId() {
        mDailog.setContentView(R.layout.delete_product_layout);
        MaterialButton deleteBtn = mDailog.findViewById(R.id.delete_btn_id);
        MaterialButton cancelBtn = mDailog.findViewById(R.id.cancel_btn_id);
        // ProgressBar progressBar1 = mDailog.findViewById(R.id.progress_id);
        mDailog.setCancelable(false);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkAvailable.isNetworkAvailable()) {
                    deleteMessage();
                } else {
                    Toast.makeText(MessageSendDetailsActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDailog.dismiss();
            }
        });

        mDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDailog.show();


    }

    private void deleteMessage() {
        ProgressDialog progressDialog = dialogUtils.showProgressDialog(MessageSendDetailsActivity.this, getString(R.string.logging), false);
        apiServiceInterface = ApiClient.getClientService();
        String x = HomeActivity.access_token;
        Call<DeleteMessageModel> call = apiServiceInterface.deleteMessage("application/json", x, model.getUsmId());
        call.enqueue(new Callback<DeleteMessageModel>() {
            @Override
            public void onResponse(Call<DeleteMessageModel> call, Response<DeleteMessageModel> response) {
                if (response.body().isSuccess()) {
                    Toast.makeText(MessageSendDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(MessageSendDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DeleteMessageModel> call, Throwable t) {
                Toast.makeText(MessageSendDetailsActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();


            }
        });
    }


    @OnClick(R.id.toolbar_back_left_btn_id)
    void backClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//    Intent startMain = new Intent(Intent.ACTION_MAIN);
//    startMain.addCategory(Intent.CATEGORY_HOME);
//    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(startMain); }
        finish();

    }




    private void buildReceivedMessageRecycler() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        rebliesAdapter =new RebliesAdapter(messageRebliesList,MessageSendDetailsActivity.this,1);
        recyclerView.setAdapter(rebliesAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // if(tab_selected==0){
                current_page++;
                getMessageReplies(current_page);
                // }
            }
        });

    }

    private void getMessageReplies(int current_page) {
        Map<String,Object> map=new HashMap<>();
        map.put("page",current_page);
        map.put("limit",20);
        progressBar.setVisibility(VISIBLE);
        apiServiceInterface= ApiClient.getClientService();
        String x=  HomeActivity.access_token;
        Call<MessageModel> call=apiServiceInterface.getMessageReblies(user_message_id,"application/json",x,map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if(response.body().isSuccess()){
                    if(response.body().getMessageObjectModel().getRebliesModel().getRebliesArray().size()>0) {
                        messageRebliesList.addAll(response.body().getMessageObjectModel().getRebliesModel().getRebliesArray());
                        rebliesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }else if(response.body().getMessageObjectModel().getRebliesModel().getRebliesArray().size()==0&& current_page==1){
                        progressBar.setVisibility(View.GONE);
                        emptyData.setVisibility(VISIBLE);
                    }
                }else {
                    Toast.makeText(MessageSendDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

            }
        });

    }
}