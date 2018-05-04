package com.example.oleg.weatherapp_demo.utils;

import android.content.Context;

import com.example.oleg.weatherapp_demo.R;

public class WeatherTempUtils {

    private static double celsiusToFahrenheit(double temperatureInCelsius) {
        double temperatureInFahrenheit = (temperatureInCelsius * 1.8) + 32;
        return temperatureInFahrenheit;
    }


    public static String formatTemperature(Context context, double temperature) {
//        if (!SunshinePreferences.isMetric(context)) {
//            temperature = celsiusToFahrenheit(temperature);
//        }

        int temperatureFormatResourceId = R.string.format_temperature;

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }


    public static String formatHighLows(Context context, double high, double low) {
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String formattedHigh = formatTemperature(context, roundedHigh);
        String formattedLow = formatTemperature(context, roundedLow);

        return formattedHigh + " / " + formattedLow;
    }
}