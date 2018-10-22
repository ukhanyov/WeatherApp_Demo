package com.example.oleg.weatherapp_demo.utils;

import com.example.oleg.weatherapp_demo.R;

public class WeatherIconInterpreter {

    public static int interpretIcon(String input) {
        switch (input) {
            case "clear-day":
                return R.drawable.ic_weather_clear_day;

            case "clear-night":
                return R.drawable.ic_weather_clear_night;

            case "rain":
                return R.drawable.ic_weather_rain;

            case "snow":
                return R.drawable.ic_weather_snow;

            case "sleet":
                return R.drawable.ic_weather_sleet;

            case "wind":
                return R.drawable.ic_weather_wind;

            case "fog":
                return R.drawable.ic_weather_fog;

            case "cloudy":
                return R.drawable.ic_weather_cloudy;

            case "partly-cloudy-day":
                return R.drawable.ic_weather_partly_cloudy_day;

            case "partly-cloudy-night":
                return R.drawable.ic_weather_partly_cloudy_night;

            case "hail":
                return R.drawable.ic_weather_hail;

            case "thunderstorm":
                return R.drawable.ic_weather_thunderstorm;

            case "tornado":
                return R.drawable.ic_weather_tornado;

            case "sunriseTime":
                return R.drawable.ic_weather_sunrise;

            case "sunsetTime":
                return R.drawable.ic_weather_sunset;

            default:
                return R.drawable.ic_weather_default;
        }
    }

    public static String interpretDescription(String input) {
        switch (input) {
            case "clear-day":
                return "Clear day";

            case "clear-night":
                return "Clear night";

            case "rain":
                return "Rain";

            case "snow":
                return "Snow";

            case "sleet":
                return "Sleet (rain with snow)";

            case "wind":
                return "Strong wind";

            case "fog":
                return "Fog";

            case "cloudy":
                return "Cloudy";

            case "partly-cloudy-day":
                return "Somewhat cloudy day";

            case "partly-cloudy-night":
                return "Somewhat cloudy night";

            case "hail":
                return "Hail";

            case "thunderstorm":
                return "Thunderstorm";

            case "tornado":
                return "Tornado";

            default:
                return "Something nasty occurred";

        }
    }
}
