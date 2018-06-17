package com.example.oleg.weatherapp_demo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.databinding.ActivityMainBinding;
import com.example.oleg.weatherapp_demo.data.Weather;
import com.example.oleg.weatherapp_demo.data.WeatherViewModel;
import com.example.oleg.weatherapp_demo.network.GetDataService;
import com.example.oleg.weatherapp_demo.network.ParsedJSON;
import com.example.oleg.weatherapp_demo.network.ParsedJSONCurrentWeather;
import com.example.oleg.weatherapp_demo.network.ParsedSpecificDate;
import com.example.oleg.weatherapp_demo.network.RetrofitWeatherInstance;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        // item click shit
        WeatherAdapter.WeatherAdapterOnClickHandler{

    //private static String url = "https://api.darksky.net/forecast/31b4710c5ae2b750bb6227c0517f84de/37.8267,-122.4233?units=si&exclude=currently,minutely,hourly,flags";
    private ProgressBar progressBar;

    private static final String ACCESS_KEY = "31b4710c5ae2b750bb6227c0517f84de";
    private static String LOCATION = "37.8267,-122.4233";

    private static final String QUERY_UTILS = "units";
    private static final String QUERY_UTILS_FORMAT = "si";

    private static final String QUERY_EXCLUDE = "exclude";
    private static final String QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY = "currently,minutely,hourly,flags";
    private static final String QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER = "minutely,hourly,flags,daily";

    private WeatherViewModel mWeatherViewModel;

    // Fancy dataBinding
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fancy dataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        RecyclerView recyclerView = mBinding.rvWeather;
        progressBar = mBinding.pbLoadingIndicator;

        // Second this is for item click
        final WeatherAdapter adapter = new WeatherAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        // Get data from the json
        fetchData();

        // Display weather now
        displayWeatherNow();

        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherViewModel.getAllWeather().observe(this, adapter::setWeather);

    }

    private void displayWeatherNow() {
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, String> data = new HashMap<>();
        data.put(QUERY_UTILS, QUERY_UTILS_FORMAT);
        data.put(QUERY_EXCLUDE, QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER);
        Call<ParsedJSONCurrentWeather> parsedJSON = service.getCurrentWeather(ACCESS_KEY, LOCATION, data);

        parsedJSON.enqueue(new Callback<ParsedJSONCurrentWeather>() {
            @Override
            public void onResponse(@NonNull Call<ParsedJSONCurrentWeather> call, @NonNull Response<ParsedJSONCurrentWeather> response) {
                ParsedJSONCurrentWeather pj = response.body();
                if(pj != null) {
                    mBinding.ivWeatherNow.setImageResource(WeatherIconInterpreter.interpretIcon(pj.getCurrently().getIcon()));
                    mBinding.tvWeatherNowDate.setText(NormalizeDate.getHumanFriendlyDate((pj.getCurrently().getTime())));
                    mBinding.tvWeatherNowDescription.setText(WeatherIconInterpreter.interpretDescription(pj.getCurrently().getIcon()));
                    mBinding.tvWeatherNowTemp.setText(getString(R.string.weather_now_current_temp) + pj.getCurrently().getTemperature().toString() + "\u00b0");
                    mBinding.tvWeatherNowHumidity.setText(getString(R.string.weather_now_humidity_level) + pj.getCurrently().getHumidity().toString());
                    mBinding.tvWeatherNowLocation.setText(pj.getTimezone());
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<ParsedJSONCurrentWeather> call, @NonNull Throwable t) {
                Log.d("Error: ", t.getMessage());
                Toast.makeText(MainActivity.this,  "Oh no... Something went wrong when fetching data!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, String> data = new HashMap<>();
        data.put(QUERY_UTILS, QUERY_UTILS_FORMAT);
        data.put(QUERY_EXCLUDE, QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY);
        Call<ParsedJSON> parsedJSON = service.getAllWeather(ACCESS_KEY, LOCATION, data);

        parsedJSON.enqueue(new Callback<ParsedJSON>() {
            @Override
            public void onResponse(@NonNull Call<ParsedJSON> call, @NonNull Response<ParsedJSON> response) {
                ParsedJSON pj = response.body();
                for(ParsedSpecificDate item : Objects.requireNonNull(pj).getParsedArrayWithDates().getData()){
                    Weather weather = new Weather(
                            item.getTime().toString(),
                            item.getIcon(),
                            item.getTemperatureMax().toString(),
                            item.getTemperatureMin().toString(),
                            item.getHumidity().toString(),
                            item.getPressure().toString(),
                            item.getWindSpeed().toString());

                    mWeatherViewModel.insert(weather);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<ParsedJSON> call, @NonNull Throwable t) {
                Log.d("Error: ", t.getMessage());
                Toast.makeText(MainActivity.this,  "Oh no... Something went wrong when fetching data!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mWeatherViewModel.deleteAll();

        int id = item.getItemId();

        if(id == R.id.action_drop_table){
            mWeatherViewModel.deleteAll();
            return true;
        }else if(id == R.id.action_refresh_table){
            fetchData();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Item click shit, obviously
    @Override
    public void onClick(Weather weather) {
        Intent startDetailsActivity = new Intent(MainActivity.this, DetailsActivity.class);
        String[] data = {
                weather.getDate(),
                weather.getSummary(),
                weather.getTemperatureMax(),
                weather.getTemperatureMin(),
                weather.getHumidity(),
                weather.getPressure(),
                weather.getWindSpeed()
        };
        startDetailsActivity.putExtra(Intent.EXTRA_TEXT, data);
        startActivity(startDetailsActivity);
    }
}

