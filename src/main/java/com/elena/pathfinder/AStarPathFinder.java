package com.elena.pathfinder;

import com.elena.model.*;
import com.elena.repository.GeoDataDAL;
import com.elena.utils.ElenaUtils;

import java.util.*;

public class AStarPathFinder extends PathFinder {

    private Set<Edge> excludedEdges = new HashSet<>();

    public AStarPathFinder() {

    }

    public AStarPathFinder(GeoDataDAL geoDataDAL, Set<Edge> excludedEdges) {
        this.geoDataDAL = geoDataDAL;
        this.excludedEdges = excludedEdges;
    }

    private class WrapperNode implements Comparable<WrapperNode> {
        ElenaNode node;
        double fCost;
        double gCost;
        double hCost;

        public WrapperNode(ElenaNode node) {
            this.node = node;
            this.fCost = 0.0;
            this.gCost = 0.0;
            this.hCost = 0.0;
        }

        public void setGCost(double gCost) {
            this.gCost = gCost;
            this.calcFCost();
        }

        public void setHCost(double hCost) {
            this.hCost = hCost;
            this.calcFCost();
        }

        public void calcFCost() {
            this.fCost = this.gCost + this.hCost;
        }

        @Override
        public int compareTo(WrapperNode other) {
            return Double.compare(this.fCost, other.fCost);
        }
    }


    @Override
    public List<Path> findPath(ElenaNode start, ElenaNode end) {
        List<Path> paths = new ArrayList<>();
        PriorityQueue<WrapperNode> queue = new PriorityQueue<>();
        Map<ElenaNode, ElenaNode> nodeAncestor = new HashMap<>();
        Map<ElenaNode, WrapperNode> nodeList = new HashMap<>();

        WrapperNode startWrapperNode = new WrapperNode(start);
        startWrapperNode.setHCost(this.heuristic(start, end));

        queue.add(startWrapperNode);
        nodeList.put(start, startWrapperNode);
        nodeAncestor.put(start, null);

        while (!queue.isEmpty()) {
            WrapperNode candidateWrapperNode = queue.poll();

            if (candidateWrapperNode.node.equals(end)) {
                paths.add(new Path(end, nodeAncestor));
                return paths;
            }

            for (ElenaNeighbor nei: candidateWrapperNode.node.getNeighbors()) {
                ElenaNode neighborNode = this.geoDataDAL.getNodeById(nei.getNei());
                Edge edge = new Edge(candidateWrapperNode.node, neighborNode);
                if (!this.excludedEdges.contains(edge)) {
                    double newGCost = candidateWrapperNode.gCost + nei.getCost();
                    if (!nodeList.containsKey(neighborNode)) {
                        WrapperNode neighborWrapperNode = new WrapperNode(neighborNode);
                        nodeList.put(neighborNode, neighborWrapperNode);
                        neighborWrapperNode.setGCost(newGCost);
                        neighborWrapperNode.setHCost(this.heuristic(candidateWrapperNode.node, neighborNode));
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborNode, candidateWrapperNode.node);
                    }
                    else if (newGCost < nodeList.get(neighborNode).gCost) {
                        nodeList.get(neighborNode).setGCost(newGCost);
                        nodeAncestor.put(neighborNode, candidateWrapperNode.node);
                    }
                }
            }

        }

        return paths;
    }



    private double heuristic(ElenaNode startNode, ElenaNode goalNode) {
        return ElenaUtils.distanceBetweenNodes(startNode, goalNode);
    }




}