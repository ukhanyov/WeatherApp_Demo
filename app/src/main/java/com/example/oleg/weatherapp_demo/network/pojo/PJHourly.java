package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJHourly {

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("hourly")
    @Expose
    private PJHourlyArray hourlyArray;

    public PJHourlyArray getHourlyArray() {
        return hourlyArray;
    }

    public String getTimezone() {
        return timezone;
    }
}
