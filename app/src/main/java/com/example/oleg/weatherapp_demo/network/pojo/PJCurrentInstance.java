package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJCurrentInstance {
    @SerializedName("time")
    @Expose
    private Integer time;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("temperature")
    @Expose
    private Double temperatureMax;

    @SerializedName("apparentTemperature")
    @Expose
    private Double temperatureMin;

    @SerializedName("humidity")
    @Expose
    private Double humidity;

    @SerializedName("pressure")
    @Expose
    private Double pressure;

    @SerializedName("windSpeed")
    private Double windSpeed;

    public Integer getTime() {
        return time;
    }

    public String getIcon() {
        return icon;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }
}
