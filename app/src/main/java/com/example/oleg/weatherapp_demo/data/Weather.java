package com.example.oleg.weatherapp_demo.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

// Timeline 1

@Entity(tableName = "weather_table")
public class Weather {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "summary")
    private String mSummary;

    @ColumnInfo(name = "temp_max")
    private String mTemperatureMax;

    @ColumnInfo(name = "temp_min")
    private String mTemperatureMin;

    @ColumnInfo(name = "humidity")
    private String mHumidity;

    @ColumnInfo(name = "pressure")
    private String mPressure;

    @ColumnInfo(name = "wind_speed")
    private String mWindSpeed;

    @ColumnInfo(name = "type_of_day")
    private String mTypeOfDay;

    public Weather(@NonNull String date,
                   String summary,
                   String temperatureMax,
                   String temperatureMin,
                   String humidity,
                   String pressure,
                   String windSpeed,
                   String typeOfDay) {

        this.mDate = date;
        this.mSummary = summary;
        this.mTemperatureMax = temperatureMax;
        this.mTemperatureMin = temperatureMin;
        this.mHumidity = humidity;
        this.mPressure = pressure;
        this.mWindSpeed = windSpeed;
        this.mTypeOfDay = typeOfDay;
    }

    @NonNull
    public String getDate() {
        return mDate;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getTemperatureMax() {
        return mTemperatureMax;
    }

    public String getTemperatureMin() {
        return mTemperatureMin;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public String getPressure() {
        return mPressure;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public String getTypeOfDay() {
        return mTypeOfDay;
    }

    public void setTypeOfDay(String mTypeOfDay) {
        this.mTypeOfDay = mTypeOfDay;
    }
}
