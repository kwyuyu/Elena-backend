package com.elena.model;

import java.util.List;

public interface ElenaGraph {


    /**
     * @return a list of addresses
     */
    public List<String> getAllAddresses();


    /**
     * get Node by address
     * @param address
     * @return ElenaNode
     */
    public ElenaNode getNodeByAddress(String address);

    /**
     * get Node by node id (osm id)
     * @param id
     * @return ElenaNode
     */
    public ElenaNode getNodeById(String id);


    /**
     * get Node by longitude and latitude
     * @param lonLat
     * @return ElenaNode
     */
    public ElenaNode getNodeByLonLat(LonLat lonLat);

    /**
     * get the nearest node given a pair of longitude and latitude
     * @param lonLat
     * @return ElenaNode
     */
    public ElenaNode getNearestNode(LonLat lonLat);

    /**
     *
     * @param userInput
     * @return a list of suggestions
     */
    public List<String> getAutoCompleteSuggestions(String userInput);
}
