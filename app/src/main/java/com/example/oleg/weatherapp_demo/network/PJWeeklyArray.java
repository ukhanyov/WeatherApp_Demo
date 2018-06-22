package com.example.oleg.weatherapp_demo.network;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PJWeeklyArray {

//    @SerializedName("summary")
//    @Expose
//    private String summary;

//    @SerializedName("icon")
//    @Expose
//    private String icon;

    @SerializedName("data")
    @Expose
    private List<PJWeeklySpecificDay> data = null;

//    public String getSummary() {
//        return summary;
//    }
//
//    public void setSummary(String summary) {
//        this.summary = summary;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }

    public List<PJWeeklySpecificDay> getData() {
        return data;
    }

//    public void setData(List<PJWeeklySpecificDay> data) {
//        this.data = data;
//    }

}
