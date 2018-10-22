package com.example.oleg.weatherapp_demo.utils;

import java.util.Calendar;
import java.util.Date;

public class NormalizeDate {

    public static String getHumanFriendlyDayOfWeek(long dbDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dbDate * 1000L);
        return getFriendlyDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getHumanFriendlyDateFromDB(long dbDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dbDate * 1000L);
        return getFriendlyDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)) + ", " +
                getFriendlyMonth(calendar.get(Calendar.MONTH)) + " " +
                calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getHumanFriendlyTimeFromDB(long dbDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dbDate * 1000L);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + "0";
    }

    private static String getFriendlyDayOfTheWeek(int key) {
        switch (key) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wensday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "NaN";
        }
    }

    private static String getFriendlyMonth(int key){
        switch (key) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "NaN";
        }
    }

    public static boolean checkIfItIsToday(String inputDate){
        Calendar currentTime = Calendar.getInstance();

        //Divide by 1000 because DarkWeather time is in seconds, not milliseconds
        long date = currentTime.getTimeInMillis()  / 1000L;

        return getHumanFriendlyTimeFromDB(date).equals(getHumanFriendlyTimeFromDB(Long.valueOf(inputDate)));
    }

    public static boolean checkIfTimeIsNow(long dbDate){
        Calendar calendarFromDb = Calendar.getInstance();
        calendarFromDb.setTimeInMillis(dbDate * 1000L);

        Calendar currentTime = Calendar.getInstance();
        return (calendarFromDb.get(Calendar.HOUR_OF_DAY) == currentTime.get(Calendar.HOUR_OF_DAY) &&
                calendarFromDb.get(Calendar.DAY_OF_MONTH) == currentTime.get(Calendar.DAY_OF_MONTH));
    }
}
