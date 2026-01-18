package com.example.fjnuserviceapp.ui.nav.model;

/**
 * 经纬度点模型
 */
public class LatLngPoint {
    private double latitude;
    private double longitude;

    public LatLngPoint() {
    }

    public LatLngPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLngPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}