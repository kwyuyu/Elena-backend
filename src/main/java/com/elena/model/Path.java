package com.elena.model;

import com.elena.utils.ElenaUtils;

import java.util.*;

public class Path {

    private int cumulativeElevation = 0;
    private double pathDistance = 0.0;
    private List<Edge> edges = new ArrayList<>();
    private List<Node> path = new ArrayList<>();

    public Path() {

    }

    public Path(Node end, Map<Node, Node> pathTraversal) {
        this.setPath(end, pathTraversal);
    }

    public void setPath(List<Node> path) {
        this.path = path;
        this.calculateCumulativeElevation();
        this.calcPathDistanceAndSetEdges();
    }

    public void setPath(Node end, Map<Node, Node> pathTraversal) {
        Node currentNode = end;
        while (pathTraversal.get(currentNode) != null) {
            this.path.add(currentNode);
            currentNode = pathTraversal.get(currentNode);
        }
        this.path.add(currentNode);
        Collections.reverse(this.path);

        this.calculateCumulativeElevation();
        this.calcPathDistanceAndSetEdges();
    }

    public Node getSourceNode() {
        return this.path.get(0);
    }

    public Node getTargetNode() {
        return this.path.get(this.path.size()-1);
    }

    public List<Node> getPath() {
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

    private void calcPathDistanceAndSetEdges() {
        for (int i = 1; i < this.path.size(); i++) {
            double dist = ElenaUtils.distanceBetweenNodes(this.path.get(i-1), this.path.get(i));
            this.pathDistance += dist;
            this.edges.add(new Edge(this.path.get(i-1).getId(), this.path.get(i).getId(), dist));
        }
    }

    private void calculateCumulativeElevation() {
        int prevElevation = Integer.MIN_VALUE;
        for (Node node: this.path) {
            if (prevElevation != Integer.MIN_VALUE && prevElevation < node.getEle()) {
                this.cumulativeElevation += node.getEle() - prevElevation;
            }
            prevElevation = node.getEle();
        }
    }

}
