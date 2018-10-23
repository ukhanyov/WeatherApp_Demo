package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJWeekly {

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("daily")
    @Expose
    private PJWeeklyArray PJWeeklyArray;

    public PJWeeklyArray getPJWeeklyArray() {
        return PJWeeklyArray;
    }

    public String getTimezone() {
        return timezone;
    }
}
