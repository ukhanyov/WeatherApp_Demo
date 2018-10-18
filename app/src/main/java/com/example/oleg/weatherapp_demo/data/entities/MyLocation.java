package com.example.oleg.weatherapp_demo.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "location_table")
public class MyLocation {

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "locationId")
//    private Integer mId;

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

    public MyLocation(@NonNull String locationName, String locationCoordinates, Double latitude, Double longitude) {
        this.mLocationName = locationName;
        this.mLocationCoordinates = locationCoordinates;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
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
}
