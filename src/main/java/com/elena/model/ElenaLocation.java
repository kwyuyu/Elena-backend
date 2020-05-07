package com.elena.model;

import java.util.List;

public interface ElenaLocation {

    public double[] getLonLatDouble();

    public double[] getLatLonDouble();

    public String getType();

    public void setType(String type);

    public List<Double> getCoordinates();

    public void setCoordinates(List<Double> coordinates);

    public String toString();

}
