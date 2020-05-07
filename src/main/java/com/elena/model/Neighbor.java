package com.elena.model;


public class Neighbor implements ElenaNeighbor {

    private String nei;
    private double cost;

    public Neighbor(String nei, double cost) {
        this.nei = nei;
        this.cost = cost;
    }


    public String getNei() {
        return nei;
    }

    public void setNei(String nei) {
        this.nei = nei;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Neighbor{" +
                "nei=" + nei +
                ", cost=" + cost +
                '}';
    }
}
