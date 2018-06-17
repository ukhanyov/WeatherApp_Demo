package com.example.oleg.weatherapp_demo.network;

import com.example.oleg.weatherapp_demo.data.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface GetDataService {
    //@GET("/forecast/{access_key}/{location}?{units}&{exclude}")
    @GET("/forecast/31b4710c5ae2b750bb6227c0517f84de/37.8267,-122.4233?units=si&exclude=currently,minutely,hourly,flags")
    Call<ParsedJSON> getAllWeather();
}
