package com.example.oleg.weatherapp_demo.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface GetDataService {
    @GET("/forecast/{access_key}/{location}")
    Call<ParsedJSON> getAllWeather(
            @Path("access_key") String access_key,
            @Path("location") String location,
            @QueryMap Map<String, String> options
    );

    @GET("/forecast/{access_key}/{location}")
    Call<ParsedJSONCurrentWeather> getCurrentWeather(
            @Path("access_key") String access_key,
            @Path("location") String location,
            @QueryMap Map<String, String> options
    );
}
