package com.example.oleg.weatherapp_demo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oleg.weatherapp_demo.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    // Fancy dataBinding
    ActivityDetailsBinding mBinding;

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
        mBinding.tvDetails1.setText(weatherData[0]);
        mBinding.tvDetails2.setText(weatherData[1]);
        mBinding.tvDetails3.setText(weatherData[2]);
        mBinding.tvDetails4.setText(weatherData[3]);
        mBinding.tvDetails5.setText(weatherData[4]);
        mBinding.tvDetails6.setText(weatherData[5]);
        mBinding.tvDetails7.setText(weatherData[6]);
    }

}
