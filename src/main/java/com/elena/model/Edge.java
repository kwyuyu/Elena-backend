package com.elena.model;

import java.util.Objects;

public class Edge {

    private ElenaNode startNode;
    private ElenaNode endNode;

    public Edge(ElenaNode startNode, ElenaNode endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public ElenaNode getStartNode() {
        return startNode;
    }

    public void setStartNode(ElenaNode startNode) {
        this.startNode = startNode;
    }

    public ElenaNode getEndNode() {
        return endNode;
    }

    public void setEndNode(ElenaNode endNode) {
        this.endNode = endNode;
    }

    @Override
    public boolean equals(Object other) {
        Edge otherEdge = (Edge) other;
        ElenaNode otherStartNode = otherEdge.getStartNode();
        ElenaNode otherEndNode = otherEdge.getEndNode();
        return (this.startNode.getId().equals(otherStartNode.getId())) &&
                (this.endNode.getId().equals(otherEndNode.getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startNode, endNode);
    }
}
