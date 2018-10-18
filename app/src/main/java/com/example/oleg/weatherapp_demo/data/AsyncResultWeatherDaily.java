package com.example.oleg.weatherapp_demo.data;

import com.example.oleg.weatherapp_demo.data.entities.Weather;

import java.util.List;

public interface AsyncResultWeatherDaily {
    void asyncFinishedDaily(List<Weather> result);
}
