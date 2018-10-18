package com.example.oleg.weatherapp_demo.data;

import com.example.oleg.weatherapp_demo.data.entities.Weather;

import java.util.List;

public interface AsyncResultWeatherHourly {
    void asyncFinishedHourly(List<Weather> result);
}
