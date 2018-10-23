package com.example.oleg.weatherapp_demo;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.oleg.weatherapp_demo.adapters.DetailsWeatherAdapter;
import com.example.oleg.weatherapp_demo.data.entities.Weather;
import com.example.oleg.weatherapp_demo.databinding.ActivityDetailsBinding;
import com.example.oleg.weatherapp_demo.utils.BitmapTransforamationHelper;
import com.example.oleg.weatherapp_demo.utils.Constants;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.ParcelableWeather;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity{

    // Fancy dataBinding
    ActivityDetailsBinding mBinding;

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

        // Get Weathers
        ArrayList<ParcelableWeather> list = getIntent().getParcelableArrayListExtra("weather_list");
        String key = getIntent().getStringExtra("weather_key");
        byte[] byteArray = getIntent().getByteArrayExtra("bite");
        String title = getIntent().getStringExtra("toolbar_title");
        Objects.requireNonNull(toolbar).setTitle(title);

        List<Weather> weatherList = new ArrayList<>();
        for (ParcelableWeather instance : list){
            weatherList.add(new Weather(
                    instance.getDate(),
                    instance.getSummary(),
                    instance.getTemperatureMax(),
                    instance.getTemperatureMin(),
                    instance.getHumidity(),
                    instance.getPressure(),
                    instance.getWindSpeed(),
                    instance.getCoordinates(),
                    instance.getTypeOfDay(),
                    instance.getPrecipProbability(),
                    instance.getSunriseTime(),
                    instance.getSunsetTime(),
                    instance.getTimezone())
            );
        }

        if(key.equals(Constants.KEY_VERTICAL)){
            RecyclerView recyclerView = mBinding.rvDetails;
            final DetailsWeatherAdapter adapter = new DetailsWeatherAdapter(this, key);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            adapter.setWeather(weatherList);

            mBinding.ivDetailsWeatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(weatherList.get(0).getSummary()));
            mBinding.tvDetailsDate.setText(NormalizeDate.getHumanFriendlyDateFromDB(Long.parseLong(weatherList.get(0).getDate())));
            mBinding.tvDetailsForecastType.setText(getString(R.string.weather_for_this_week));
            mBinding.tvDetailsTemperature.setText(getString(R.string.weather_now_current_temp,
                    String.valueOf(Math.round(Double.parseDouble(weatherList.get(0).getTemperatureMax()))),
                    getString(R.string.degrees_celsius)));
        } else if(key.equals(Constants.KEY_HORIZONTAL)){
            RecyclerView recyclerView = mBinding.rvDetails;
            final DetailsWeatherAdapter adapter = new DetailsWeatherAdapter(this, key);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            adapter.setWeather(weatherList);

            mBinding.ivDetailsWeatherIcon.setImageResource(WeatherIconInterpreter.interpretIcon(weatherList.get(0).getSummary()));
            mBinding.tvDetailsDate.setText(NormalizeDate.getHumanFriendlyDateFromDB(Long.parseLong(weatherList.get(0).getDate())));
            mBinding.tvDetailsForecastType.setText(getString(R.string.weather_for_next_days));
            mBinding.tvDetailsTemperature.setText(getString(R.string.weather_now_current_temp,
                    String.valueOf(Math.round(Double.parseDouble(weatherList.get(0).getTemperatureMax()))),
                    getString(R.string.degrees_celsius)));
        }

        if(byteArray != null){
            // Transform original bitmap to resized bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            View view = mBinding.clActivityDetails;
            if(view != null){
                view.post(() -> {
                    Drawable drawable = new BitmapDrawable(getResources(),
                            BitmapTransforamationHelper.transformWithSavedProportions(bitmap, view.getWidth(), view.getHeight()));
                    mBinding.clActivityDetails.setBackground(drawable);
                    mBinding.clActivityDetails.getBackground().setAlpha(51); // Setting opacity (scale is 0 - 255)
                });
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
