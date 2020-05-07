package com.elena.model;


import java.util.List;

public class Location implements ElenaLocation {

    private String type;
    private List<Double> coordinates;

    public Location(String type, List<Double> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    @Override
    public double[] getLonLatDouble() {
        return new double[]{this.coordinates.get(0), this.coordinates.get(1)};
    }

    @Override
    public double[] getLatLonDouble() {
        return new double[]{this.coordinates.get(1), this.coordinates.get(0)};
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<Double> getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Location{" +
                "type='" + type + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
