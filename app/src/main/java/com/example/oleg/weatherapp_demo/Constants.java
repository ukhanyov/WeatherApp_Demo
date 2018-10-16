package com.example.oleg.weatherapp_demo;

public class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    private static final String PACKAGE_NAME = "com.example.oleg.weatherapp_demo";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";

    static final String ACCESS_KEY = "31b4710c5ae2b750bb6227c0517f84de";
    static final String QUERY_UTILS = "units";
    static final String QUERY_UTILS_FORMAT = "si";
    static final String QUERY_EXCLUDE = "exclude";
    static final String QUERY_EXCLUDE_ALL_BUT_DATE_ARRAY = "currently,minutely,hourly,flags";
    static final String QUERY_EXCLUDE_ALL_BUT_CURRENT_WEATHER = "minutely,hourly,flags,daily";
    static final String QUERY_EXCLUDE_ALL_BUT_HOURLY_WEATHER = "currently,minutely,flags,daily";

    static final String DB_WEATHER_TYPE_DAILY = "daily";
    static final String DB_WEATHER_TYPE_HOURLY = "hourly";
    static final String DB_WEATHER_TYPE_NOW = "now";

    // Permissions
    static final int REQUEST_LOCATION = 1;

    // Requests
    static final int PLACE_PICKER_REQUEST = 101;

}
