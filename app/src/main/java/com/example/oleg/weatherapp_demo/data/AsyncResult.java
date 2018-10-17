package com.example.oleg.weatherapp_demo.data;

import java.util.List;

public interface AsyncResult {
    void asyncFinished(List<Weather> result);
}
