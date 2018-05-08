package com.example.oleg.weatherapp_demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oleg.weatherapp_demo.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    // Fancy dataBinding
    ActivityDetailsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Fancy dataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

    }

}
