package com.example.oleg.weatherapp_demo.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "location_table")
public class MyLocation {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "locationName")
    private String mLocationName;

    @ColumnInfo(name = "locationCoordinates")
    private String mLocationCoordinates;

    @ColumnInfo(name = "latitude")
    private Double mLatitude;

    @ColumnInfo(name = "longitude")
    private Double mLongitude;

    @ColumnInfo(name = "imageString")
    String mImageString;

    public MyLocation(@NonNull String locationName, String locationCoordinates, Double latitude, Double longitude, String imageString) {
        this.mLocationName = locationName;
        this.mLocationCoordinates = locationCoordinates;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mImageString = imageString;
    }

    public void setLocationName(@NonNull String mLocationName) {
        this.mLocationName = mLocationName;
    }

    public void setLocationCoordinates(String mLocationCoordinates) {
        this.mLocationCoordinates = mLocationCoordinates;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public String getLocationCoordinates() {
        return mLocationCoordinates;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public String getImageString() {
        return mImageString;
    }
}
