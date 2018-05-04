package com.example.oleg.weatherapp_demo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.data.WeatherContract;
import com.example.oleg.weatherapp_demo.data.WeatherDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private static String url = "https://api.darksky.net/forecast/31b4710c5ae2b750bb6227c0517f84de/37.8267,-122.4233/";

    private SQLiteDatabase mDb;
    private WeatherAdapter mWeatherAdapter;
    private RecyclerView mRecyclerView;

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

        new GetWeatherData().execute();

        mWeatherAdapter = new WeatherAdapter(MainActivity.this, cursor);
        mRecyclerView.setAdapter(mWeatherAdapter);

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

    private boolean removeSampleData(long id){
        return mDb.delete(WeatherContract.WeatherEntry.TABLE_NAME,
                WeatherContract.WeatherEntry._ID + "=" +id, null) > 0;
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

