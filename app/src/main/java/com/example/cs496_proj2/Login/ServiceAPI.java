package com.example.cs496_proj2.Login;

import com.facebook.AccessToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface ServiceAPI {

    @GET ("/login/{userid}/{token}/{appId}")
    //Call<ResponseBody> login(@Path("AppID") String AppID, @Path("token") AccessToken token, @Path("UserID") String UserID);
    Call<ResponseBody> login(@Path("userid") String userid, @Path("token") String token, @Path("appId") String appId);

    @POST ("/post")
    Call<ResponseBody> register(AccessToken token);
}
