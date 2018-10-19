package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.oleg.weatherapp_demo.data.entities.MyLocation;

import java.util.List;

public class MyLocationViewModel extends AndroidViewModel {

    private MyLocationRepository myLocationRepository;
    private LiveData<List<MyLocation>> mAllLocations;
    private MutableLiveData<MyLocation> mSearchResult;

    public MyLocationViewModel(@NonNull Application application) {
        super(application);

        myLocationRepository = new MyLocationRepository(application);
        mAllLocations = myLocationRepository.getAllMyLocations();
        mSearchResult = myLocationRepository.getSearchResults();
    }

    public LiveData<List<MyLocation>> getAllLocations() {
        return mAllLocations;
    }

    public void insert(MyLocation myLocation) {
        myLocationRepository.insertMyLocation(myLocation);
    }

    public void deleteAll() {
        myLocationRepository.deleteAll();
    }

    public void deleteSpecificLocation(String name) {
        myLocationRepository.deleteSpecificMyLocation(name);
    }

    public void queryForSpecifiedLocation(String name){
        myLocationRepository.querySpecificMyLocation(name);
    }

    public MutableLiveData<MyLocation> getSpecificLocation() {
        return mSearchResult;
    }
}
