package com.elena.pathfinder;

import com.elena.model.*;
import com.elena.repository.GeoDataDAL;

import java.util.*;


public class DijkstraPathFinder extends PathFinder {

    private Set<Edge> excludedEdges = new HashSet<>();

    public DijkstraPathFinder() {

    }

    public DijkstraPathFinder(GeoDataDAL geoDataDAL, Set<Edge> excludedEdges) {
        this.geoDataDAL = geoDataDAL;
        this.excludedEdges = excludedEdges;
    }

    private class WrapperNode implements Comparable<WrapperNode> {
        ElenaNode node;
        double cost;

        public WrapperNode(ElenaNode node) {
            this.node = node;
            this.cost = 0.0;
        }

        @Override
        public int compareTo(WrapperNode other) {
            return Double.compare(this.cost, other.cost);
        }
    }


    @Override
    public List<Path> findPath(ElenaNode start, ElenaNode end) {
        List<Path> paths = new ArrayList<>();
        PriorityQueue<WrapperNode> queue = new PriorityQueue<>();
        Map<ElenaNode, ElenaNode> nodeAncestor = new HashMap<>();
        Map<ElenaNode, WrapperNode> nodeList = new HashMap<>();

        WrapperNode startWrapperNode = new WrapperNode(start);

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
                    double newCost = candidateWrapperNode.cost + nei.getCost();
                    if (!nodeList.containsKey(neighborNode)) {
                        WrapperNode neighborWrapperNode = new WrapperNode(neighborNode);
                        nodeList.put(neighborNode, neighborWrapperNode);
                        neighborWrapperNode.cost = newCost;
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborNode, candidateWrapperNode.node);
                    }
                    else if (newCost < nodeList.get(neighborNode).cost) {
                        nodeList.get(neighborNode).cost = newCost;
                        nodeAncestor.put(neighborNode, candidateWrapperNode.node);
                    }
                }
            }

        }

        return paths;
    }


}
