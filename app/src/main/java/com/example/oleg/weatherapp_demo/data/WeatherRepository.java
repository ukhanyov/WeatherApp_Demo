package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.oleg.weatherapp_demo.data.dao.WeatherDao;
import com.example.oleg.weatherapp_demo.data.entities.Weather;

import java.util.List;

// Timeline 4

public class WeatherRepository implements
        AsyncResultWeatherDaily,
        AsyncResultWeatherHourly{

    private WeatherDao mWeatherDao;
    private MutableLiveData<List<Weather>> searchResultsDaily = new MutableLiveData<>();
    private MutableLiveData<List<Weather>> searchResultsHourly = new MutableLiveData<>();

    WeatherRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mWeatherDao = db.weatherDao();
    }

    @Override
    public void asyncFinishedDaily(List<Weather> result) {
        searchResultsDaily.setValue(result);
    }

    @Override
    public void asyncFinishedHourly(List<Weather> result) {
        searchResultsHourly.setValue(result);
    }

    MutableLiveData<List<Weather>> getSearchResultsDaily() {
        return searchResultsDaily;
    }

    MutableLiveData<List<Weather>> getSearchResultsHourly(){
        return searchResultsHourly;
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

    void deleteSpecificWeatherByTypeAndCoordinates(String deleteKeyType, String deleteKeyCoordinates){
        new deleteSpecificWeatherByTypeAndCoordinatesAsyncTask(mWeatherDao).execute(deleteKeyType, deleteKeyCoordinates);
    }

    private static class deleteSpecificWeatherByTypeAndCoordinatesAsyncTask extends AsyncTask<String, Void, Void>{

        private WeatherDao mAsyncDao;

        deleteSpecificWeatherByTypeAndCoordinatesAsyncTask(WeatherDao dao){
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncDao.deleteSpecificfWeatherByTypeAndLocation(strings[0], strings[1]);
            return null;
        }
    }

    // Query specific weather stuff
    void queryDailyByCoordinatesAndType(String coordinates, String type) {
        getWeatherDailyByCoordinatesAndTypeAsyncTask task = new getWeatherDailyByCoordinatesAndTypeAsyncTask(mWeatherDao);
        task.delegate = this;
        task.execute(coordinates, type);
    }

    private static class getWeatherDailyByCoordinatesAndTypeAsyncTask extends AsyncTask<String, Void, List<Weather>>{

        private WeatherDao mAsyncDao;
        private WeatherRepository delegate = null;

        getWeatherDailyByCoordinatesAndTypeAsyncTask(WeatherDao dao) { mAsyncDao = dao;}

        @Override
        protected List<Weather> doInBackground(String... strings) {
            return mAsyncDao.getAllWeatherByCoordinatesAndType(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            delegate.asyncFinishedDaily(weathers);
        }
    }

    void queryHourlyByCoordinatesAndType(String coordinates, String type) {
        getWeatherHourlyByCoordinatesAndTypeAsyncTask task = new getWeatherHourlyByCoordinatesAndTypeAsyncTask(mWeatherDao);
        task.delegate = this;
        task.execute(coordinates, type);
    }

    private static class getWeatherHourlyByCoordinatesAndTypeAsyncTask extends AsyncTask<String, Void, List<Weather>>{

        private WeatherDao mAsyncDao;
        private WeatherRepository delegate = null;

        getWeatherHourlyByCoordinatesAndTypeAsyncTask(WeatherDao dao) { mAsyncDao = dao;}

        @Override
        protected List<Weather> doInBackground(String... strings) {
            return mAsyncDao.getAllWeatherByCoordinatesAndType(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            delegate.asyncFinishedHourly(weathers);
        }
    }

}
