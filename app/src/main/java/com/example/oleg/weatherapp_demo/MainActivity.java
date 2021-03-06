package com.example.oleg.weatherapp_demo;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.adapters.MyLocationAdapter;
import com.example.oleg.weatherapp_demo.adapters.WeatherAdapter;
import com.example.oleg.weatherapp_demo.adapters.WeatherHorizontalAdapter;
import com.example.oleg.weatherapp_demo.data.MyLocationViewModel;
import com.example.oleg.weatherapp_demo.data.WeatherViewModel;
import com.example.oleg.weatherapp_demo.data.entities.MyLocation;
import com.example.oleg.weatherapp_demo.data.entities.Weather;
import com.example.oleg.weatherapp_demo.databinding.ActivityMainBinding;
import com.example.oleg.weatherapp_demo.geo.GeocodeAddressIntentService;
import com.example.oleg.weatherapp_demo.network.pojo.PJCurrent;
import com.example.oleg.weatherapp_demo.network.pojo.PJHourly;
import com.example.oleg.weatherapp_demo.network.pojo.PJHourlyInstance;
import com.example.oleg.weatherapp_demo.network.pojo.PJWeekly;
import com.example.oleg.weatherapp_demo.network.pojo.PJWeeklySpecificDay;
import com.example.oleg.weatherapp_demo.network.retrofit.GetDataService;
import com.example.oleg.weatherapp_demo.network.retrofit.RetrofitWeatherInstance;
import com.example.oleg.weatherapp_demo.utils.BitmapTransforamationHelper;
import com.example.oleg.weatherapp_demo.utils.Constants;
import com.example.oleg.weatherapp_demo.utils.CustomOnSwipeTouchListener;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.ParcelableWeather;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.oleg.weatherapp_demo.utils.Constants.OPACITY_LEVEL;

public class MainActivity extends AppCompatActivity implements
        // item click stuff
        WeatherAdapter.WeatherAdapterOnClickHandler,
        WeatherHorizontalAdapter.WeatherHorizontalAdapterOnClickHandler,
        NavigationView.OnNavigationItemSelectedListener {

    //private static String url = "https://api.darksky.net/forecast/31b4710c5ae2b750bb6227c0517f84de/37.8267,-122.4233?units=si&exclude=currently,minutely,hourly,flags";
    private ProgressBar progressBar;
    private WeatherViewModel mWeatherViewModel;
    private MyLocationViewModel mMyLocationViewModel;

    // Fancy dataBinding
    ActivityMainBinding mBinding;

    // Get city name
    AddressResultReceiver mResultReceiver;

    // Weather instance for weather now
    private Weather mWeatherNow;
    private Weather mWeatherForThisDay;

    // Nav drawer
    DrawerLayout mDrawer;

    // MyLocation instance for location
    private List<MyLocation> mMyLocationsList;
    private List<MyLocation> mMyLocationsListForBackground;

    private String LOCATION_COORDINATES = "37.8267,-122.4233";
    private String LOCATION_NAME = null;

    // For picture
    GeoDataClient mGeoDataClient;

    Bitmap mSavedPicture;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        // Toolbar stuff
        toolbar = mBinding.toolbarLayout.toolbar;
        setSupportActionBar(toolbar);
        mDrawer = mBinding.drawerLayout;

        // Nav view stuff
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = mBinding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerViewVertical = mBinding.layoutContentMain.layoutContentAppBar.rvWeather;
        RecyclerView recyclerViewHorizontal = mBinding.layoutContentMain.layoutContentAppBar.rvWeatherHorizontal;
        RecyclerView recyclerViewNavView = mBinding.rvNavList;
        progressBar = mBinding.pbLoadingIndicator;

        // Second "this" is for item click
        final WeatherAdapter adapterVertical = new WeatherAdapter(this, this);
        recyclerViewVertical.setHasFixedSize(true);
        recyclerViewVertical.setAdapter(adapterVertical);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this));

        // Setup horizontal recyclerView
        final WeatherHorizontalAdapter horizontalAdapter =
                new WeatherHorizontalAdapter(this, this);
        recyclerViewHorizontal.setAdapter(horizontalAdapter);
        LinearLayoutManager layoutManagerHorizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal.setHasFixedSize(true);
        recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        recyclerViewHorizontal.setItemViewCacheSize(20);

        // LiveData/viewModels
        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        mWeatherViewModel.getWeatherDailyByCoordinatesAndType().observe(this, list -> {
            adapterVertical.setWeather(list);
            horizontalAdapter.setWeatherList(list);
            mWeatherForThisDay = Objects.requireNonNull(list).get(0);
            mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowDate.setText(NormalizeDate.getHumanFriendlyDateFromDB(
                    Long.parseLong(mWeatherForThisDay.getDate())));

            mBinding.layoutContentMain.layoutContentAppBar.tvTempHigh.setText(getString(R.string.weather_now_current_temp,
                    String.valueOf(Math.round(Double.parseDouble(mWeatherForThisDay.getTemperatureMax()))), getString(R.string.degrees_celsius)));

            mBinding.layoutContentMain.layoutContentAppBar.tvTempLow.setText(getString(R.string.weather_now_current_temp,
                    String.valueOf(Math.round(Double.parseDouble(mWeatherForThisDay.getTemperatureMin()))), getString(R.string.degrees_celsius)));

            if (!isConnected()) {
                setWeatherNowViews(mWeatherForThisDay);
            }

        });

        mWeatherViewModel.getWeatherHourlyByCoordinatesAndType().observe(this, list -> {
            horizontalAdapter.setWeather(list);
        });

        // Recycler view for nav drawer
        final MyLocationAdapter myLocationAdapter = new MyLocationAdapter(this);
        recyclerViewNavView.setAdapter(myLocationAdapter);
        recyclerViewNavView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNavView.setHasFixedSize(true);

        // Location viewModel stuff
        mMyLocationsListForBackground = new ArrayList<>();
        mMyLocationViewModel = ViewModelProviders.of(this).get(MyLocationViewModel.class);
        mMyLocationViewModel.getAllLocations().observe(this, list -> {
            myLocationAdapter.setMyLocations(list);
            mMyLocationsListForBackground.addAll(Objects.requireNonNull(list));
            loadMyLocations(LOCATION_COORDINATES);
        });

        // Swipes on screen
        swipesOnScreen(myLocationAdapter);

    }

    private void swipesOnScreen(MyLocationAdapter myLocationAdapter) {

        mBinding.layoutContentMain.layoutContentAppBar.clWeatherNow.setOnTouchListener(new CustomOnSwipeTouchListener(MainActivity.this) {

            public void onSwipeRight() {

                mMyLocationsList = new ArrayList<>();
                mMyLocationsList = myLocationAdapter.getAll();

                for (MyLocation location : mMyLocationsList) {

                    if (Objects.equals(location.getLocationCoordinates(), LOCATION_COORDINATES)) {

                        int position = mMyLocationsList.indexOf(location);
                        if (position < mMyLocationsList.size() - 1) {

                            LOCATION_COORDINATES = mMyLocationsList.get(position + 1).getLocationCoordinates();
                            toolbar.setTitle(mMyLocationsList.get(position + 1).getLocationName());

                            loadMyLocations(LOCATION_COORDINATES);

                            fetchAllTheData(LOCATION_COORDINATES);

                            SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                            SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                            prefEditor.clear();
                            prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
                            prefEditor.putString("saved_location_name", mMyLocationsList.get(position + 1).getLocationName());
                            prefEditor.apply();

                        }

                        return;
                    }
                }
            }

            public void onSwipeLeft() {

                mMyLocationsList = new ArrayList<>();
                mMyLocationsList = myLocationAdapter.getAll();

                for (MyLocation location : mMyLocationsList) {

                    if (Objects.equals(location.getLocationCoordinates(), LOCATION_COORDINATES)) {
                        int position = mMyLocationsList.indexOf(location);
                        if (position >= 0) {

                            LOCATION_COORDINATES = mMyLocationsList.get(position - 1).getLocationCoordinates();
                            toolbar.setTitle(mMyLocationsList.get(position - 1).getLocationName());

                            loadMyLocations(LOCATION_COORDINATES);

                            fetchAllTheData(LOCATION_COORDINATES);

                            SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                            SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                            prefEditor.clear();
                            prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
                            prefEditor.putString("saved_location_name", mMyLocationsList.get(position - 1).getLocationName());
                            prefEditor.apply();

                        }

                        return;
                    }
                }
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.layoutContentMain.layoutContentAppBar.tvOffline.setVisibility(View.GONE);

        if (haveLocationEnabled()) {
            // Get users location

            // Simple location saved, change if have time
            SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
            String checkLocationCoordinates = preferencesLocation.getString("saved_location_coordinates", null);
            String checkLocationName = preferencesLocation.getString("saved_location_name", null);


            //findUserLocation();
            if (checkLocationName != null) {
                mMyLocationViewModel.queryForSpecifiedLocation(checkLocationName);
            }

            if (checkLocationCoordinates == null) {
                findUserLocation();
            } else {
                LOCATION_COORDINATES = checkLocationCoordinates;
                toolbar.setTitle(checkLocationName);
            }

        } else {
            mBinding.layoutContentMain.layoutContentAppBar.tvOffline.setVisibility(View.VISIBLE);
            mBinding.layoutContentMain.layoutContentAppBar.tvOffline.setText(R.string.offline_turn_on_location);

            // TODO: Add callback when internet is enabled
        }

        fetchAllTheData(LOCATION_COORDINATES);
    }

    // Nav view
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = mBinding.drawerLayout;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Nav view
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = mBinding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add_location:
                if (isConnected()) {
                    callToGetPicture();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST_FOR_LOCATION) {
            if (resultCode == RESULT_OK) {
                cleanViews();
                Place place = PlacePicker.getPlace(this, data);

                LOCATION_COORDINATES = String.valueOf(place.getLatLng().latitude) + "," +
                        String.valueOf(place.getLatLng().longitude);

                mResultReceiver = new AddressResultReceiver(null);
                Intent intent = new Intent(this, GeocodeAddressIntentService.class);
                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
                intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, place.getLatLng().latitude);
                intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, place.getLatLng().longitude);
                startService(intent);

                fetchAllTheData(LOCATION_COORDINATES);

                SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                prefEditor.clear();
                prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
                prefEditor.apply();

                getPhotoFromPlacePicker(place.getId());
            }
        }
    }

    @Override
    public void onClick(List<Weather> weather, String key) {
        launchDetailsActivity(weather, key);
    }

    private void fetchAllTheData(String coordinates) {

        if (isConnected()) {

            // Daily data
            fetchDailyData();

            // Hourly data
            fetchHourlyData();

            // Now data
            fetchNowData();

            mBinding.layoutContentMain.layoutContentAppBar.tvOffline.setVisibility(View.GONE);

        } else {
            // Query for data (offline)
            mWeatherViewModel.queryWeatherHourlyByCoordinatesAndType(coordinates, Constants.DB_WEATHER_TYPE_HOURLY);
            mWeatherViewModel.queryWeatherDailyByCoordinatesAndType(coordinates, Constants.DB_WEATHER_TYPE_DAILY);


            mBinding.layoutContentMain.layoutContentAppBar.tvOffline.setVisibility(View.VISIBLE);
            mBinding.layoutContentMain.layoutContentAppBar.tvOffline.setText(R.string.offline_turn_on_internet);

            setWeatherNowViews(mWeatherForThisDay);
        }
    }

    private void findUserLocation() {

        // Acquire a reference to the system MyLocation Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION);

        } else {
            assert locationManager != null;

            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            LOCATION_COORDINATES = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

            SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = preferencesLocation.edit();
            prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
            prefEditor.apply();

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

    private void fetchDailyData() {
        if (LOCATION_COORDINATES.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
            Map<String, String> data = new HashMap<>();
            data.put(Constants.QUERY_UTILS, Constants.QUERY_UTILS_FORMAT);
            data.put(Constants.QUERY_EXCLUDE, Constants.QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY);
            Call<PJWeekly> parsedJSON = service.getAllWeather(Constants.ACCESS_KEY, LOCATION_COORDINATES, data);

            mWeatherViewModel.deleteSpecificWeatherByTypeAndCoordinates(Constants.DB_WEATHER_TYPE_DAILY, LOCATION_COORDINATES);

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
                                item.getWindSpeed().toString(),
                                LOCATION_COORDINATES,
                                Constants.DB_WEATHER_TYPE_DAILY,
                                item.getPrecipIntensity().toString(),
                                item.getSunriseTime().toString(),
                                item.getSunsetTime().toString(),
                                pj.getTimezone());

                        mWeatherViewModel.insert(weather);
                    }

                    mWeatherViewModel.queryWeatherDailyByCoordinatesAndType(LOCATION_COORDINATES, Constants.DB_WEATHER_TYPE_DAILY);

                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<PJWeekly> call, @NonNull Throwable t) {
                    Log.d("Error: ", t.getMessage());
                    Toast.makeText(MainActivity.this, "Oh no... Error fetching Daily data!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void fetchHourlyData() {
        if (LOCATION_COORDINATES.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
            Map<String, String> data = new HashMap<>();
            data.put(Constants.QUERY_UTILS, Constants.QUERY_UTILS_FORMAT);
            data.put(Constants.QUERY_EXCLUDE, Constants.QUERY_EXCLUDE_ALL_BUT_HOURLY_WEATHER);
            Call<PJHourly> parsedJSON = service.getHourlyWeather(Constants.ACCESS_KEY, LOCATION_COORDINATES, data);

            mWeatherViewModel.deleteSpecificWeatherByTypeAndCoordinates(Constants.DB_WEATHER_TYPE_HOURLY, LOCATION_COORDINATES);

            parsedJSON.enqueue(new Callback<PJHourly>() {
                @Override
                public void onResponse(@NonNull Call<PJHourly> call, @NonNull Response<PJHourly> response) {
                    PJHourly pj = response.body();

                    for (PJHourlyInstance item : Objects.requireNonNull(pj).getHourlyArray().getData()) {
                        Weather weather = new Weather(
                                item.getTime().toString(),
                                item.getIcon(),
                                item.getTemperatureMax().toString(),
                                item.getTemperatureMin().toString(),
                                item.getHumidity().toString(),
                                item.getPressure().toString(),
                                item.getWindSpeed().toString(),
                                LOCATION_COORDINATES,
                                Constants.DB_WEATHER_TYPE_HOURLY,
                                item.getPrecipIntensity().toString(),
                                null,
                                null,
                                pj.getTimezone());

                        mWeatherViewModel.insert(weather);
                    }

                    mWeatherViewModel.queryWeatherHourlyByCoordinatesAndType(LOCATION_COORDINATES, Constants.DB_WEATHER_TYPE_HOURLY);

                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<PJHourly> call, @NonNull Throwable t) {
                    Log.d("Error: ", t.getMessage());
                    Toast.makeText(MainActivity.this, "Oh no... Error fetching hourly data!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void fetchNowData() {
        if (LOCATION_COORDINATES.length() != 0) {

            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
            Map<String, String> data = new HashMap<>();
            data.put(Constants.QUERY_UTILS, Constants.QUERY_UTILS_FORMAT);
            data.put(Constants.QUERY_EXCLUDE, Constants.QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER);
            Call<PJCurrent> parsedJSON = service.getCurrentWeather(Constants.ACCESS_KEY, LOCATION_COORDINATES, data);

            parsedJSON.enqueue(new Callback<PJCurrent>() {
                @Override
                public void onResponse(@NonNull Call<PJCurrent> call, @NonNull Response<PJCurrent> response) {
                    PJCurrent pj = response.body();
                    if (pj != null) {

                        mWeatherNow = new Weather(
                                pj.getCurrently().getTime().toString(),
                                pj.getCurrently().getIcon(),
                                pj.getCurrently().getTemperatureMax().toString(),
                                pj.getCurrently().getTemperatureMin().toString(),
                                pj.getCurrently().getHumidity().toString(),
                                pj.getCurrently().getPressure().toString(),
                                pj.getCurrently().getWindSpeed().toString(),
                                LOCATION_COORDINATES,
                                Constants.DB_WEATHER_TYPE_NOW,
                                pj.getCurrently().getPrecipIntensity().toString(),
                                null,
                                null,
                                pj.getTimezone());

                        setWeatherNowViews(mWeatherNow);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<PJCurrent> call, @NonNull Throwable t) {
                    Log.d("Error: ", t.getMessage());
                    Toast.makeText(MainActivity.this, "Oh no... Error fetching 'Now' data!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void setWeatherNowViews(Weather weather) {

        if (weather != null) {
            mBinding.layoutContentMain.layoutContentAppBar.ivWeatherNow.setImageResource(
                    WeatherIconInterpreter.interpretIcon(weather.getSummary()));

            mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowTemp.setText(getString(R.string.weather_now_current_temp,
                    String.valueOf(Math.round(Double.parseDouble(weather.getTemperatureMax()))), getString(R.string.degrees_celsius)));

            mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowHumidity.setText(getString(R.string.weather_now_current_humidity,
                    weather.getHumidity(), getString(R.string.percentage)));

            mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowWind.setText(getString(R.string.weather_now_current_wind,
                    weather.getWindSpeed(), getString(R.string.kmp)));

            mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowPrecipitation.setText(getString(R.string.weather_now_current_precipitation,
                    weather.getPrecipProbability(), getString(R.string.millimeters)));
        }
    }

    private void launchDetailsActivity(List<Weather> weather, String key) {
        if (weather != null) {
            Intent startDetailsActivityIntent = new Intent(MainActivity.this, DetailsActivity.class);

            List<ParcelableWeather> parcelableWeathers = new ArrayList<>();
            for (Weather instance : weather) {
                parcelableWeathers.add(new ParcelableWeather(
                        instance.getId(),
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


            if (mMyLocationsListForBackground.size() > 0) {
                for (MyLocation iterator : mMyLocationsListForBackground) {
                    if (iterator.getLocationCoordinates().equals(LOCATION_COORDINATES)) {
                        byte[] encodeByte = Base64.decode(iterator.getImageString(), Base64.DEFAULT);
                        startDetailsActivityIntent.putExtra("bite", encodeByte);
                        break;
                    }
                }
            }

            startDetailsActivityIntent
                    .putExtra("toolbar_title", toolbar.getTitle().toString())
                    .putExtra("weather_key", key)
                    .putExtra("weather_list", (ArrayList<ParcelableWeather>) parcelableWeathers);

            startActivity(startDetailsActivityIntent);
        }
    }

    public void textOnLocationNavigationDrawerClicked(View view) {
        View child = mBinding.rvNavList.findContainingItemView(view);

        if (child != null) {
            mDrawer.closeDrawers();

            MyLocationAdapter adapter = (MyLocationAdapter) mBinding.rvNavList.getAdapter();
            MyLocation location = Objects.requireNonNull(adapter).getItem(mBinding.rvNavList.getChildAdapterPosition(child));
            LOCATION_COORDINATES = location.getLocationCoordinates();

            SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = preferencesLocation.edit();
            prefEditor.clear();
            prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
            prefEditor.putString("saved_location_name", location.getLocationName());
            prefEditor.apply();

            toolbar.setTitle(location.getLocationName());

            fetchAllTheData(LOCATION_COORDINATES);

            loadMyLocations(LOCATION_COORDINATES);
        }
    }

    public void buttonOnLocationNavigationDrawerClicked(View view) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    View child = mBinding.rvNavList.findContainingItemView(view);
                    int position = mBinding.rvNavList.getChildAdapterPosition(Objects.requireNonNull(child));
                    MyLocationAdapter adapter = (MyLocationAdapter) mBinding.rvNavList.getAdapter();
                    Objects.requireNonNull(adapter).notifyItemRemoved(position);

                    MyLocation location = adapter.getItem(position);
                    mMyLocationViewModel.deleteSpecificLocation(location.getLocationName());

                    // Check if location is in preferences
                    SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                    if (location.getLocationName().equals(preferencesLocation.getString("saved_location_name", null)) ||
                            location.getLocationCoordinates().equals(preferencesLocation.getString("saved_location_coordinates", null))) {
                        SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                        prefEditor.clear();
                        prefEditor.apply();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
                    toolbar.setTitle(address.getLocality());

                    LOCATION_NAME = address.getLocality();

                    SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                    prefEditor.putString("saved_location_name", address.getLocality());
                    prefEditor.apply();

                });
            } else {
                Toast.makeText(MainActivity.this, "Error parsing location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveCurrentLocation(String address) {
        if (LOCATION_COORDINATES.length() > 0) {

            String[] coordinates = LOCATION_COORDINATES.split(",");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mSavedPicture.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);

            mMyLocationViewModel.insert(new MyLocation(
                    address,
                    LOCATION_COORDINATES,
                    Double.valueOf(coordinates[0]),
                    Double.valueOf(coordinates[1]),
                    temp
            ));
        }
    }

    private boolean haveLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled;
        boolean network_enabled;

        gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps_enabled && network_enabled;
    }

    private void cleanViews() {
        mWeatherViewModel.deleteAll();
        mBinding.layoutContentMain.layoutContentAppBar.ivWeatherNow.setImageResource(R.drawable.ic_weather_default);

        mBinding.layoutContentMain.layoutContentAppBar.tvPrecipitationLabel.setText(null);
        mBinding.layoutContentMain.layoutContentAppBar.tvTempHigh.setText(null);
        mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowTemp.setText(null);
        mBinding.layoutContentMain.layoutContentAppBar.tvTempLow.setText(null);
        mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowHumidity.setText(null);
        mBinding.layoutContentMain.layoutContentAppBar.tvWeatherNowDate.setText(null);
    }

    public boolean isConnected() {

        // The -i 5 is a timeout option
        final String command = "ping -i 5 -c 1 google.com";
        try {
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getPhotoFromPlacePicker(String placeId) {

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(task -> {
            try{
                PlacePhotoMetadataResponse photos = task.getResult();
                PlacePhotoMetadataBuffer photoMetadataBuffer = Objects.requireNonNull(photos).getPhotoMetadata();
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);

                photoResponse.addOnCompleteListener(task1 -> {
                    PlacePhotoResponse photo = task1.getResult();

                    Bitmap originalImage = Objects.requireNonNull(photo).getBitmap();
                    int width = mBinding.layoutContentMain.layoutContentAppBar.clWeatherNow.getWidth();
                    int height = mBinding.layoutContentMain.layoutContentAppBar.clWeatherNow.getHeight();

                    mSavedPicture = originalImage;

                    Drawable drawable = new BitmapDrawable(getResources(),
                            BitmapTransforamationHelper.transformWithSavedProportions(originalImage, width, height));

                    mBinding.clActivityMain.setBackground(drawable);
                    mBinding.clActivityMain.getBackground().setAlpha(OPACITY_LEVEL); // Setting opacity (scale is 0 - 255)

                    saveCurrentLocation(LOCATION_NAME);
                });
            }catch (IllegalStateException e) {
                Toast.makeText(this, "No location picture to save", Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.class.getSimpleName(), e.toString());
            }
        });
    }

    private void callToGetPicture() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(MainActivity.this), Constants.PLACE_PICKER_REQUEST_FOR_LOCATION);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(MainActivity.class.getSimpleName(), e.toString());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    private void loadMyLocations(String coordinates) {

        if (mMyLocationsListForBackground.size() > 0) {
            for (MyLocation iterator : mMyLocationsListForBackground) {
                if (iterator.getLocationCoordinates().equals(coordinates)) {
                    setBackgroundPicture(iterator);
                    return;
                }
            }
        }
    }

    private void setBackgroundPicture(MyLocation myLocation) {

        final Bitmap bitmap;

        if (myLocation.getImageString() != null) {
            byte[] encodeByte = Base64.decode(myLocation.getImageString(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            // Transform original bitmap to resized bitmap
            final View view = mBinding.clActivityMain;
            view.post(() -> {
                Drawable drawable = new BitmapDrawable(getResources(),
                        BitmapTransforamationHelper.transformWithSavedProportions(bitmap, view.getWidth(), view.getHeight()));
                mBinding.clActivityMain.setBackground(drawable);
                mBinding.clActivityMain.getBackground().setAlpha(OPACITY_LEVEL); // Setting opacity (scale is 0 - 255)
            });
        }
    }
}