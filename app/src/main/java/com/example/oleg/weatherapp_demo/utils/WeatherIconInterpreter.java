package com.example.oleg.weatherapp_demo.utils;

public class WeatherIconInterpreter {

    public static void interpreteIcon(String input) {
        switch (input) {
            case "clear-day":

                break;
            case "clear-night":

                break;
            case "rain":

                break;
            case "snow":

                break;
            case "sleet":

                break;
            case "wind":

                break;
            case "fog":

                break;
            case "cloudy":

                break;
            case "partly-cloudy-day":

                break;
            case "partly-cloudy-night":

                break;
            case "hail":

                break;
            case "thunderstorm":

                break;
            case "tornado":

                break;
            default:
                break;
        }
    }

    public static String interpreteDescription(String input) {
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
