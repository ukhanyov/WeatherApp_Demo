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
    private LiveData<List<Weather>> mAllDailyWeather;
    private LiveData<List<Weather>> mAllHourlyWeather;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeatherRepository(application);
        mAllWeather = mRepository.getAllWeather();
        mAllDailyWeather = mRepository.getAllDailyWeather();
        mAllHourlyWeather = mRepository.getAllHourlyWeather();
    }

    public LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

    public LiveData<List<Weather>> getAllDailyWeather() { return mAllDailyWeather; }

    public LiveData<List<Weather>> getAllHourlyWeather() { return mAllHourlyWeather; }

    public void insert(Weather weather) {
        mRepository.insert(weather);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteSpecificWeatherByDate(String deleteKey) {mRepository.deleteSpecificWeatherByDate(deleteKey);}

    public void deleteSpecificWeatherByType(String deleteKey) {mRepository.deleteSpecificWeatherByType(deleteKey);}

}
