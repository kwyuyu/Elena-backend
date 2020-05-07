package com.elena.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "graph")
public class Node implements ElenaNode {

    @Id
    private ObjectId _id;
    private String id;
    private String name;
    private int ele;
    private Location location;
    private String address;
    private List<Neighbor> neighbors;


    public Node(ObjectId _id, String id, String name, int ele, Location location, String address, List<Neighbor> neighbors) {
        this._id = _id;
        this.id = id;
        this.name = name;
        this.ele = ele;
        this.location = location;
        this.address = address;
        this.neighbors = neighbors;
    }


    @Override
    public LonLat getLonLat() {
        return new LonLat(this.location.getCoordinates().get(0), this.location.getCoordinates().get(1));
    }

    @Override
    public double[] getLonLatDouble() {
        return this.location.getLonLatDouble();
    }

    @Override
    public double[] getLatLonDouble() {
        return this.location.getLatLonDouble();
    }


    @Override
    public ObjectId get_id() {
        return _id;
    }

    @Override
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getEle() {
        return ele;
    }

    @Override
    public void setEle(int ele) {
        this.ele = ele;
    }

    @Override
    public ElenaLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(ElenaLocation location) {
        this.location = (Location) location;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public List<? extends ElenaNeighbor> getNeighbors() {
        return neighbors;
    }

    @Override
    public void setNeighbors(List<? extends ElenaNeighbor> neighbors) {
        this.neighbors = (List<Neighbor>) neighbors;
    }

    @Override
    public boolean equals(Object other) {
        return this.id.equals(((ElenaNode)other).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, id, name, ele, location, address, neighbors);
    }


    @Override
    public String toString() {
        return "Node{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", elevation=" + ele +
                ", location=" + location +
                ", address='" + address + '\'' +
                ", neighbors=" + neighbors +
                '}';
    }
}