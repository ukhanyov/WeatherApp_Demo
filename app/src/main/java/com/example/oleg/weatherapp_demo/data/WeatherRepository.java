package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

// Timeline 4

public class WeatherRepository {

    private WeatherDao mWeatherDao;
    private LiveData<List<Weather>> mAllWeather;
    private static LiveData<Weather> mWeatherInstance;

    WeatherRepository(Application application) {
        WeatherRoomDatabase db = WeatherRoomDatabase.getDatabase(application);
        mWeatherDao = db.weatherDao();
        mAllWeather = mWeatherDao.getAllWeather();
    }

    LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

    LiveData<Weather> getSingleWeather(String weatherDate) {
        new getSingleWeatherAsyncTask(mWeatherDao).execute(weatherDate);
        return mWeatherInstance;
    }

    private static class getSingleWeatherAsyncTask extends AsyncTask<String, Void, LiveData<Weather>> {

        private WeatherDao mAsyncDao;

        getSingleWeatherAsyncTask(WeatherDao dao) {
            this.mAsyncDao = dao;
        }

        @Override
        protected LiveData<Weather> doInBackground(String... strings) {
            return mAsyncDao.getSingleWeather(strings[0]);
        }

        @Override
        protected void onPostExecute(LiveData<Weather> weatherLiveData) {
            mWeatherInstance = weatherLiveData;
        }
    }

    void insert(Weather weather) {
        new insertAsyncTask(mWeatherDao).execute(weather);
    }

    private static class insertAsyncTask extends AsyncTask<Weather, Void, Void> {

        private WeatherDao mAsyncDao;

        insertAsyncTask(WeatherDao dao) {
            this.mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(Weather... weathers) {
            mAsyncDao.insert(weathers[0]);
            return null;
        }
    }

    void deleteAll() {
        new deleteAllAsyncTask(mWeatherDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Weather, Void, Void> {

        private WeatherDao mAsyncDao;

        deleteAllAsyncTask(WeatherDao dao) {
            this.mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(Weather... weathers) {
            mAsyncDao.deleteAll();
            return null;
        }
    }
}
