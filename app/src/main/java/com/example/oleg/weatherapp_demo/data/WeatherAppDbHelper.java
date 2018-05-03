package com.example.oleg.weatherapp_demo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherAppDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";

    private static final int DATABASE_VERSION = 1;

    public WeatherAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + WeatherAppContract.WeatherAppEntry.TABLE_NAME + " (" +

                        /*
                         * WeatherEntry did not explicitly declare a column called "_ID". However,
                         * WeatherEntry implements the interface, "BaseColumns", which does have a field
                         * named "_ID". We use that here to designate our table's primary key.
                         */
                        WeatherAppContract.WeatherAppEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        WeatherAppContract.WeatherAppEntry.COLUMN_DATE       + " INTEGER NOT NULL, "                 +

                        WeatherAppContract.WeatherAppEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL,"                  +

                        WeatherAppContract.WeatherAppEntry.COLUMN_MIN_TEMP   + " REAL NOT NULL, "                    +
                        WeatherAppContract.WeatherAppEntry.COLUMN_MAX_TEMP   + " REAL NOT NULL, "                    +

                        WeatherAppContract.WeatherAppEntry.COLUMN_HUMIDITY   + " REAL NOT NULL, "                    +

                        WeatherAppContract.WeatherAppEntry.COLUMN_DEGREES    + " REAL NOT NULL, "                    +

                        " UNIQUE (" + WeatherAppContract.WeatherAppEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherAppContract.WeatherAppEntry.TABLE_NAME);
        onCreate(db);
    }
}
