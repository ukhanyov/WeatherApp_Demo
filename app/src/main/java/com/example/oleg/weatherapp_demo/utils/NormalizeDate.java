package com.example.oleg.weatherapp_demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NormalizeDate {

    private static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static String getHumanFriendlyDate(long dbDate){
        Date date = new java.util.Date(dbDate*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
        return sdf.format(date);
    }


    public static long getNormalizedUtcNow(long date) {
        long daysSinceEpoch = elapsedDaysSinceEpoch(date);
        return daysSinceEpoch * DAY_IN_MILLIS;
    }

    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }
}
