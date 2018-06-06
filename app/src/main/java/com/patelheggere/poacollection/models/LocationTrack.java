package com.patelheggere.poacollection.models;

/**
 * Created by Patel Heggere on 4/6/2018.
 */

public class LocationTrack {
    private double mLatitude;
    private double mLongitude;
    private String mPointName;
    private double mElevation;
    private Long mTime;

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmPointName() {
        return mPointName;
    }

    public void setmPointName(String mPointName) {
        this.mPointName = mPointName;
    }

    public double getmElevation() {
        return mElevation;
    }

    public void setmElevation(double mElevation) {
        this.mElevation = mElevation;
    }

    public Long getmTime() {
        return mTime;
    }

    public void setmTime(Long mTime) {
        this.mTime = mTime;
    }
}
