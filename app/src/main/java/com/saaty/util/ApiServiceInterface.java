package com.saaty.util;

import com.saaty.models.AdsProductsModel;
import com.saaty.models.CategoryModel;
import com.saaty.models.CheckWishlistModel;
import com.saaty.models.DataObjectModel;
import com.saaty.models.GetProductImagesModel;
import com.saaty.models.LoginModel;
import com.saaty.models.ProductDataModel;
import com.saaty.models.RegisterModel;
import com.saaty.models.SendCodeModel;
import com.saaty.models.StoreListModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

   /////////////////stores//////////////////////////////
   @GET("/api/stores")
    Call<StoreListModel> getStoresList(@QueryMap Map<String,Object> map);

//////////////////////////////   get category product list  watchets or brackletes////////////////

    @GET("/api/products")
    Call<StoreListModel> getCategoryProductsList(@QueryMap Map<String,Object> map);

/////////////////////////////////////////



   @GET("/api/storeproducts/{id}")
    Call<StoreListModel> gerStoreProducts(@Path("id") int id,@QueryMap Map<String,Object> map);



//   get wish list????////////////////
    @GET("/api/wishproducts")
    Call<StoreListModel> getWishlist(@QueryMap Map<String,Object> map, @Header("Accept") String accept, @Header("Authorization") String token);

    /////////////////////add product to wish list///////////////


    @POST("/api/addtowishlist")
    Call<AdsProductsModel> addProuctToWishlist(@Header("Accept")String accept,@Header("Authorization") String token,@Query("product_id") int product_id);


    /////// about app /////////////////
    @POST("/api/getscreen")
    Call<ProductDataModel>  getAboutAppData(@Body Map<String,Object> map);


    //////////////////////////// contact details///////////////
    @GET("/api/getContactDetails")
    Call<ProductDataModel> getContactDetails(@Query ("key") String key);

//////////////////////////////////add ads//////////////////////////

    @Multipart
    @POST("/api/products")
    Call<AdsProductsModel> addAdsProduct(@Header("Accept") String accept
            , @Header("Authorization") String token
            , @QueryMap Map<String, Object> map
             ,@Part  MultipartBody.Part[] images);
//             , @Part("photos[]") RequestBody requestBody
//            , @Part  MultipartBody.Part[] images



    ///////////////////////////get slider images////////////////
    @GET("/api/sliderimages")
    Call<ProductDataModel> getSliderImages();
////////////////////////////////  getlist of product images//////////////

    @GET("/api/productimages/{id}")
    Call<GetProductImagesModel> getProductImagesList(@Path("id") int id);

    ////////////////////check wishlist product/////////////

    @GET("/api/wishlist/")
    Call<CheckWishlistModel> checkWishlistProduct(@Query("product_id") int id,@Header("Accept")String accept,@Header("Authorization") String auth);

    //////////////  delete from wishlist////////////

    @DELETE("/api/wishlist?")
    Call<CheckWishlistModel> deleteItemFromWishlist(@Header("Accept")String accept,@Header("Authorization") String auth,@Query("product_id")int id);
}
