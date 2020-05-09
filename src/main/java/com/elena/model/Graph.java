package com.elena.model;

import com.elena.autocomplete.BruteForceAutoComplete;
import com.elena.autocomplete.AutoComplete;
import com.elena.utils.ElenaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private Map<String, Node> nodeById = new HashMap<>();
    private Map<String, Node> nodeByAddress = new HashMap<>();
    private Map<LonLat, Node> nodeByLonLat = new HashMap<>();

    public Graph() {

    }

    public Graph(Map<String, Node> graph) {

        for (Node node: graph.values()) {
            this.nodeById.put(node.getId(), node);
            this.nodeByAddress.put(node.getAddress().toLowerCase(), node);
            this.nodeByLonLat.put(node.getLonLat(), node);
        }

    }

    public List<String> getAllAddresses() {
        return new ArrayList<>(this.nodeByAddress.keySet());
    }

    public Node getNodeByAddress(String address) {
        return this.nodeByAddress.getOrDefault(address, null);
    }

    public Node getNodeById(String id) {
        return this.nodeById.getOrDefault(id, null);
    }

    public Node getNodeByLonLat(LonLat lonLat) {
        return this.nodeByLonLat.getOrDefault(lonLat, null);
    }

    public Node getNearestNode(LonLat lonLat) {
        double minDist = Double.MAX_VALUE;
        Node nearestNode = null;

        for (Node node: this.nodeById.values()) {
            double dist = ElenaUtils.distanceBetweenNodes(lonLat, node.getLonLat());
            if (dist < minDist) {
                minDist = dist;
                nearestNode = node;
            }
        }
        return nearestNode;
    }

}
