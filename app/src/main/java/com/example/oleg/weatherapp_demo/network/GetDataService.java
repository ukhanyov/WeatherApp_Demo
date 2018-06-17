package com.example.oleg.weatherapp_demo.network;

import com.example.oleg.weatherapp_demo.data.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {
    @GET("/forecast/{access_key}/{location}?{units}")
    Call<List<Weather>> getAllWeather(
            @Path("access_key") String access_key,
            @Path("location") String location,
            @Path("units") String units
    );
}
