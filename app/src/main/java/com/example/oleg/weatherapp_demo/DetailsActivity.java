//package com.example.oleg.weatherapp_demo;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.oleg.weatherapp_demo.data.WeatherContract;
//import com.example.oleg.weatherapp_demo.utils.NormalizeDate;
//
//public class DetailsActivity extends AppCompatActivity implements
//        LoaderManager.LoaderCallbacks<Cursor>{
//
//    /*
//     * The columns of data that we are interested in displaying within our DetailActivity's
//     * weather display.
//     */
//    public static final String[] WEATHER_DETAIL_PROJECTION = {
//            WeatherContract.WeatherEntry.COLUMN_DATE,
//            WeatherContract.WeatherEntry.COLUMN_SUMMARY_ID,
//            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
//            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
//            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
//            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
//            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED
//    };
//
//    /*
//     * This ID will be used to identify the Loader responsible for loading the weather details
//     * for a particular day.
//     */
//    public static final int ID_DETAIL_LOADER = 353;
//
//    private ImageView mImageView;
//    private Uri mUri;
//    private TextView mDateTextView;
//    private TextView mSummaryTextView;
//    private TextView mHighTempTextView;
//    private TextView mLowTempTextView;
//    private TextView mHumidityTextView;
//    private TextView mPressureTextView;
//    private TextView mWindSpeedTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_details);
//
//        mUri = getIntent().getData();
//        if (mUri == null) {
//            throw new NullPointerException("URI for DetailActivity cannot be null");
//        }
//
//        mImageView = findViewById(R.id.iv_deteils_wether_icon);
//        mDateTextView = findViewById(R.id.tv_details_1);
//        mSummaryTextView = findViewById(R.id.tv_details_2);
//        mHighTempTextView = findViewById(R.id.tv_details_3);
//        mLowTempTextView = findViewById(R.id.tv_details_4);
//        mHumidityTextView = findViewById(R.id.tv_details_5);
//        mPressureTextView = findViewById(R.id.tv_details_6);
//        mWindSpeedTextView = findViewById(R.id.tv_details_7);
//
//
//        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
//    }
//
//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
////        switch (id){
////            case ID_DETAIL_LOADER :
////
////                return new CursorLoader(this,
////                        mUri,
////                        null,
////                        null,
////                        null,
////                        null);
////
////            default:
////                throw new RuntimeException("Loader Not Implemented: " + id);
////        }
//
//        return new CursorLoader(this,
//                mUri,
//                WEATHER_DETAIL_PROJECTION,
//                null,
//                null,
//                null);
//
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//
//        boolean cursorHasValidData = false;
//        if (data != null && data.moveToFirst()) {
//            /* We have valid data, continue on to bind the data to the UI */
//            cursorHasValidData = true;
//        }
//
//        if (!cursorHasValidData) {
//            /* No data to display, simply return and do nothing */
//            return;
//        }
//
//        /****************
//         * Weather Icon *
//         ****************/
//        /* Read weather condition ID from the cursor (ID provided by Open Weather Map) */
//        //int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
//        mImageView.setImageResource(R.drawable.ic_brightness_medium_black_48dp);
//
//
//        /****************
//         * Weather Date *
//         ****************/
//
//        long dateFromDb = data.getLong(MainActivity.INDEX_WEATHER_COLUMN_DATE);
//        String dateText = NormalizeDate.getHumanFriendlyDate(dateFromDb);
//
//        mDateTextView.setText(dateText);
//
//        /***********************
//         * Weather Description *
//         ***********************/
//        String summary = data.getString(MainActivity.INDEX_WEATHER_COLUMN_SUMMARY_ID);
//        mSummaryTextView.setText(summary);
//
//        /**************************
//         * High (max) temperature *
//         **************************/
//        double highTemperature = data.getDouble(MainActivity.INDEX_WEATHER_COLUMN_MAX_TEMP);
//        mHighTempTextView.setText(String.valueOf(highTemperature));
//
//        /*************************
//         * Low (min) temperature *
//         *************************/
//        double lowTemperature = data.getDouble(MainActivity.INDEX_WEATHER_COLUMN_MIN_TEMP);
//        mLowTempTextView.setText(String.valueOf(lowTemperature));
//
//        /************
//         * Humidity *
//         ************/
//        String humidity = data.getString(MainActivity.INDEX_WEATHER_COLUMN_HUMIDITY);
//        mHumidityTextView.setText(humidity);
//
//        /************
//         * Pressure *
//         ************/
//        String pressure = data.getString(MainActivity.INDEX_WEATHER_COLUMN_PRESSURE);
//        mPressureTextView.setText(pressure);
//
//        /****************************
//         * Wind speed and direction *
//         ****************************/
//        String windSpeed = data.getString(MainActivity.INDEX_WEATHER_COLUMN_WIND_SPEED);
//        mWindSpeedTextView.setText(windSpeed);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//
//    }
//}
