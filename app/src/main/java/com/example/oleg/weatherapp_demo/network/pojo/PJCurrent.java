package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJCurrent {

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("currently")
    @Expose
    private PJCurrentInstance currently;

    public PJCurrentInstance getCurrently() {
        return currently;
    }

    public String getTimezone() {
        return timezone;
    }
}
