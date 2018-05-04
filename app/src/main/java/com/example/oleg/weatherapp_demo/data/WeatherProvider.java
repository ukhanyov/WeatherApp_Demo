package com.example.oleg.weatherapp_demo.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.oleg.weatherapp_demo.utils.WeatherDateUtils;

import java.util.Objects;

public class WeatherProvider extends ContentProvider {

    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WeatherDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;

        /* This URI is content://com.example.oleg.weatherapp_demo/weather/ */
        matcher.addURI(authority, WeatherContract.PATH_WEATHER, CODE_WEATHER);

        /*
         * This URI would look something like content://com.example.oleg.weatherapp_demo/weather/1472214172
         * The "/#" signifies to the UriMatcher that if PATH_WEATHER is followed by ANY number,
         * that it should return the CODE_WEATHER_WITH_DATE code
         */
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_WITH_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_WEATHER:
                db.beginTransaction();
                int rowsInserted = 0;

                try {

                    for (ContentValues value : values) {
                        long weatherDate =
                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
                        if (!WeatherDateUtils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }

                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_WEATHER_WITH_DATE: {

                String normalizedUtcDateString = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{normalizedUtcDateString};

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_WEATHER: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_WEATHER:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Meh.");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new RuntimeException("Meh");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Meh");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}