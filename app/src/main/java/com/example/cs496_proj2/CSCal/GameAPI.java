package com.example.cs496_proj2.CSCal;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;

public interface GameAPI {
    @GET("/game/{char}")
    Call<ResponseBody> getword(@Path("char") char word);

    @GET("/game/{str}")
    Call<ResponseBody> CheckVal(@Path("str") String str);

    @POST("/game/score/{userid}")
    Call<ResponseBody> PostScore(@Path("userid") String gobalid, String score);
}
