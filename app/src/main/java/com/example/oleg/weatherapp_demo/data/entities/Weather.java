package com.example.oleg.weatherapp_demo.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

// Timeline 1

@Entity(tableName = "weather_table")
public class Weather {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "weatherId")
    private Integer mId;

    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "summary")
    private String mSummary;

    @ColumnInfo(name = "temperature")
    private String mTemperatureMax;

    @ColumnInfo(name = "apparentTemperature")
    private String mTemperatureMin;

    @ColumnInfo(name = "humidity")
    private String mHumidity;

    @ColumnInfo(name = "pressure")
    private String mPressure;

    @ColumnInfo(name = "wind_speed")
    private String mWindSpeed;

    @ColumnInfo(name = "coordinates")
    private String mCoordinates;

    @ColumnInfo(name = "type_of_day")
    private String mTypeOfDay;

    @ColumnInfo(name = "precipProbability")
    private String mPrecipProbability;

    @ColumnInfo(name = "sunriseTime")
    private String mSunriseTime;

    @ColumnInfo(name = "sunsetTime")
    private String mSunsetTime;

    @ColumnInfo(name = "timezone")
    private String mTimezone;

    public Weather(String date,
                   String summary,
                   String temperatureMax,
                   String temperatureMin,
                   String humidity,
                   String pressure,
                   String windSpeed,
                   String coordinates,
                   String typeOfDay,
                   String precipProbability,
                   String sunriseTime,
                   String sunsetTime,
                   String timezone) {

        this.mDate = date;
        this.mSummary = summary;
        this.mTemperatureMax = temperatureMax;
        this.mTemperatureMin = temperatureMin;
        this.mHumidity = humidity;
        this.mPressure = pressure;
        this.mWindSpeed = windSpeed;
        this.mCoordinates = coordinates;
        this.mTypeOfDay = typeOfDay;
        this.mPrecipProbability = precipProbability;
        this.mSunriseTime = sunriseTime;
        this.mSunsetTime = sunsetTime;
        this.mTimezone = timezone;
    }

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

    public Integer getId() { return mId;}

    public String getCoordinates() {
        return mCoordinates;
    }

    public void setId(Integer id) { this.mId = id; }

    public String getPrecipProbability() {
        return mPrecipProbability;
    }

    public String getSunriseTime() {
        return mSunriseTime;
    }

    public String getSunsetTime() {
        return mSunsetTime;
    }

    public String getTimezone() {
        return mTimezone;
    }
}
