package com.example.oleg.weatherapp_demo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.data.WeatherContract;
import com.example.oleg.weatherapp_demo.data.WeatherDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements WeatherAdapter.WeatherAdapterOnClickHandler,
                    LoaderManager.LoaderCallbacks<Cursor>{

    private ProgressDialog pDialog;

    private static String url = "https://api.darksky.net/forecast/31b4710c5ae2b750bb6227c0517f84de/37.8267,-122.4233?units=si";

    private SQLiteDatabase mDb;
    private WeatherAdapter mWeatherAdapter;
    private RecyclerView mRecyclerView;

    public static final String[] MAIN_WEATHER_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_SUMMARY_ID,
    };

    public static final int INDEX_WEATHER_TABLE_NAME = 0;
    public static final int INDEX_WEATHER_COLUMN_DATE = 1;
    public static final int INDEX_WEATHER_COLUMN_SUMMARY_ID = 2;
    public static final int INDEX_WEATHER_COLUMN_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_COLUMN_MAX_TEMP = 4;
    public static final int INDEX_WEATHER_COLUMN_HUMIDITY = 5;
    public static final int INDEX_WEATHER_COLUMN_PRESSURE = 6;
    public static final int INDEX_WEATHER_COLUMN_WIND_SPEED = 7;

    private static final int ID_WEATHER_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherDbHelper dbHelper = new WeatherDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllSamples();

        mRecyclerView = findViewById(R.id.rv_weather);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        //new GetWeatherData().execute();

        mWeatherAdapter = new WeatherAdapter(MainActivity.this, cursor, this);
        mRecyclerView.setAdapter(mWeatherAdapter);

        getSupportLoaderManager().initLoader(ID_WEATHER_LOADER, null, this);
    }

    private Cursor getAllSamples() {
        // Inside, call query on mDb passing in the table name and projection String [] order by _ID
        return mDb.query(
                WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WeatherContract.WeatherEntry._ID
        );
    }

    @Override
    public void onClick(long date) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setData(WeatherContract.WeatherEntry.buildWeatherUriWithDate(date));
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {

            case ID_WEATHER_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_WEATHER_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mWeatherAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mWeatherAdapter.swapCursor(null);
    }

    private class GetWeatherData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject daily = jsonObj.getJSONObject("daily");
                    JSONArray data = daily.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {

                        ContentValues contentValues = new ContentValues();

                        JSONObject c = data.getJSONObject(i);

                        //date
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_DATE,
                                c.getLong("time"));

                        //shit for icon
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_SUMMARY_ID,
                                c.getString("icon"));

                        //temp
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                                c.getDouble("temperatureLow"));
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                                c.getDouble("temperatureHigh"));

                        //humidity
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                                c.getDouble("humidity"));

                        //pressure
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                                c.getDouble("pressure"));

                        //wind speed
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
                                c.getDouble("windSpeed"));


                        // adding shit
                        mDb.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, contentValues);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}

