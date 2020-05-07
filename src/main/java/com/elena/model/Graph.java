package com.elena.model;

import com.elena.autocomplete.BruteForceAutoComplete;
import com.elena.autocomplete.AutoComplete;
import com.elena.utils.ElenaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph implements ElenaGraph {

    private Map<String, ElenaNode> nodeById = new HashMap<>();
    private Map<String, ElenaNode> nodeByAddress = new HashMap<>();
    private Map<LonLat, ElenaNode> nodeByLonLat = new HashMap<>();
    private AutoComplete addressAutoComplete = new BruteForceAutoComplete();

    public Graph() {

    }

    public Graph(Map<String, Node> graph) {

        for (Node node: graph.values()) {
            this.nodeById.put(node.getId(), node);
            this.nodeByAddress.put(node.getAddress().toLowerCase(), node);
            this.nodeByLonLat.put(node.getLonLat(), node);
        }

        this.addressAutoComplete.buildSuggestions(this.nodeByAddress.keySet());

    }

    @Override
    public List<String> getAllAddresses() {
        return new ArrayList<>(this.nodeByAddress.keySet());
    }

    @Override
    public ElenaNode getNodeByAddress(String address) {
        return this.nodeByAddress.getOrDefault(address, null);
    }

    @Override
    public ElenaNode getNodeById(String id) {
        return this.nodeById.getOrDefault(id, null);
    }

    @Override
    public ElenaNode getNodeByLonLat(LonLat lonLat) {
        return this.nodeByLonLat.getOrDefault(lonLat, null);
    }

    @Override
    public ElenaNode getNearestNode(LonLat lonLat) {
        double minDist = Double.MAX_VALUE;
        ElenaNode nearestNode = null;

        for (ElenaNode node: this.nodeById.values()) {
            double dist = ElenaUtils.distanceBetweenNodes(lonLat, node.getLonLat());
            if (dist < minDist) {
                minDist = dist;
                nearestNode = node;
            }
        }
        return nearestNode;
    }


    @Override
    public List<String> getAutoCompleteSuggestions(String userInput) {
        String input = userInput.toLowerCase();
        return this.addressAutoComplete.getSuggestions(userInput);
    }
}
