package com.example.oleg.weatherapp_demo.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PJHourly {

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("hourly")
    @Expose
    private List<PJHourlyInstance> hourlyList;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<PJHourlyInstance> getHourlyList() {
        return hourlyList;
    }

    public void setHourlyList(List<PJHourlyInstance> hourlyList) {
        this.hourlyList = hourlyList;
    }
}
