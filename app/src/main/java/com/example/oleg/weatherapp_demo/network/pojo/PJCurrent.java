package com.example.oleg.weatherapp_demo.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJCurrent {

    @SerializedName("currently")
    @Expose
    private PJCurrentInstance currently;

    public PJCurrentInstance getCurrently() {
        return currently;
    }

}
