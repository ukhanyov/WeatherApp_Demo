package com.example.oleg.weatherapp_demo.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

public class MyLocationRepository implements AsyncResultMyLocation{

    private MyLocationDao mMyLocationDao;
    private LiveData<List<MyLocation>> mAllMyLocations;
    private MutableLiveData<MyLocation> searchResults = new MutableLiveData<>();

    MyLocationRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        mMyLocationDao = db.myLocationDao();
        mAllMyLocations = mMyLocationDao.getAllLocations();
    }

    @Override
    public void asyncFinished(MyLocation result) {
        searchResults.setValue(result);
    }

    LiveData<List<MyLocation>> getAllMyLocations(){
        return mAllMyLocations;
    }

    MutableLiveData<MyLocation> getSearchResults() { return searchResults; }

    // Insert stuff
    void insertMyLocation(MyLocation myLocation) { new insertAsyncTask(mMyLocationDao).execute(myLocation); }

    private static class insertAsyncTask extends AsyncTask<MyLocation, Void, Void>{

        private MyLocationDao mAsyncDao;

        insertAsyncTask(MyLocationDao dao) {
            this.mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(MyLocation... myLocations) {
            mAsyncDao.insert(myLocations[0]);
            return null;
        }
    }

    // Delete stuff
    void deleteAll() {
        new deleteAllAsyncTask(mMyLocationDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<MyLocation, Void, Void> {

        private MyLocationDao mAsyncDao;

        deleteAllAsyncTask(MyLocationDao dao) {
            this.mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(MyLocation... myLocations) {
            mAsyncDao.deleteAll();
            return null;
        }
    }

    void deleteSpecificMyLocation(String name) {
        new deleteSpecificMyLocationAsyncTask(mMyLocationDao).execute(name);
    }

    private static class deleteSpecificMyLocationAsyncTask extends AsyncTask<String, Void, Void> {

        private MyLocationDao mAsyncDao;

        deleteSpecificMyLocationAsyncTask(MyLocationDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncDao.deleteSpecificLocation(strings[0]);
            return null;
        }
    }

    // Query specific MyLocation stuff
    void querySpecificMyLocation(String name) {
        querySpecificMyLocationAsyncTask task = new querySpecificMyLocationAsyncTask(mMyLocationDao);
        task.delegate = this;
        task.execute(name);

    }

    private static class querySpecificMyLocationAsyncTask extends AsyncTask<String, Void, MyLocation> {

        private MyLocationDao mAsyncDao;
        private MyLocationRepository delegate = null;

        querySpecificMyLocationAsyncTask(MyLocationDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected MyLocation doInBackground(String... strings) {
            return mAsyncDao.getSpecificLocation(strings[0]);
        }

        @Override
        protected void onPostExecute(MyLocation myLocation) {
            delegate.asyncFinished(myLocation);
        }
    }
}
