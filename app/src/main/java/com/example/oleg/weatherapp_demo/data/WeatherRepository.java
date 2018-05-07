package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

// Timeline 4

public class WeatherRepository {

    private WeatherDao mWeatherDao;
    private LiveData<List<Weather>> mAllWeather;

    public WeatherRepository(Application application) {

    }
}
