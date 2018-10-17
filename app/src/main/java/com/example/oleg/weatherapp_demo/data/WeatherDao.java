package com.example.oleg.weatherapp_demo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

// Timeline 2

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Weather weather);

    @Query("DELETE FROM weather_table")
    void deleteAll();

    @Query("DELETE FROM weather_table WHERE date = :deleteString")
    void deleteSpecificWeather(String deleteString);

    @Query("SELECT * FROM weather_table ORDER BY date ASC")
    LiveData<List<Weather>> getAllWeather();

    @Query("SELECT * FROM weather_table WHERE date = :dateQueryKey")
    List<Weather> getSpecificWeatherEntry(String dateQueryKey);

    @Query("SELECT * FROM weather_table WHERE type_of_day = :typeOfDay")
    LiveData<List<Weather>> getAllWeatherSpecifiedByDayType(String typeOfDay);
}