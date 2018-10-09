package com.example.oleg.weatherapp_demo.settings;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.oleg.weatherapp_demo.R;
import com.example.oleg.weatherapp_demo.databinding.ActivitySettingsDeleteLocationsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsDeleteLocations extends AppCompatActivity {

    ActivitySettingsDeleteLocationsBinding mBinding;

    private List<String> locationsList;
    private List<String> coordinatesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_delete_locations);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_delete_locations);

        RecyclerView recyclerView = mBinding.rvSettingsDeleteLocation;
        final SettingsDeleteLocationsAdapter adapter = new SettingsDeleteLocationsAdapter(this);

        preferencesRetrieve();
        adapter.setTitles(locationsList);
        adapter.setTexts(coordinatesList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void preferencesRetrieve() {
        SharedPreferences sharedPreferencesLocations = getSharedPreferences("LOCATIONS_PREF", 0);
        String locationsString = sharedPreferencesLocations.getString("locations", "");
        locationsList = new ArrayList<>(Arrays.asList(locationsString.split(";")));

        SharedPreferences sharedPreferencesCoordinates = getSharedPreferences("COORDINATES_PREF", 0);
        String coordinatesString = sharedPreferencesCoordinates.getString("coordinates", "");
        coordinatesList = new ArrayList<>(Arrays.asList(coordinatesString.split(";")));
    }
}
