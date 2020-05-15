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

        public WrapperNode(Node node, double gCost, Node hCostDestinationNode) {
            this.node = node;
            this.gCost = gCost;
            this.hCost = heuristic(node, hCostDestinationNode);
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

        WrapperNode startWrapperNode = new WrapperNode(start, 0.0, end);

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
                        WrapperNode neighborWrapperNode = new WrapperNode(this.geoDataDAL.getNodeById(neiEdge.getTo()), 0.0, end);
                        nodeList.put(neiEdge.getTo(), neighborWrapperNode);
                        neighborWrapperNode.gCost = newGCost;
                        neighborWrapperNode.fCost = neighborWrapperNode.gCost + neighborWrapperNode.hCost;
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborWrapperNode.node, candidateWrapperNode.node);
                    }
                    else if (newGCost < nodeList.get(neiEdge.getTo()).gCost) {
                        WrapperNode neighborWrapperNode = nodeList.get(neiEdge.getTo());
                        queue.remove(neighborWrapperNode);
                        neighborWrapperNode.gCost = newGCost;
                        neighborWrapperNode.fCost = neighborWrapperNode.gCost + neighborWrapperNode.hCost;
                        queue.add(neighborWrapperNode);
                        nodeAncestor.put(neighborWrapperNode.node, candidateWrapperNode.node);
                    }
                }
            }
        }

        return paths;
    }

    private double heuristic(Node startNode, Node goalNode) {
        return 0.97 * ElenaUtils.distanceBetweenNodes(startNode, goalNode);
    }




}