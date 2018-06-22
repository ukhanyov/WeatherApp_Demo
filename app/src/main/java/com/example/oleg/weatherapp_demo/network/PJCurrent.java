package com.example.oleg.weatherapp_demo.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJCurrent {
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

//    @SerializedName("timezone")
//    @Expose
//    private String timezone;

    @SerializedName("currently")
    @Expose
    private PJCurrentInstance currently;

//    @SerializedName("offset")
//    @Expose
//    private Integer offset;

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

//    public String getTimezone() {
//        return timezone;
//    }
//
//    public void setTimezone(String timezone) {
//        this.timezone = timezone;
//    }

    public PJCurrentInstance getCurrently() {
        return currently;
    }

    public void setCurrently(PJCurrentInstance currently) {
        this.currently = currently;
    }

//    public Integer getOffset() {
//        return offset;
//    }
//
//    public void setOffset(Integer offset) {
//        this.offset = offset;
//    }
}
