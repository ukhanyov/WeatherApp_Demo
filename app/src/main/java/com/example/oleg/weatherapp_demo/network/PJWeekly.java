package com.example.oleg.weatherapp_demo.network;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJWeekly {

//    @SerializedName("latitude")
//    @Expose
//    private Double latitude;

//    @SerializedName("longitude")
//    @Expose
//    private Double longitude;

//    @SerializedName("timezone")
//    @Expose
//    private String timezone;

    @SerializedName("daily")
    @Expose
    private PJWeeklyArray PJWeeklyArray;

//    @SerializedName("offset")
//    @Expose
//    private Long offset;

//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getTimezone() {
//        return timezone;
//    }
//
//    public void setTimezone(String timezone) {
//        this.timezone = timezone;
//    }

    public PJWeeklyArray getPJWeeklyArray() {
        return PJWeeklyArray;
    }

//    public void setPJWeeklyArray(PJWeeklyArray PJWeeklyArray) {
//        this.PJWeeklyArray = PJWeeklyArray;
//    }

//
//    public Long getOffset() {
//        return offset;
//    }
//
//    public void setOffset(Long offset) {
//        this.offset = offset;
//    }

}
