package com.example.oleg.weatherapp_demo.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PJHourly {

    @SerializedName("hourly")
    @Expose
    private PJHourlyArray hourlyArray;

    public PJHourlyArray getHourlyArray() {
        return hourlyArray;
    }
}
