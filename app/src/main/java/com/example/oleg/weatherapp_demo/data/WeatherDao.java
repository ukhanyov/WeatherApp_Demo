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

    @Query("SELECT * FROM weather_table ORDER BY date ASC")
    LiveData<List<Weather>> getAllWeather();

    @Query("SELECT * FROM weather_table WHERE date = :weatherDate")
    LiveData<Weather> getSingleWeather(String weatherDate);
}