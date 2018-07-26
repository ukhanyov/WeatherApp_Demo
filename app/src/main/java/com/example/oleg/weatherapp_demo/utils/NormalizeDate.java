package com.example.oleg.weatherapp_demo.utils;

import java.util.Calendar;
import java.util.Date;

public class NormalizeDate {

    public static String getHumanFriendlyDateFromDB(long dbDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dbDate * 1000L);
        return getFriendlyDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                calendar.get(Calendar.MONTH) + "-" +
                calendar.get(Calendar.YEAR);
    }

    private static String getFriendlyDayOfTheWeek(int key) {
        switch (key) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wen";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            default:
                return "NanN";
        }
    }

    public static boolean checkIfItIsToday(String inputDate){
        Calendar currentTime = Calendar.getInstance();

        //Divide by 1000 because DarkWeather time is in seconds, not milliseconds
        String compareDate = getHumanFriendlyDateFromDB(currentTime.getTimeInMillis() / 1000L);
        return inputDate.equals(compareDate);
    }
}
