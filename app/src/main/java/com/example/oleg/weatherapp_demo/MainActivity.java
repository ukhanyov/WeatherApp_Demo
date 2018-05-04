package com.example.oleg.weatherapp_demo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.data.WeatherContract;
import com.example.oleg.weatherapp_demo.utils.WeatherDateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private static String url = "https://api.darksky.net/forecast/31b4710c5ae2b750bb6227c0517f84de/37.8267,-122.4233";

    TextView mText;
    List<String> mList;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;

    private static final int ID_WEATHER_LOADER = 44;

    private WeatherAdapter mWeatherAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;

    List<ContentValues> weatherValues;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0f);

        mText = findViewById(R.id.tv_main_activity);
        mList = new ArrayList<>();
        weatherValues = new ArrayList<>();

        insertWeatherData(this);
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
                                WeatherDateUtils.normalizeDate(c.getLong("time")));

                        //shit for icon
                        contentValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
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


//                        String summary = c.getString("summary");
//                        double temperatureHigh = c.getDouble("temperatureHigh");
//                        double temperatureLow = c.getDouble("temperatureLow");

                        // adding shit
                        weatherValues.add(contentValues);
//                        mList.add(summary + " - " + String.valueOf(temperatureHigh) +
//                        " - " + String.valueOf(temperatureLow) + "\n");
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
            /**
             * Updating parsed JSON data into ListView
             * */

            mText.setText(mList.toString());

        }
    }

    private void insertWeatherData(Context context){

        new GetWeatherData().execute();

        context.getContentResolver().bulkInsert(
                WeatherContract.WeatherEntry.CONTENT_URI,
                weatherValues.toArray(new ContentValues[weatherValues.size()]));
    }
}

