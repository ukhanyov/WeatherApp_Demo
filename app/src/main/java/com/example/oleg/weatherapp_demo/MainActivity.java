package com.example.oleg.weatherapp_demo;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.data.Weather;
import com.example.oleg.weatherapp_demo.data.WeatherViewModel;
import com.example.oleg.weatherapp_demo.databinding.ActivityMainBinding;
import com.example.oleg.weatherapp_demo.geo.GeocodeAddressIntentService;
import com.example.oleg.weatherapp_demo.network.GetDataService;
import com.example.oleg.weatherapp_demo.network.PJWeekly;
import com.example.oleg.weatherapp_demo.network.PJCurrent;
import com.example.oleg.weatherapp_demo.network.PJWeeklySpecificDay;
import com.example.oleg.weatherapp_demo.network.RetrofitWeatherInstance;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        // item click shit
        WeatherAdapter.WeatherAdapterOnClickHandler {

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

    // Permissions
    private static final int REQUEST_LOCATION = 1;

    // Get city name
    AddressResultReceiver mResultReceiver;

    private Weather mWeatherNow;
    private List<Weather> mWeatherList;

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

        // Get users location
        findUserLocation();

        // Get data from the json
        fetchData();

        // Display weather now
        displayWeatherNow();

        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherViewModel.getAllWeather().observe(this, adapter::setWeather);

        mWeatherList = new ArrayList<>();
        mWeatherViewModel.getAllWeather().observe(this, mWeatherList::addAll);


    }

    private void findUserLocation() {

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            LOCATION = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

            // Get city name
            mResultReceiver = new AddressResultReceiver(null);
            Intent intent = new Intent(this, GeocodeAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, location.getLatitude());
            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, location.getLongitude());
            startService(intent);
        }

    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, String> data = new HashMap<>();
        data.put(QUERY_UTILS, QUERY_UTILS_FORMAT);
        data.put(QUERY_EXCLUDE, QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY);
        Call<PJWeekly> parsedJSON = service.getAllWeather(ACCESS_KEY, LOCATION, data);

        parsedJSON.enqueue(new Callback<PJWeekly>() {
            @Override
            public void onResponse(@NonNull Call<PJWeekly> call, @NonNull Response<PJWeekly> response) {
                PJWeekly pj = response.body();
                for (PJWeeklySpecificDay item : Objects.requireNonNull(pj).getPJWeeklyArray().getData()) {
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
            public void onFailure(@NonNull Call<PJWeekly> call, @NonNull Throwable t) {
                Log.d("Error: ", t.getMessage());
                Toast.makeText(MainActivity.this, "Oh no... Error fetching all data!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void displayWeatherNow() {
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, String> data = new HashMap<>();
        data.put(QUERY_UTILS, QUERY_UTILS_FORMAT);
        data.put(QUERY_EXCLUDE, QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER);
        Call<PJCurrent> parsedJSON = service.getCurrentWeather(ACCESS_KEY, LOCATION, data);

        parsedJSON.enqueue(new Callback<PJCurrent>() {
            @Override
            public void onResponse(@NonNull Call<PJCurrent> call, @NonNull Response<PJCurrent> response) {
                PJCurrent pj = response.body();
                if (pj != null) {
                    mBinding.ivWeatherNow.setImageResource(WeatherIconInterpreter.interpretIcon(pj.getCurrently().getIcon()));

                    String currentTime = NormalizeDate.getHumanFriendlyDateFromDB(pj.getCurrently().getTime() * 1000L);

                    if(NormalizeDate.checkIfItIsToday(currentTime)){
                        mBinding.tvWeatherNowDate.setText(R.string.weather_now);
                    }

                    mBinding.tvWeatherNowDescription.setText(WeatherIconInterpreter.interpretDescription(pj.getCurrently().getIcon()));
                    mBinding.tvWeatherNowTemp.setText(getString(R.string.weather_now_current_temp, pj.getCurrently().getTemperature().toString(), getString(R.string.degrees_celsius)));
                    mBinding.tvWeatherNowHumidity.setText(getString(R.string.weather_now_humidity_level, pj.getCurrently().getHumidity().toString()));
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<PJCurrent> call, @NonNull Throwable t) {
                Log.d("Error: ", t.getMessage());
                Toast.makeText(MainActivity.this, "Oh no... Error fetching today's data!", Toast.LENGTH_SHORT).show();
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

        if (id == R.id.action_drop_table) {
            mWeatherViewModel.deleteAll();
            mBinding.ivWeatherNow.setImageResource(R.drawable.ic_weather_default);

            mBinding.tvWeatherNowDate.setText(null);
            mBinding.tvWeatherNowDescription.setText(null);
            mBinding.tvWeatherNowTemp.setText(null);
            mBinding.tvWeatherNowHumidity.setText(null);
            mBinding.tvWeatherNowLocation.setText(null);
            return true;
        } else if (id == R.id.action_refresh_table) {
            displayWeatherNow();
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

    public void currentWeatherClick(View view) {
        // Implement single item retrieval from db by date
        if(mWeatherList.isEmpty()){
            return;
        }
        String date = mWeatherList.get(0).getDate();
        if(NormalizeDate.checkIfItIsToday(date)){
            mWeatherViewModel.init(mWeatherList.get(0).getDate());
            mWeatherViewModel.getWeatherNow().observe(this, weather -> mWeatherNow = weather);

        }else{

        }

    }

    // Get city name
    class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(() -> {
                    assert address != null;
                    mBinding.tvWeatherNowLocation.setText(address.getLocality());
                });
            } else {
                Toast.makeText(MainActivity.this, "Error parsing location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

