package com.saaty.util;

import com.saaty.models.CategoryModel;
import com.saaty.models.LoginModel;
import com.saaty.models.ProductDataItem;
import com.saaty.models.ProductDataModel;
import com.saaty.models.RegisterModel;
import com.saaty.models.SendCodeModel;

import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiServiceInterface {


    @POST("/oauth/token")
    Call<LoginModel> loginUser(@Body Map<String,Object> map);

    @POST("/api/register")
    Call<RegisterModel> registerUser(@Body Map<String, Object> map);



    @GET("/api/SendMobileResetCode/{id}")
    Call<SendCodeModel> sendMobileCode(@Path("id")String email);

   @POST("/api/CheckResetCode")
    Call<SendCodeModel> checkResetCode(@Body Map<String,Object> map);

   @POST("/api/ResetPassword")
    Call<SendCodeModel> resetPassword(@Body Map<String,Object> map);


   @GET("/api/categories")
    Call<CategoryModel> getCatogory();

   @GET("/api/stores")
    Call<CategoryModel> getStoresList();


   @GET("/api/storeproducts/{id}")
    Call<ProductDataModel> gerStoreProducts(@Path("id") int id);



//   get wish list????////////////////
    @GET("/api/wishproducts")
    Call<ProductDataModel> getWishlist(@Header("Accept") String accept,@Header("Authorization") String token);


    /////// about app /////////////////
    @POST("/api/getscreen")
    Call<ProductDataModel>  getAboutAppData(@Body Map<String,Object> map);


    //////////////////////////// contact details///////////////
    @GET("/api/getContactDetails")
    Call<ProductDataModel> getContactDetails(@Query ("key") String key);


}