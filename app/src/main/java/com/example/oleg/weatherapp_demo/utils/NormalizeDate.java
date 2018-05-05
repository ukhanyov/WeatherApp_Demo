package com.example.oleg.weatherapp_demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NormalizeDate {
    public static String getHumanFriendlyDate(long dbDate){
        Date date = new java.util.Date(dbDate*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
        return sdf.format(date);
    }
}
