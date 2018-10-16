package com.example.oleg.weatherapp_demo.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface GetDataService {
    @GET("/forecast/{access_key}/{location}")
    Call<PJWeekly> getAllWeather(
            @Path("access_key") String access_key,
            @Path("location") String location,
            @QueryMap Map<String, String> options
    );

    @GET("/forecast/{access_key}/{location}")
    Call<PJCurrent> getCurrentWeather(
            @Path("access_key") String access_key,
            @Path("location") String location,
            @QueryMap Map<String, String> options
    );

    @GET("/forecast/{access_key}/{location}")
    Call<PJHourly> getHourlyWeather(
            @Path("access_key") String access_key,
            @Path("location") String location,
            @QueryMap Map<String, String> options
    );
}
