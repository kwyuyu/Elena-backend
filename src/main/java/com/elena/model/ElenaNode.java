package com.elena.model;

import org.bson.types.ObjectId;

import java.util.List;

public interface ElenaNode {

    public LonLat getLonLat();
    public double[] getLonLatDouble();
    public double[] getLatLonDouble();

    public ObjectId get_id();
    public void set_id(ObjectId _id);

    public String getId();
    public void setId(String id);

    public String getName();
    public void setName(String name);

    public int getEle();
    public void setEle(int ele);

    public ElenaLocation getLocation();
    public void setLocation(ElenaLocation location);

    public String getAddress();
    public void setAddress(String address);

    public List<? extends ElenaNeighbor> getNeighbors();
    public void setNeighbors(List<? extends ElenaNeighbor> neighbors);

    public boolean equals(Object other);

    public int hashCode();

    public String toString();
}
