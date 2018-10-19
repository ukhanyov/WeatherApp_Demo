package com.example.oleg.weatherapp_demo;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.example.oleg.weatherapp_demo.utils.Constants;
import com.example.oleg.weatherapp_demo.utils.CustomOnSwipeTouchListener;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        // item click stuff
        WeatherAdapter.WeatherAdapterOnClickHandler,
        WeatherHorizontalAdapter.WeatherHorizontalAdapterOnClickHandler {

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

    // Nav drawer
    DrawerLayout mDrawer;

    // MyLocation instance for location
    private List<MyLocation> mMyLocationsList;

    private String LOCATION_COORDINATES = "37.8267,-122.4233";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fancy dataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        RecyclerView recyclerViewVertical = mBinding.rvWeather;
        RecyclerView recyclerViewHorizontal = mBinding.rvWeatherHoryzontal;
        RecyclerView recyclerViewNavView = mBinding.rvNavList;
        progressBar = mBinding.pbLoadingIndicator;

        // Second "this" is for item click
        final WeatherAdapter adapter = new WeatherAdapter(this, this);
        recyclerViewVertical.setAdapter(adapter);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVertical.setHasFixedSize(true);

        // Setup horizontal recyclerView
        final WeatherHorizontalAdapter horizontalAdapter =
                new WeatherHorizontalAdapter(this, this);
        recyclerViewHorizontal.setAdapter(horizontalAdapter);
        LinearLayoutManager layoutManagerHorizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        recyclerViewHorizontal.setHasFixedSize(true);

        // LiveData/viewModels
        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherViewModel.getWeatherDailyByCoordinatesAndType().observe(this, adapter::setWeather);
        mWeatherViewModel.getWeatherHourlyByCoordinatesAndType().observe(this, horizontalAdapter::setWeather);

        // Recycler view for nav drawer
        final MyLocationAdapter myLocationAdapter = new MyLocationAdapter(this);
        recyclerViewNavView.setAdapter(myLocationAdapter);
        recyclerViewNavView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNavView.setHasFixedSize(true);

        // Nav view stuff
        mDrawer = mBinding.navDrawer;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                null,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        toggle.syncState();

        recyclerViewNavView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawer.closeDrawers();

                    MyLocation myLocation = myLocationAdapter.getItem(recyclerView.getChildAdapterPosition(child));
                    LOCATION_COORDINATES = myLocation.getLocationCoordinates();

                    SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                    prefEditor.clear();
                    prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
                    prefEditor.putString("saved_location_name", myLocation.getLocationName());
                    prefEditor.apply();

                    fetchDailyData();
                    fetchHourlyData();
                    fetchNowData();
                    mBinding.tvWeatherNowLocation.setText(myLocation.getLocationName());

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        // Swipe to delete from locations list
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return true;// true if moved, false otherwise
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                myLocationAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                // this is wanky, think around later
                MyLocation myLocation = myLocationAdapter.getItem(viewHolder.getLayoutPosition());
                mMyLocationViewModel.deleteSpecificLocation(myLocation.getLocationName());

                // Check if location is in preferences
                SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                if(myLocation.getLocationName().equals(preferencesLocation.getString("saved_location_name", null)) ||
                        myLocation.getLocationCoordinates().equals(preferencesLocation.getString("saved_location_coordinates", null))){
                    SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                    prefEditor.clear();
                    prefEditor.apply();
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewNavView);

        // Location viewModel stuff
        mMyLocationViewModel = ViewModelProviders.of(this).get(MyLocationViewModel.class);
        mMyLocationViewModel.getAllLocations().observe(this, myLocationAdapter::setMyLocations);

        // Swipes on screen
        mBinding.clWeatherNow.setOnTouchListener(new CustomOnSwipeTouchListener(MainActivity.this) {

            public void onSwipeRight() {

                mMyLocationsList = new ArrayList<>();
                mMyLocationsList = myLocationAdapter.getAll();

                for(MyLocation location : mMyLocationsList){

                    if(Objects.equals(location.getLocationCoordinates(), LOCATION_COORDINATES)){

                        int position = mMyLocationsList.indexOf(location);
                        if(position < mMyLocationsList.size() - 1){

                            LOCATION_COORDINATES = mMyLocationsList.get(position + 1).getLocationCoordinates();
                            mBinding.tvWeatherNowLocation.setText(mMyLocationsList.get(position + 1).getLocationName());

                            fetchNowData();
                            fetchHourlyData();
                            fetchDailyData();

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

                for(MyLocation location : mMyLocationsList){

                    if(Objects.equals(location.getLocationCoordinates(), LOCATION_COORDINATES)){
                        int position = mMyLocationsList.indexOf(location);
                        if(position >= 0){

                            LOCATION_COORDINATES = mMyLocationsList.get(position - 1).getLocationCoordinates();
                            mBinding.tvWeatherNowLocation.setText(mMyLocationsList.get(position - 1).getLocationName());

                            fetchNowData();
                            fetchHourlyData();
                            fetchDailyData();

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
            public void onSimpleClick(){
                if(mWeatherNow != null) launchDetailsActivity(mWeatherNow);
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.tvOffline.setVisibility(View.GONE);

        if (haveLocationEnabled()) {
            // Get users location

            // Simple location saved, change if have time
            SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
            String checkLocationCoordinates = preferencesLocation.getString("saved_location_coordinates", null);
            String checkLocationName = preferencesLocation.getString("saved_location_name", null);

            if(checkLocationName != null){
                mMyLocationViewModel.queryForSpecifiedLocation(checkLocationName);
            }

            if(checkLocationCoordinates == null){
                findUserLocation();
            }else {
                LOCATION_COORDINATES = checkLocationCoordinates;
                mBinding.tvWeatherNowLocation.setText(checkLocationName);
            }


        } else {
            mBinding.tvOffline.setVisibility(View.VISIBLE);
            mBinding.tvOffline.setText(R.string.offline_turn_on_location);

            // TODO: Add offline mode
            // TODO: Proper internet check
            // TODO: Add backgroundImage (maybe from placePicker)

        }

        try {
            if (haveNetworkConnection() && isConnected()) {
                // Daily data
                fetchDailyData();

                // Hourly data
                fetchHourlyData();

                // Now data
                fetchNowData();

            } else {
                mBinding.tvOffline.setVisibility(View.VISIBLE);
                mBinding.tvOffline.setText(R.string.offline_turn_on_internet);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
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
                                Constants.DB_WEATHER_TYPE_DAILY);

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
                                Constants.DB_WEATHER_TYPE_HOURLY);

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
                                Constants.DB_WEATHER_TYPE_NOW);

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
            mBinding.ivWeatherNow.setImageResource(
                    WeatherIconInterpreter.interpretIcon(weather.getSummary()));

            mBinding.tvWeatherNowDate.setText(
                    NormalizeDate.getHumanFriendlyDateFromDB(Long.valueOf(weather.getDate())));

            mBinding.tvWeatherNowDescription.setText(
                    WeatherIconInterpreter.interpretDescription(weather.getSummary()));

            mBinding.tvWeatherNowTemp.setText(getString(R.string.weather_now_current_temp,
                    weather.getTemperatureMax(), getString(R.string.degrees_celsius)));

            mBinding.tvWeatherNowTmpApparent.setText(getString(R.string.how_it_feels,
                    weather.getTemperatureMin(), getString(R.string.degrees_celsius)));

            mBinding.tvWeatherNowHumidity.setText(getString(R.string.weather_now_humidity_level,
                    weather.getHumidity()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_drop_table:
                cleanViews();
                return true;

            case R.id.action_refresh_table:
                cleanViews();
                fetchNowData();
                fetchDailyData();
                fetchHourlyData();
                return true;

            case R.id.action_add_location:
                // Start picking place on the map
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), Constants.PLACE_PICKER_REQUEST_FOR_LOCATION);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
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

                fetchNowData();
                fetchHourlyData();
                fetchDailyData();

                SharedPreferences preferencesLocation = getSharedPreferences("display_location_settings", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = preferencesLocation.edit();
                prefEditor.clear();
                prefEditor.putString("saved_location_coordinates", LOCATION_COORDINATES);
                prefEditor.apply();
            }
        }
    }

    // Item click, obviously
    @Override
    public void onClick(Weather weather) {
        launchDetailsActivity(weather);
    }

    public void currentWeatherClick(View view) {

        if (mWeatherNow != null)
            launchDetailsActivity(mWeatherNow);

    }

    private void launchDetailsActivity(Weather weather) {
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
                    saveCurrentLocation();


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

    private void saveCurrentLocation() {
        if (LOCATION_COORDINATES.length() > 0) {

            String[] coordinates = LOCATION_COORDINATES.split(",");

            mMyLocationViewModel.insert(new MyLocation(
                    mBinding.tvWeatherNowLocation.getText().toString(),
                    LOCATION_COORDINATES,
                    Double.valueOf(coordinates[0]),
                    Double.valueOf(coordinates[1])
            ));
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = Objects.requireNonNull(cm).getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private boolean haveLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps_enabled && network_enabled;
    }

    private void cleanViews() {
        mWeatherViewModel.deleteAll();
        mBinding.ivWeatherNow.setImageResource(R.drawable.ic_weather_default);

        mBinding.tvWeatherNowDate.setText(null);
        mBinding.tvWeatherNowDescription.setText(null);
        mBinding.tvWeatherNowTemp.setText(null);
        mBinding.tvWeatherNowTmpApparent.setText(null);
        mBinding.tvWeatherNowHumidity.setText(null);
        mBinding.tvWeatherNowLocation.setText(null);
    }

    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}