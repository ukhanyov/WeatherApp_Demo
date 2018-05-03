package com.example.oleg.weatherapp_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.oleg.weatherapp_demo.data.WeatherAppContract;

public class MainActivity extends AppCompatActivity {


    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherAppContract.WeatherAppEntry.COLUMN_DATE,
            WeatherAppContract.WeatherAppEntry.COLUMN_MAX_TEMP,
            WeatherAppContract.WeatherAppEntry.COLUMN_MIN_TEMP,
            WeatherAppContract.WeatherAppEntry.COLUMN_WEATHER_ID,
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
