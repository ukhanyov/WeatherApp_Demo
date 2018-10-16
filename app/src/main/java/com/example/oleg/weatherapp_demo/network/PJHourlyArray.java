package com.example.oleg.weatherapp_demo.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PJHourlyArray {
    @SerializedName("data")
    @Expose
    private List<PJHourlyInstance> data = null;

    public List<PJHourlyInstance> getData() {
        return data;
    }
}
