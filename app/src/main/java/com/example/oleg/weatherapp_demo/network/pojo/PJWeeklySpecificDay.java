package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJWeeklySpecificDay {

    @SerializedName("time")
    @Expose
    private Long time;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("humidity")
    @Expose
    private Double humidity;

    @SerializedName("pressure")
    @Expose
    private Double pressure;

    @SerializedName("windSpeed")
    @Expose
    private Double windSpeed;

    @SerializedName("temperatureMin")
    @Expose
    private Double temperatureMin;

    @SerializedName("temperatureMax")
    @Expose
    private Double temperatureMax;

    @SerializedName("precipIntensity")
    @Expose
    private Double mPrecipIntensity;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Double getPrecipIntensity() {
        return mPrecipIntensity;
    }

}
