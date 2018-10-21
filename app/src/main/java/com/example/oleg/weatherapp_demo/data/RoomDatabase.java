package com.example.oleg.weatherapp_demo.data;

// Timeline 3

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.oleg.weatherapp_demo.data.dao.MyLocationDao;
import com.example.oleg.weatherapp_demo.data.dao.WeatherDao;
import com.example.oleg.weatherapp_demo.data.entities.MyLocation;
import com.example.oleg.weatherapp_demo.data.entities.Weather;

@Database(entities = {Weather.class, MyLocation.class}, version = 6)
public abstract class RoomDatabase extends android.arch.persistence.room.RoomDatabase {

    public abstract WeatherDao weatherDao();
    public abstract MyLocationDao myLocationDao();

    private static RoomDatabase INSTANCE;

    public static RoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (RoomDatabase.class){
                if(INSTANCE == null){
                    // Create data base here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "weather_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
