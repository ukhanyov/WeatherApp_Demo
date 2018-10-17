package com.example.oleg.weatherapp_demo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyLocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MyLocation myLocation);

    @Query("DELETE FROM location_table")
    void deleteAll();

    @Query("DELETE FROM location_table WHERE locationName = :name")
    void deleteSpecificLocation(String name);

    @Query("SELECT * FROM location_table ORDER BY locationName ASC")
    LiveData<List<MyLocation>> getAllLocations();

    @Query("SELECT * FROM location_table WHERE locationName = :name")
    MyLocation getSpecificLocation(String name);
}
