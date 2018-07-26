package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

// Timeline 5

public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository mRepository;
    private LiveData<List<Weather>> mAllWeather;
    private LiveData<Weather> singleWeather;
    private MutableLiveData<String> keySQ;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeatherRepository(application);
        mAllWeather = mRepository.getAllWeather();
        //loadWeather = mRepository.getSingleWeather();
        //loadWeather = Transformations.switchMap(filterLiveData, filter -> mRepository.getSingleWeather(filter));
    }

    public LiveData<Weather> init(String key){
        if(this.singleWeather != null){
            return null;
        }
        return singleWeather = mRepository.getSingleWeather(key);
    }

    public LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

//    public LiveData<Weather> getSingleWeather(String filter) {
//        //setSearchDate(filter);
//        filterLiveData.setValue(filter);
//        return loadWeather;
//    }

    //private void setSearchDate(String filter) { filterLiveData.setValue(filter); }

    public void insert(Weather weather) {mRepository.insert(weather);}

    public void deleteAll() {mRepository.deleteAll();}
}
