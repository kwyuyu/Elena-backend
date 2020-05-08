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
        Node node;
        double fCost;
        double gCost;
        double hCost;

        public WrapperNode(Node node) {
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
    public List<Path> findPath(Node start, Node end) {
        List<Path> paths = new ArrayList<>();
        PriorityQueue<WrapperNode> queue = new PriorityQueue<>();
        Map<Node, Node> nodeAncestor = new HashMap<>();
        Map<String, WrapperNode> nodeList = new HashMap<>();

        WrapperNode startWrapperNode = new WrapperNode(start);
        startWrapperNode.setHCost(this.heuristic(start, end));

        queue.add(startWrapperNode);
        nodeList.put(start.getId(), startWrapperNode);
        nodeAncestor.put(start, null);

        while (!queue.isEmpty()) {
            WrapperNode candidateWrapperNode = queue.poll();

            if (candidateWrapperNode.node.equals(end)) {
                paths.add(new Path(end, nodeAncestor));
                return paths;
            }


            for (Edge neiEdge: candidateWrapperNode.node.getOutGoingEdges()) {
                if (!this.excludedEdges.contains(neiEdge)) {
                    double newGCost = candidateWrapperNode.gCost + neiEdge.getCost();
                    if (!nodeList.containsKey(neiEdge.getTo())) {
                        WrapperNode neighborWrapperNode = new WrapperNode(this.geoDataDAL.getNodeById(neiEdge.getTo()));
                        nodeList.put(neiEdge.getTo(), neighborWrapperNode);
                        neighborWrapperNode.setGCost(newGCost);
                        neighborWrapperNode.setHCost(this.heuristic(candidateWrapperNode.node, neighborWrapperNode.node));
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborWrapperNode.node, candidateWrapperNode.node);
                    }
                    else if (newGCost < nodeList.get(neiEdge.getTo()).gCost) {
                        WrapperNode neighborWrapperNode = nodeList.get(neiEdge.getTo());
                        neighborWrapperNode.setGCost(newGCost);
                        nodeAncestor.put(neighborWrapperNode.node, candidateWrapperNode.node);
                    }
                }
            }
        }

        return paths;
    }




    private double heuristic(Node startNode, Node goalNode) {
        return ElenaUtils.distanceBetweenNodes(startNode, goalNode);
    }




}