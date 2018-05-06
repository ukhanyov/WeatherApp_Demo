package com.example.oleg.weatherapp_demo.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.oleg.weatherapp_demo.utils.NormalizeDate;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.oleg.weatherapp_demo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weatherTable";

    public static final class WeatherEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "weatherTable";

        public static final String COLUMN_DATE = "date";

        /* Weather ID as returned by API, used to identify the icon to be used */
        public static final String COLUMN_SUMMARY_ID = "summary_id";

        /* Min and max temperatures in °C for the day (stored as floats in the database) */
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        /* Humidity is stored as a float representing percentage */
        public static final String COLUMN_HUMIDITY = "humidity";

        /* Pressure is stored as a float representing percentage */
        public static final String COLUMN_PRESSURE = "pressure";

        /* Wind speed is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_SPEED = "wind";

        /**
         * Builds a URI that adds the weather date to the end of the forecast content URI path.
         * This is used to query details about a single weather entry by date.*
         */
        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

        /**
         * Returns just the selection part of the weather query from a normalized today value.
         * This is used to get a weather forecast from today's date.
         */
        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = NormalizeDate.getNormalizedUtcNow(System.currentTimeMillis());
            return WeatherContract.WeatherEntry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }

    }
}
