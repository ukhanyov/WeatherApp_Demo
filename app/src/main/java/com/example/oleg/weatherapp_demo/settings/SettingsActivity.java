package com.example.oleg.weatherapp_demo.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {

        if (preference.getKey().equals("location_key")) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            // TODO : look into editing title
        }

        return true;
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || ConnectionPreferenceFragment.class.getName().equals(fragmentName)
                || LocationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ConnectionPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_connections);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LocationPreferenceFragment extends PreferenceFragment {

        private static Context mContext;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_location);
            setHasOptionsMenu(true);

            mContext = this.getActivity();

            expandListOfLocations((ListPreference) findPreference("location_key"));

            bindPreferenceSummaryToValue(findPreference("location_key"));
        }



        @Override
        public void onStart() {
            super.onStart();

            expandListOfLocations((ListPreference) findPreference("location_key"));

            Toast.makeText(mContext, "Hey Hey HEy", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private static void expandListOfLocations(ListPreference preference) {
            SharedPreferences sharedPreferencesLocations = mContext.getSharedPreferences("LOCATIONS_PREF", 0);
            String locationsString = sharedPreferencesLocations.getString("locations", "");
            List<String> locationsList = new ArrayList<>(Arrays.asList(locationsString.split(";")));
            locationsList.remove("");
            String[] locationsArray = new String[locationsList.size()];
            for(int i = 0; i < locationsList.size(); i++){
                locationsArray[i] = locationsList.get(i);
            }


            SharedPreferences sharedPreferencesCoordinates = mContext.getSharedPreferences("COORDINATES_PREF", 0);
            String coordinatesString = sharedPreferencesCoordinates.getString("coordinates", "");
            List<String> coordinatesList = new ArrayList<>(Arrays.asList(coordinatesString.split(";")));
            coordinatesList.remove("");
            String[] coordinatesArray = new String[coordinatesList.size()];
            for(int i = 0; i < locationsList.size(); i++){
                coordinatesArray[i] = coordinatesList.get(i);
            }

            if(coordinatesList.size() == locationsList.size()){
                preference.setEntries(locationsArray);
                preference.setEntryValues(coordinatesArray);
            }



            if (coordinatesArray.length == 0 || locationsArray.length == 0) {
                preference.setSummary("");
            }

            SharedPreferences sharedPreferencesSelectedItem = mContext.getSharedPreferences("INDEX_PREF", 0);
            String s = sharedPreferencesSelectedItem.getString("coordination_index", "");

            if (s.length() != 0) {
                int counter = 0;
                for (String iterator : coordinatesArray) {
                    if (iterator.equals(s)) {
                        preference.setValueIndex(counter);
                    }
                    counter++;
                }
                SharedPreferences.Editor editorSelectedItem = sharedPreferencesSelectedItem.edit();
                editorSelectedItem.clear();
                editorSelectedItem.apply();
            }

        }

    }
}
