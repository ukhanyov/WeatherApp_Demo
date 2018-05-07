package com.example.oleg.weatherapp_demo.data;

// Timeline 3

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Weather.class}, version = 1)
public abstract class WeatherRoomDatabase extends RoomDatabase{

    public abstract WeatherDao weatherDao();

    private static WeatherRoomDatabase INSTANCE;

    private static WeatherRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (WeatherRoomDatabase.class){
                if(INSTANCE == null){
                    // Create data base here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherRoomDatabase.class, "weather_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}