package com.elena.repository;

import com.elena.model.LonLat;
import com.elena.model.Node;


import java.util.List;


public interface GeoDataDAL {

    /**
     * get Node by longitude and latitude
     * @param lonLat
     * @return
     */
    Node getNodeByLonLat(LonLat lonLat);

    /**
     * get Node by node id (osm id)
     * @param id
     * @return
     */
    Node getNodeById(String id);

    /**
     * get Node by address
     * @param address
     * @return
     */
    Node getNodeByAddress(String address);

    /**
     * get the nearest Node by longitude and latitude
     * @param lonLat
     * @return
     */
    Node getNearestNode(LonLat lonLat);

    /**
     * get a list of addresses
     * @return
     */
    List<String> getAllAddress();
}
