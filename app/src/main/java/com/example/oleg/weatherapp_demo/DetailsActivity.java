package com.example.oleg.weatherapp_demo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oleg.weatherapp_demo.databinding.ActivityDetailsBinding;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

public class DetailsActivity extends AppCompatActivity {

    // Fancy dataBinding
    ActivityDetailsBinding mBinding;

    private static int WEATHER_DATE = 0;
    private static int WEATHER_SUMMARY = 1;
    private static int WEATHER_TEMPERATURE_MAX = 2;
    private static int WEATHER_TEMPERATURE_MIN = 3;
    private static int WEATHER_HUMIDITY = 4;
    private static int WEATHER_PRESSURE = 5;
    private static int WEATHER_WIND_SPEED = 6;

    String[] mWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Fancy dataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        mWeatherData = getIntent().getStringArrayExtra(Intent.EXTRA_TEXT);

        populateViews(mWeatherData);
    }

    private void populateViews(String[] weatherData) {
        mBinding.ivDetailsWeatherIcon.setImageResource(
                WeatherIconInterpreter.interpretIcon(weatherData[WEATHER_SUMMARY]));

        mBinding.tvDetails1.setText("Date: " + NormalizeDate.getHumanFriendlyDate(
                Long.parseLong(weatherData[WEATHER_DATE])));
        mBinding.tvDetails2.setText("Summary: " + WeatherIconInterpreter.interpretDescription(
                weatherData[WEATHER_SUMMARY]));
        mBinding.tvDetails3.setText("Temperature max: " + weatherData[WEATHER_TEMPERATURE_MAX] + "\u00b0");
        mBinding.tvDetails4.setText("Temperature min: " +weatherData[WEATHER_TEMPERATURE_MIN] + "\u00b0");
        mBinding.tvDetails5.setText("Humidity: " + weatherData[WEATHER_HUMIDITY]);
        mBinding.tvDetails6.setText("Pressure: " + weatherData[WEATHER_PRESSURE]);
        mBinding.tvDetails7.setText("Wind speed: " + weatherData[WEATHER_WIND_SPEED]);
    }

}
