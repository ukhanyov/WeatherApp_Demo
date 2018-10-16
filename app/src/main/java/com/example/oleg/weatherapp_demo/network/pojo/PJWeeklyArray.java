package com.example.oleg.weatherapp_demo.network.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJWeeklyArray {

    @SerializedName("data")
    @Expose
    private List<PJWeeklySpecificDay> data = null;

    public List<PJWeeklySpecificDay> getData() {
        return data;
    }

}
