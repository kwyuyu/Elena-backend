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
        Node node;
        double cost;

        public WrapperNode(Node node) {
            this.node = node;
            this.cost = 0.0;
        }

        @Override
        public int compareTo(WrapperNode other) {
            return Double.compare(this.cost, other.cost);
        }
    }


    @Override
    public List<Path> findPath(Node start, Node end) {
        List<Path> paths = new ArrayList<>();
        PriorityQueue<WrapperNode> queue = new PriorityQueue<>();
        Map<Node, Node> nodeAncestor = new HashMap<>();
        Map<String, WrapperNode> nodeList = new HashMap<>();

        WrapperNode startWrapperNode = new WrapperNode(start);

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
                    double newCost = candidateWrapperNode.cost + neiEdge.getCost();
                    if (!nodeList.containsKey(neiEdge.getTo())) {
                        WrapperNode neighborWrapperNode = new WrapperNode(this.geoDataDAL.getNodeById(neiEdge.getTo()));
                        nodeList.put(neiEdge.getTo(), neighborWrapperNode);
                        neighborWrapperNode.cost = newCost;
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborWrapperNode.node, candidateWrapperNode.node);
                    }
                    else if (newCost < nodeList.get(neiEdge.getTo()).cost) {
                        WrapperNode neighborWrapperNode = nodeList.get(neiEdge.getTo());
                        queue.remove(neighborWrapperNode);
                        neighborWrapperNode.cost = newCost;
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborWrapperNode.node, candidateWrapperNode.node);
                    }
                }
            }
        }

        return paths;
    }


}
