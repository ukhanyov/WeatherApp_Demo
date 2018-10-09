package com.example.oleg.weatherapp_demo.settings;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

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

        // Swipe to delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                Toast.makeText(SettingsDeleteLocations.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                preferencesRemove(position);
                adapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void preferencesRetrieve() {
        SharedPreferences sharedPreferencesLocations = getSharedPreferences("LOCATIONS_PREF", 0);
        String locationsString = sharedPreferencesLocations.getString("locations", "");
        locationsList = new ArrayList<>(Arrays.asList(locationsString.split(";")));

        SharedPreferences sharedPreferencesCoordinates = getSharedPreferences("COORDINATES_PREF", 0);
        String coordinatesString = sharedPreferencesCoordinates.getString("coordinates", "");
        coordinatesList = new ArrayList<>(Arrays.asList(coordinatesString.split(";")));
    }

    private void preferencesRemove(int position){
        locationsList.remove(position);
        coordinatesList.remove(position);
        preferenceUpdate();
    }

    private void preferenceUpdate(){
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
}
