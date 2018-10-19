package com.example.oleg.weatherapp_demo.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.oleg.weatherapp_demo.data.entities.Weather;

import java.util.List;

// Timeline 2

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Weather weather);

    @Query("DELETE FROM weather_table")
    void deleteAll();

    @Query("DELETE FROM weather_table WHERE type_of_day = :deleteTypeString AND coordinates = :deleteLocationString")
    void deleteSpecificfWeatherByTypeAndLocation(String deleteTypeString, String deleteLocationString);

    @Query("SELECT * FROM weather_table WHERE coordinates = :coordinatesKey AND type_of_day =:typeOfDay")
    List<Weather> getAllWeatherByCoordinatesAndType(String coordinatesKey, String typeOfDay);
}