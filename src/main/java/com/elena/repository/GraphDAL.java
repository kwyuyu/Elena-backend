package com.elena.repository;

import com.elena.model.ElenaGraph;
import com.elena.model.ElenaNode;
import com.elena.model.LonLat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository("graphDAL")
public class GraphDAL implements GeoDataDAL {

    private final ElenaGraph graph;

    @Autowired
    public GraphDAL(@Qualifier("graph") ElenaGraph graph) {
        this.graph = graph;
    }


    @Override
    public ElenaNode getNodeByLonLat(LonLat lonLat) {
        return this.graph.getNodeByLonLat(lonLat);
    }

    @Override
    public ElenaNode getNodeById(String id) {
        return this.graph.getNodeById(id);
    }

    @Override
    public ElenaNode getNodeByAddress(String address) {
        return this.graph.getNodeByAddress(address);
    }

    @Override
    public List<ElenaNode> getNeighborNodes(ElenaNode node) {
        return new ArrayList<ElenaNode>(){{
            node.getNeighbors().forEach(
                    (nei) -> add(getNodeById(nei.getNei()))
            );
        }};
    }

    @Override
    public ElenaNode getNearestNode(LonLat lonLat) {
        return this.graph.getNearestNode(lonLat);
    }

    @Override
    public List<String> getAutoCompleteSuggestions(String userInput) {
        return this.graph.getAutoCompleteSuggestions(userInput);
    }

}
