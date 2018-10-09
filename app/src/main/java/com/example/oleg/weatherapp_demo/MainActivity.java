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
import android.preference.ListPreference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
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
import com.example.oleg.weatherapp_demo.network.PJCurrent;
import com.example.oleg.weatherapp_demo.network.PJWeekly;
import com.example.oleg.weatherapp_demo.network.PJWeeklySpecificDay;
import com.example.oleg.weatherapp_demo.network.RetrofitWeatherInstance;
import com.example.oleg.weatherapp_demo.settings.SettingsActivity;
import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
import com.example.oleg.weatherapp_demo.utils.WeatherIconInterpreter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Arrays;
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

    private WeatherViewModel mWeatherViewModel;

    // Fancy dataBinding
    ActivityMainBinding mBinding;

    // Get city name
    AddressResultReceiver mResultReceiver;

    private List<Weather> mWeatherList;

    // Preferences lists
    private List<String> locationsList;
    private List<String> coordinatesList;

    private String LOCATION = "37.8267,-122.4233";


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


        // Check if Internet is connected


        mWeatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);


        mWeatherViewModel.getAllWeather().observe(this, adapter::setWeather);

        mWeatherList = new ArrayList<>();
        mWeatherViewModel.getAllWeather().observe(this, mWeatherList::addAll);

        // TODO : Implement offline functionality
        // TODO : Refactor MainActivity
        // TODO : Look into returning from empty list preferences yields last selected result
    }

    private void preferencesSetup() {
        preferencesRetrieve();

        String[] locations = {"Kiev", "London"};
        String[] coordinates = {"50.4501,30.5234", "51.5074,0.1278"};

        StringBuilder stringBuilderLocations = new StringBuilder();
        StringBuilder stringBuilderCoordinates = new StringBuilder();

        for (String s : locationsList) {
            stringBuilderLocations.append(s);
            stringBuilderLocations.append(";");
        }

        for (String s : locations) {
            stringBuilderLocations.append(s);
            stringBuilderLocations.append(";");
        }

        for (String s : coordinatesList) {
            stringBuilderCoordinates.append(s);
            stringBuilderCoordinates.append(";");
        }

        for (String s : coordinates) {
            stringBuilderCoordinates.append(s);
            stringBuilderCoordinates.append(";");
        }

        SharedPreferences sharedPreferencesLocations = getSharedPreferences("LOCATIONS_PREF", 0);
        SharedPreferences.Editor editorLocations = sharedPreferencesLocations.edit();
        editorLocations.clear();
        editorLocations.putString("locations", stringBuilderLocations.toString());
        editorLocations.apply();

        SharedPreferences sharedPreferencesCoordinates = getSharedPreferences("COORDINATES_PREF", 0);
        SharedPreferences.Editor editorCoordinates = sharedPreferencesCoordinates.edit();
        editorCoordinates.clear();
        editorCoordinates.putString("coordinates", stringBuilderCoordinates.toString());
        editorCoordinates.apply();
    }

    private void preferencesRetrieve() {
        SharedPreferences sharedPreferencesLocations = getSharedPreferences("LOCATIONS_PREF", 0);
        String locationsString = sharedPreferencesLocations.getString("locations", "");
        locationsList = new ArrayList<>(Arrays.asList(locationsString.split(";")));

        SharedPreferences sharedPreferencesCoordinates = getSharedPreferences("COORDINATES_PREF", 0);
        String coordinatesString = sharedPreferencesCoordinates.getString("coordinates", "");
        coordinatesList = new ArrayList<>(Arrays.asList(coordinatesString.split(";")));
    }

    private void preferenceUpdate() {
        StringBuilder stringBuilderLocations = new StringBuilder();
        StringBuilder stringBuilderCoordinates = new StringBuilder();

        for (String s : locationsList) {
            stringBuilderLocations.append(s);
            stringBuilderLocations.append(";");
        }

        for (String s : coordinatesList) {
            stringBuilderCoordinates.append(s);
            stringBuilderCoordinates.append(";");
        }

        SharedPreferences sharedPreferencesLocations = getSharedPreferences("LOCATIONS_PREF", 0);
        SharedPreferences.Editor editorLocations = sharedPreferencesLocations.edit();
        editorLocations.clear();
        editorLocations.putString("locations", stringBuilderLocations.toString());
        editorLocations.apply();

        SharedPreferences sharedPreferencesCoordinates = getSharedPreferences("COORDINATES_PREF", 0);
        SharedPreferences.Editor editorCoordinates = sharedPreferencesCoordinates.edit();
        editorCoordinates.clear();
        editorCoordinates.putString("coordinates", stringBuilderCoordinates.toString());
        editorCoordinates.apply();
    }

    private void preferenceAddLocationCoordinate(String location, String coordinates) {
        locationsList.add(location);
        coordinatesList.add(coordinates);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (haveLocationEnabled()) {
            // Get users location
            findUserLocation();
        } else {
            askForLocation();
        }

        if (haveNetworkConnection()) {
            // Get data from the json
            fetchData();

            // Display weather now
            displayWeatherNow();

        } else {
            askForWifi();
        }
    }

    private void findUserLocation() {

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION);

        } else {
            assert locationManager != null;

            SharedPreferences useCurrentLocationSwitch = PreferenceManager.getDefaultSharedPreferences(this);
            if (useCurrentLocationSwitch.getBoolean("use_my_current_location", false)) {
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
            } else {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String s = sharedPreferences.getString("location_key", null);
                LOCATION = s;

                if (s != null) {
                    String[] ss = s.split(",");
                    double lat = Double.valueOf(ss[0]);
                    double lon = Double.valueOf(ss[1]);

                    mResultReceiver = new AddressResultReceiver(null);
                    Intent intent = new Intent(this, GeocodeAddressIntentService.class);
                    intent.putExtra(Constants.RECEIVER, mResultReceiver);
                    intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
                    intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, lat);
                    intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, lon);
                    startService(intent);
                } else {
                    Toast.makeText(this, "Choose location to forecast", Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    private void fetchData() {
        if (LOCATION.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);

            GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
            Map<String, String> data = new HashMap<>();
            data.put(Constants.QUERY_UTILS, Constants.QUERY_UTILS_FORMAT);
            data.put(Constants.QUERY_EXCLUDE, Constants.QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY);
            Call<PJWeekly> parsedJSON = service.getAllWeather(Constants.ACCESS_KEY, LOCATION, data);

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
    }

    private void displayWeatherNow() {
        progressBar.setVisibility(View.VISIBLE);

        GetDataService service = RetrofitWeatherInstance.getRetrofitInstance().create(GetDataService.class);
        Map<String, String> data = new HashMap<>();
        data.put(Constants.QUERY_UTILS, Constants.QUERY_UTILS_FORMAT);
        data.put(Constants.QUERY_EXCLUDE, Constants.QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER);
        Call<PJCurrent> parsedJSON = service.getCurrentWeather(Constants.ACCESS_KEY, LOCATION, data);

        parsedJSON.enqueue(new Callback<PJCurrent>() {
            @Override
            public void onResponse(@NonNull Call<PJCurrent> call, @NonNull Response<PJCurrent> response) {
                PJCurrent pj = response.body();
                if (pj != null) {
                    mBinding.ivWeatherNow.setImageResource(WeatherIconInterpreter.interpretIcon(pj.getCurrently().getIcon()));

                    String currentTime = NormalizeDate.getHumanFriendlyDateFromDB(pj.getCurrently().getTime());

                    if (NormalizeDate.checkIfItIsToday(currentTime)) {
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
        //mWeatherViewModel.deleteAll();

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_drop_table:
                cleanViews();
                return true;

            case R.id.action_refresh_table:
                cleanViews();

                displayWeatherNow();
                fetchData();
                return true;

            case R.id.action_settings:
                cleanViews();
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;

            case R.id.action_add_location:
                // Start picking place on the map
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), Constants.PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                return true;

            case R.id.action_add_dummy_location:
                preferencesSetup();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                cleanViews();
                Place place = PlacePicker.getPlace(this, data);

                preferencesRetrieve();
                preferenceAddLocationCoordinate(place.getName().toString(),
                        String.valueOf(place.getLatLng().latitude) + "," +
                                String.valueOf(place.getLatLng().longitude));
                preferenceUpdate();

                LOCATION = String.valueOf(place.getLatLng().latitude) + "," +
                        String.valueOf(place.getLatLng().longitude);

                mResultReceiver = new AddressResultReceiver(null);
                Intent intent = new Intent(this, GeocodeAddressIntentService.class);
                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);
                intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, place.getLatLng().latitude);
                intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, place.getLatLng().longitude);
                startService(intent);

                // Get data from the json
                fetchData();

                // Display weather now
                displayWeatherNow();

                SharedPreferences sharedPreferencesSelectedItem = getSharedPreferences("INDEX_PREF", 0);
                SharedPreferences.Editor editorSelectedItem = sharedPreferencesSelectedItem.edit();
                editorSelectedItem.clear();
                editorSelectedItem.putString("coordination_index", LOCATION);
                editorSelectedItem.apply();
            }
        }
    }

    // Item click, obviously
    @Override
    public void onClick(Weather weather) {
        launchDetailsActivity(weather);
    }

    public void currentWeatherClick(View view) {
        if (mWeatherList.isEmpty()) {
            return;
        }

        for (Weather instance : mWeatherList) {
            if (NormalizeDate.checkIfItIsToday(
                    NormalizeDate.getHumanFriendlyDateFromDB(
                            Long.parseLong(instance.getDate())
                    ))) {
                launchDetailsActivity(instance);
                return;
            }
        }
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
                });
            } else {
                Toast.makeText(MainActivity.this, "Error parsing location", Toast.LENGTH_SHORT).show();
            }
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

    private void askForWifi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", (dialog, id) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Quit", (dialog, id) -> finish());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean haveLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps_enabled && network_enabled;
    }

    private void askForLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS Network not enabled")
                .setCancelable(false)
                .setPositiveButton("Open Location Settings", (dialog, id) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Quit", (dialog, id) -> finish());
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void cleanViews() {
        mWeatherViewModel.deleteAll();
        mBinding.ivWeatherNow.setImageResource(R.drawable.ic_weather_default);

        mBinding.tvWeatherNowDate.setText(null);
        mBinding.tvWeatherNowDescription.setText(null);
        mBinding.tvWeatherNowTemp.setText(null);
        mBinding.tvWeatherNowHumidity.setText(null);
        mBinding.tvWeatherNowLocation.setText(null);
    }

}

