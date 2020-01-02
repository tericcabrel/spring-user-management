package com.tericcabrel.authorization.models;

public class Coordinates {
    private float lat;

    private float lon;

    public float getLat() {
        return lat;
    }

    public Coordinates setLat(float lat) {
        this.lat = lat;
        return this;
    }

    public float getLon() {
        return lon;
    }

    public Coordinates setLon(float lon) {
        this.lon = lon;
        return this;
    }
}
