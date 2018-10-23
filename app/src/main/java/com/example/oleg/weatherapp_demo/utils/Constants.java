package com.example.oleg.weatherapp_demo.utils;

public class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    public static final String PACKAGE_NAME = "com.example.oleg.weatherapp_demo";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";

    public static final String ACCESS_KEY = "735a826704c4ad82ea2b4ab0fc4b8562";
    public static final String QUERY_UTILS = "units";
    public static final String QUERY_UTILS_FORMAT = "si";
    public static final String QUERY_EXCLUDE = "exclude";
    public static final String QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY = "currently,minutely,hourly,flags";
    public static final String QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER = "minutely,hourly,flags,daily";
    public static final String QUERY_EXCLUDE_ALL_BUT_HOURLY_WEATHER = "currently,minutely,flags,daily";

    public static final String DB_WEATHER_TYPE_DAILY = "daily";
    public static final String DB_WEATHER_TYPE_HOURLY = "hourly";
    public static final String DB_WEATHER_TYPE_NOW = "now";

    // Permissions
    public static final int REQUEST_LOCATION = 1;

    // Requests
    public static final int PLACE_PICKER_REQUEST_FOR_LOCATION = 101;

    public static final String KEY_VERTICAL = "key_vertical";
    public static final String KEY_HORIZONTAL = "key_horizontal";

    public static final int OPACITY_LEVEL = 51;

}
