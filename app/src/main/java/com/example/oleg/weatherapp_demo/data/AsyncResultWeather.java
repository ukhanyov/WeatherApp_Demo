package com.example.oleg.weatherapp_demo.data;

import java.util.List;

public interface AsyncResultWeather {
    void asyncFinished(List<Weather> result);
}
