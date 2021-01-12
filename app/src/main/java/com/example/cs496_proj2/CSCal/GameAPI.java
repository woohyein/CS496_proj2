package com.example.cs496_proj2.CSCal;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;

public interface GameAPI {
    @GET("/game/{str}")
    Call<ResponseBody> GetWord(@Path("str") String word);

    @GET("/game/{str}")
    Call<ResponseBody> CheckVal(@Path("str") String str);

    @GET ("/game/{player}/{score}")
    Call<ResponseBody> PostScore(@Path("player") String name, @Path("score") int score);

    @GET("/start/{level}")
    Call<ResponseBody> Init(@Path("level") int level);

}
