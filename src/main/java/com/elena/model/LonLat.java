package com.elena.model;

import java.util.Objects;

public class LonLat {

    private Double lon;
    private Double lat;

    public LonLat(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public LonLat(String lon, String lat) {
        this.lon = Double.valueOf(lon);
        this.lat = Double.valueOf(lat);
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public boolean equals(Object obj) {
        LonLat other = (LonLat) obj;
        if ((this.lon.equals(other.getLon())) && (this.lat.equals(other.getLat()))) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lon, lat);
    }

    @Override
    public String toString() {
        return "LonLat{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
