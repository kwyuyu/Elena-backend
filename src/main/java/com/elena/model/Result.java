package com.elena.model;

import java.util.ArrayList;
import java.util.List;

public class Result {

    public List<double[]> positions;
    public double distance;
    public int elevationAggregate;
    public double[] sourceCoord;
    public double[] targetCoord;
    public String sourceAddress;
    public String targetAddress;

    public Result(Path path) {
        this.distance = path.getPathDistance();
        this.elevationAggregate = path.getCumulativeElevation();
        this.sourceAddress = path.getSourceNode().getAddress();
        this.targetAddress = path.getTargetNode().getAddress();
        this.sourceCoord = path.getSourceNode().getLatLonDouble();
        this.targetCoord = path.getTargetNode().getLatLonDouble();
        this.positions = new ArrayList<double[]>(){{
            path.getPath().forEach(
                    (node) -> add(node.getLatLonDouble())
            );
        }};
    }
}