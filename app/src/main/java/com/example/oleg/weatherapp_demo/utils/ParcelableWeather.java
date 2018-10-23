package com.example.oleg.weatherapp_demo.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableWeather implements Parcelable {

    private Integer mId;
    private String mDate;
    private String mSummary;
    private String mTemperatureMax;
    private String mTemperatureMin;
    private String mHumidity;
    private String mPressure;
    private String mWindSpeed;
    private String mCoordinates;
    private String mTypeOfDay;
    private String mPrecipProbability;
    private String mSunriseTime;
    private String mSunsetTime;
    private String mTimezone;

    public ParcelableWeather(int id,
                             String date,
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

        this.mId = id;
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

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(String mTemperatureMax) {
        this.mTemperatureMax = mTemperatureMax;
    }

    public String getTemperatureMin() {
        return mTemperatureMin;
    }

    public void setTemperatureMin(String mTemperatureMin) {
        this.mTemperatureMin = mTemperatureMin;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String mHumidity) {
        this.mHumidity = mHumidity;
    }

    public String getPressure() {
        return mPressure;
    }

    public void setPressure(String mPressure) {
        this.mPressure = mPressure;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(String mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getCoordinates() {
        return mCoordinates;
    }

    public void setmoordinates(String mCoordinates) {
        this.mCoordinates = mCoordinates;
    }

    public String getTypeOfDay() {
        return mTypeOfDay;
    }

    public void setTypeOfDay(String mTypeOfDay) {
        this.mTypeOfDay = mTypeOfDay;
    }

    public String getPrecipProbability() {
        return mPrecipProbability;
    }

    public void setPrecipProbability(String mPrecipProbability) {
        this.mPrecipProbability = mPrecipProbability;
    }

    public String getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunriseTime(String mSunriseTime) {
        this.mSunriseTime = mSunriseTime;
    }

    public String getSunsetTime() {
        return mSunsetTime;
    }

    public void setSunsetTime(String mSunsetTime) {
        this.mSunsetTime = mSunsetTime;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String mTimezone) {
        this.mTimezone = mTimezone;
    }

    // Parcelling part
    public ParcelableWeather(Parcel in) {
        String[] data = new String[14];
        in.readStringArray(data);

        this.mId = Integer.valueOf(data[0]);
        this.mDate = data[1];
        this.mSummary = data[2];
        this.mTemperatureMax = data[3];
        this.mTemperatureMin = data[4];
        this.mHumidity = data[5];
        this.mPressure = data[6];
        this.mWindSpeed = data[7];
        this.mCoordinates = data[8];
        this.mTypeOfDay = data[9];
        this.mPrecipProbability = data[10];
        this.mSunriseTime = data[11];
        this.mSunsetTime = data[12];
        this.mTimezone = data[13];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.mId.toString(),
                this.mDate,
                this.mSummary,
                this.mTemperatureMax,
                this.mTemperatureMin,
                this.mHumidity,
                this.mPressure,
                this.mWindSpeed,
                this.mCoordinates,
                this.mTypeOfDay,
                this.mPrecipProbability,
                this.mSunriseTime,
                this.mSunsetTime,
                this.mTimezone});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ParcelableWeather createFromParcel(Parcel in) {
            return new ParcelableWeather(in);
        }

        public ParcelableWeather[] newArray(int size) {
            return new ParcelableWeather[size];
        }
    };
}
