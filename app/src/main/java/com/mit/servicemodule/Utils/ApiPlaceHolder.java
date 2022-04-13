package com.mit.servicemodule.Utils;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiPlaceHolder {

    @POST("services-add")
    Call<ResponseBody> addservice(@Body JsonObject jsonObject);

    @POST("services-getlist")
    Call<ResponseBody> listservice(@Body JsonObject jsonObject);

    @POST("services-update")
    Call<ResponseBody> updateservice(@Body JsonObject jsonObject);

    @POST("services-delete")
    Call<ResponseBody> deleteservice(@Body JsonObject jsonObject);

    @POST("services-add-category")
    Call<ResponseBody> addservicecategory(@Body JsonObject jsonObject);

    @POST("services-getlist-category")
    Call<ResponseBody> listservicecategory(@Body JsonObject jsonObject);

    @POST("services-update-category")
    Call<ResponseBody> updatesecategoryrvice(@Body JsonObject jsonObject);

    @POST("services-delete-category")
    Call<ResponseBody> deleteservicecategory(@Body JsonObject jsonObject);


    @Multipart
    @POST("services-uploadimage")
    Call<ResponseBody> uploadimage(@Part MultipartBody.Part survayImages,
                                   @Part("apikey") RequestBody apikey,
                                   @Part("userid") RequestBody userid,
                                    @Part("storeid") RequestBody storeid);
}
