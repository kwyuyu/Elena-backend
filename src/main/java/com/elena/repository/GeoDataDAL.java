package com.elena.repository;

import com.elena.model.ElenaNode;
import com.elena.model.LonLat;


import java.util.List;


public interface GeoDataDAL {

    /**
     * get ElenaNode by longitude and latitude
     * @param lonLat
     * @return
     */
    ElenaNode getNodeByLonLat(LonLat lonLat);

    /**
     * get ElenaNode by node id (osm id)
     * @param id
     * @return
     */
    ElenaNode getNodeById(String id);

    /**
     * get ElenaNode by address
     * @param address
     * @return
     */
    ElenaNode getNodeByAddress(String address);

    /**
     * get a list of neighbor ElenaNode by given node
     * @param node
     * @return
     */
    List<ElenaNode> getNeighborNodes(ElenaNode node);

    /**
     * get the nearest ElenaNode by longitude and latitude
     * @param lonLat
     * @return
     */
    ElenaNode getNearestNode(LonLat lonLat);

    /**
     * get a list of address suggestions given an user input
     * @param userInput
     * @return
     */
    List<String> getAutoCompleteSuggestions(String userInput);
}
