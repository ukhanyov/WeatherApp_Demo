package com.example.oleg.weatherapp_demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.oleg.weatherapp_demo.adapters.DetailsWeatherAdapter;
import com.example.oleg.weatherapp_demo.databinding.ActivityDetailsBinding;
import com.example.oleg.weatherapp_demo.utils.ParcelableWeather;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity{

    // Fancy dataBinding
    ActivityDetailsBinding mBinding;

//    private static int WEATHER_DATE = 0;
//    private static int WEATHER_SUMMARY = 1;
//    private static int WEATHER_TEMPERATURE_MAX = 2;
//    private static int WEATHER_TEMPERATURE_MIN = 3;
//    private static int WEATHER_HUMIDITY = 4;
//    private static int WEATHER_PRESSURE = 5;
//    private static int WEATHER_WIND_SPEED = 6;
//
//    String[] mWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Fancy dataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        // Toolbar
        Toolbar toolbar = mBinding.toolbarLayoutDetails.toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Setup recyclerView
        RecyclerView recyclerView = mBinding.rvDetails;
        final DetailsWeatherAdapter adapter = new DetailsWeatherAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ArrayList<ParcelableWeather> list = getIntent().getParcelableArrayListExtra("weather_list");
        String key = getIntent().getStringExtra("weather_key");
//        mWeatherData = getIntent().getStringArrayExtra(Intent.EXTRA_TEXT);
//        populateViews(mWeatherData);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    private void populateViews(String[] weatherData) {
//        mBinding.ivDetailsWeatherIcon.setImageResource(
//                WeatherIconInterpreter.interpretIcon(weatherData[WEATHER_SUMMARY]));
//
//        mBinding.tvDetails1.setText(getString(R.string.weather_details_date,
//                NormalizeDate.getHumanFriendlyDateFromDB(Long.parseLong(weatherData[WEATHER_DATE]))));
//
//        mBinding.tvDetails2.setText(getString(R.string.weather_details_summary,
//                WeatherIconInterpreter.interpretDescription(weatherData[WEATHER_SUMMARY])));
//
//        mBinding.tvDetails3.setText(getString(R.string.weather_details_temperature_max,
//                weatherData[WEATHER_TEMPERATURE_MAX],
//                getString(R.string.degrees_celsius)));
//
//        mBinding.tvDetails4.setText(getString(R.string.weather_details_temperature_min,
//                weatherData[WEATHER_TEMPERATURE_MIN],
//                getString(R.string.degrees_celsius)));
//
//        mBinding.tvDetails5.setText(getString(R.string.weather_details_humidity,
//                weatherData[WEATHER_HUMIDITY]));
//
//        mBinding.tvDetails6.setText(getString(R.string.weather_details_pressure,
//                weatherData[WEATHER_PRESSURE]));
//
//        mBinding.tvDetails7.setText(getString(R.string.weather_details_wind_speed,
//                weatherData[WEATHER_WIND_SPEED]));
//    }

}
