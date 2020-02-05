package com.patelheggere.poacollection.models;

public class TreeModel {
    private String name;
    private String height;
    private String age;
    private String health;
    private double lat, lon;
    private String pin;
    private String maintainedBy;
    private long dateTime;
    private String photoURL;

    public TreeModel() {
    }

    public TreeModel(String photoURL, long dateTime, String name, String height, String age, String health, double lat, double lon, String pin, String maintainedBy) {
        this.name = name;
        this.height = height;
        this.age = age;
        this.health = health;
        this.lat = lat;
        this.lon = lon;
        this.pin = pin;
        this.dateTime = dateTime;
        this.maintainedBy = maintainedBy;
        this.photoURL = photoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMaintainedBy() {
        return maintainedBy;
    }

    public void setMaintainedBy(String maintainedBy) {
        this.maintainedBy = maintainedBy;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
