package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

// Timeline 5

public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository mRepository;
    private LiveData<List<Weather>> mAllWeather;
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeatherRepository(application);
        mAllWeather = mRepository.getAllWeather();
    }

    public LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

    public void insert(Weather weather) {
        mRepository.insert(weather);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
