package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.oleg.weatherapp_demo.Constants;

import java.util.List;

// Timeline 4

public class WeatherRepository implements AsyncResultWeather {

    private WeatherDao mWeatherDao;
    private LiveData<List<Weather>> mAllWeather;
    private LiveData<List<Weather>> mAllDailyWeather;
    private LiveData<List<Weather>> mAllHourlyWeather;
    private MutableLiveData<List<Weather>> searchResults = new MutableLiveData<>();

    WeatherRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mWeatherDao = db.weatherDao();
        mAllWeather = mWeatherDao.getAllWeather();
        mAllDailyWeather = mWeatherDao.getAllWeatherSpecifiedByDayType(Constants.DB_WEATHER_TYPE_DAILY);
        mAllHourlyWeather = mWeatherDao.getAllWeatherSpecifiedByDayType(Constants.DB_WEATHER_TYPE_HOURLY);
    }

    @Override
    public void asyncFinished(List<Weather> result) {
        searchResults.setValue(result);
    }

    LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

    LiveData<List<Weather>> getAllHourlyWeather() {
        return mAllHourlyWeather;
    }

    LiveData<List<Weather>> getAllDailyWeather() {
        return mAllDailyWeather;
    }

    MutableLiveData<List<Weather>> getSearchResults() {
        return searchResults;
    }

    // Insert stuff
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


    // Delete stuff
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

    void deleteWeatherByCoordinates(String deleteKey) {
        new deleteWeatherByCoordinates(mWeatherDao).execute(deleteKey);
    }

    private static class deleteWeatherByCoordinates extends AsyncTask<String, Void, Void> {

        private WeatherDao mAsyncDao;

        deleteWeatherByCoordinates(WeatherDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncDao.deleteWeatherByCoordinates(strings[0]);
            return null;
        }
    }

    void deleteSpecificWeatherByType(String deleteKey){
        new deleteSpecificWeatherByType(mWeatherDao).execute(deleteKey);
    }

    private static class deleteSpecificWeatherByType extends AsyncTask<String, Void, Void>{

        private WeatherDao mAsyncDao;

        deleteSpecificWeatherByType(WeatherDao dao){
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncDao.deleteSpecificfWeatherByType(strings[0]);
            return null;
        }
    }

    // Query specific weather stuff
    void queryByName(String nameQueryKey) {
        queryByNameAsyncTask task = new queryByNameAsyncTask(mWeatherDao);
        task.delegate = this;
        task.execute(nameQueryKey);

    }

    private static class queryByNameAsyncTask extends AsyncTask<String, Void, List<Weather>> {

        private WeatherDao mAsyncDao;
        private WeatherRepository delegate = null;

        queryByNameAsyncTask(WeatherDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected List<Weather> doInBackground(String... strings) {
            return mAsyncDao.getSpecificWeatherEntry(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            delegate.asyncFinished(weathers);
        }
    }

    void queryByCoordinatesAndType(String coordinates, String type) {
        getWeatherByCoordinatesAndTypeAsyncTask task = new getWeatherByCoordinatesAndTypeAsyncTask(mWeatherDao);
        task.delegate = this;
        task.execute(coordinates, type);
    }

    private static class getWeatherByCoordinatesAndTypeAsyncTask extends AsyncTask<String, Void, List<Weather>>{

        private WeatherDao mAsyncDao;
        private WeatherRepository delegate = null;

        getWeatherByCoordinatesAndTypeAsyncTask(WeatherDao dao) { mAsyncDao = dao;}

        @Override
        protected List<Weather> doInBackground(String... strings) {
            return mAsyncDao.getAllWeatherByCoordinatesAndType(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            delegate.asyncFinished(weathers);
        }
    }
}
