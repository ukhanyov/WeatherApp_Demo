package com.example.oleg.weatherapp_demo.utils;

import java.util.Calendar;
import java.util.Date;

public class NormalizeDate {

    public static String getHumanFriendlyDateFromDB(long dbDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dbDate * 1000L);
        return getFriendlyDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                getFriendlyMonth(calendar.get(Calendar.MONTH)) + "-" +
                calendar.get(Calendar.YEAR);
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
                return "NaN";
        }
    }

    private static String getFriendlyMonth(int key){
        switch (key) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sept";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "NaN";
        }
    }

    public static boolean checkIfItIsToday(String inputDate){
        Calendar currentTime = Calendar.getInstance();

        //Divide by 1000 because DarkWeather time is in seconds, not milliseconds
        String compareDate = getHumanFriendlyDateFromDB(currentTime.getTimeInMillis()  / 1000L  );
        return inputDate.equals(compareDate);
    }

    public static boolean checkIfTimeIsNow(long dbDate){
        Calendar calendarFromDb = Calendar.getInstance();
        calendarFromDb.setTimeInMillis(dbDate * 1000L);

        Calendar currentTime = Calendar.getInstance();
        return (calendarFromDb.get(Calendar.HOUR_OF_DAY) == currentTime.get(Calendar.HOUR_OF_DAY) &&
                calendarFromDb.get(Calendar.DAY_OF_MONTH) == currentTime.get(Calendar.DAY_OF_MONTH));
    }
}
