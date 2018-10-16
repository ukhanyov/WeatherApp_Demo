package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJWeekly {

    @SerializedName("daily")
    @Expose
    private PJWeeklyArray PJWeeklyArray;

    public PJWeeklyArray getPJWeeklyArray() {
        return PJWeeklyArray;
    }


}
