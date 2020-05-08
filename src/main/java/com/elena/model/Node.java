package com.elena.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "graph")
public class Node {

    @Id
    private ObjectId _id;
    private String id;
    private String name;
    private int ele;
    private List<Double> coordinates;
    private String address;
    private List<Edge> outGoingEdges;


    public LonLat getLonLat() {
        return new LonLat(this.coordinates.get(0), this.coordinates.get(1));
    }

    public double[] getLonLatDouble() {
        return new double[]{this.coordinates.get(0), this.coordinates.get(1)};
    }

    public double[] getLatLonDouble() {
        return new double[]{this.coordinates.get(1), this.coordinates.get(0)};
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEle() {
        return ele;
    }

    public void setEle(int ele) {
        this.ele = ele;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Edge> getOutGoingEdges() {
        return outGoingEdges;
    }

    public void setOutGoingEdges(List<Edge> outGoingEdges) {
        this.outGoingEdges = outGoingEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return ele == node.ele &&
                Objects.equals(_id, node._id) &&
                Objects.equals(id, node.id) &&
                Objects.equals(name, node.name) &&
                Objects.equals(coordinates, node.coordinates) &&
                Objects.equals(address, node.address) &&
                Objects.equals(outGoingEdges, node.outGoingEdges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, id, name, ele, coordinates, address, outGoingEdges);
    }

    @Override
    public String toString() {
        return "Node{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ele=" + ele +
                ", coordinates=" + coordinates +
                ", address='" + address + '\'' +
                ", outGoingEdges=" + outGoingEdges +
                '}';
    }
}