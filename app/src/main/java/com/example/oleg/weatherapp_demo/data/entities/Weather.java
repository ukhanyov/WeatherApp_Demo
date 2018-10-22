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
                   String sunsetTime) {

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
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public void setTemperatureMax(String mTemperatureMax) {
        this.mTemperatureMax = mTemperatureMax;
    }

    public void setTemperatureMin(String mTemperatureMin) {
        this.mTemperatureMin = mTemperatureMin;
    }

    public void setHumidity(String mHumidity) {
        this.mHumidity = mHumidity;
    }

    public void setPressure(String mPressure) {
        this.mPressure = mPressure;
    }

    public void setWindSpeed(String mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public void setCoordinates(String mCoordinates) {
        this.mCoordinates = mCoordinates;
    }

    public void setTypeOfDay(String mTypeOfDay) {
        this.mTypeOfDay = mTypeOfDay;
    }

    public void setPrecipProbability(String mPrecipProbability) {
        this.mPrecipProbability = mPrecipProbability;
    }

    public void setSunriseTime(String mSunriseTime) {
        this.mSunriseTime = mSunriseTime;
    }

    public void setSunsetTime(String mSunsetTime) {
        this.mSunsetTime = mSunsetTime;
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
}
