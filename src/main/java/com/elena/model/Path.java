package com.elena.model;

import com.elena.utils.ElenaUtils;

import java.util.*;

public class Path {

    private int cumulativeElevation = 0;
    private double pathDistance = 0.0;
    private List<Edge> edges = new ArrayList<>();
    private List<ElenaNode> path = new ArrayList<>();

    public Path() {

    }

    public Path(ElenaNode end, Map<ElenaNode, ElenaNode> pathTraversal) {
        this.setPath(end, pathTraversal);
    }

    public void setPath(List<ElenaNode> path) {
        this.path = path;
        this.calculateCumulativeElevation();
        this.calcPathDistance();
        this.setEdges();
    }

    public void setPath(ElenaNode end, Map<ElenaNode, ElenaNode> pathTraversal) {
        ElenaNode currentNode = end;
        while (pathTraversal.get(currentNode) != null) {
            this.path.add(currentNode);
            currentNode = pathTraversal.get(currentNode);
        }
        this.path.add(currentNode);
        Collections.reverse(this.path);

        this.calculateCumulativeElevation();
        this.calcPathDistance();
        this.setEdges();
    }

    public ElenaNode getSourceNode() {
        return this.path.get(0);
    }

    public ElenaNode getTargetNode() {
        return this.path.get(this.path.size()-1);
    }

    public List<ElenaNode> getPath() {
        return this.path;
    }

    public double getPathDistance() {
        return this.pathDistance;
    }

    public int getCumulativeElevation() {
        return this.cumulativeElevation;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }



    private void setEdges() {
        for (int i = 1; i < this.path.size(); i++) {
            Edge edge = new Edge(this.path.get(i-1), this.path.get(i));
            this.edges.add(edge);
        }
    }

    private void calcPathDistance() {
        for (int i = 1; i < this.path.size(); i++) {
            this.pathDistance += ElenaUtils.distanceBetweenNodes(this.path.get(i-1), this.path.get(i));
        }
    }

    private void calculateCumulativeElevation() {
        int prevElevation = Integer.MIN_VALUE;
        for (ElenaNode node: this.path) {
            if (prevElevation != Integer.MIN_VALUE && prevElevation < node.getEle()) {
                this.cumulativeElevation += node.getEle() - prevElevation;
            }
            prevElevation = node.getEle();
        }
    }

}
