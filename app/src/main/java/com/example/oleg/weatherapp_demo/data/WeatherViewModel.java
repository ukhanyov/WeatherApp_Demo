package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.List;

// Timeline 5

public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository mRepository;
    private LiveData<List<Weather>> mAllWeather;
    private LiveData<Weather> loadWeather;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<String>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeatherRepository(application);
        mAllWeather = mRepository.getAllWeather();
        loadWeather = Transformations.switchMap(filterLiveData, filter -> mRepository.getSingleWeather(filter));
    }

    public LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

    public LiveData<Weather> getSingleWeather(String filter) {
        setSearchDate(filter);
        return loadWeather;
    }
    private void setSearchDate(String filter) { filterLiveData.setValue(filter); }

    public void insert(Weather weather) {mRepository.insert(weather);}

    public void deleteAll() {mRepository.deleteAll();}
}
