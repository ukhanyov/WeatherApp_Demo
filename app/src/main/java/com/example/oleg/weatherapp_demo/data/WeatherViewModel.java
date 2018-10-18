package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.oleg.weatherapp_demo.data.entities.Weather;

import java.util.List;

// Timeline 5

public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository mRepository;
    private LiveData<List<Weather>> mAllWeather;
    private LiveData<List<Weather>> mAllDailyWeather;
    private LiveData<List<Weather>> mAllHourlyWeather;
    private MutableLiveData<List<Weather>> mWeatherDailyCoordinatesAndType;
    private MutableLiveData<List<Weather>> mWeatherHourlyCoordinatesAndType;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeatherRepository(application);
        //mAllWeather = mRepository.getAllWeather();
        //mAllDailyWeather = mRepository.getAllDailyWeather();
        //mAllHourlyWeather = mRepository.getAllHourlyWeather();
        mWeatherDailyCoordinatesAndType = mRepository.getSearchResultsDaily();
        mWeatherHourlyCoordinatesAndType = mRepository.getSearchResultsHourly();
    }

    public void insert(Weather weather) {
        mRepository.insert(weather);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteSpecificWeatherByTypeAndCoordinates(String deleteKeyType, String deleteKeyCoordinates) {mRepository.deleteSpecificWeatherByTypeAndCoordinates(deleteKeyType, deleteKeyCoordinates);}

    public void queryWeatherDailyByCoordinatesAndType(String coordinates, String type){
        mRepository.queryDailyByCoordinatesAndType(coordinates, type);
    }

    public MutableLiveData<List<Weather>> getWeatherDailyByCoordinatesAndType(){
        return mWeatherDailyCoordinatesAndType;
    }

    public void queryWeatherHourlyByCoordinatesAndType(String coordinates, String type){
        mRepository.queryHourlyByCoordinatesAndType(coordinates, type);
    }

    public MutableLiveData<List<Weather>> getWeatherHourlyByCoordinatesAndType(){
        return mWeatherHourlyCoordinatesAndType;
    }

}
