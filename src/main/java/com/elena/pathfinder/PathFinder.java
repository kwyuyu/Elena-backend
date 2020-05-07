package com.elena.pathfinder;

import com.elena.model.ElenaNode;
import com.elena.model.Path;
import com.elena.repository.GeoDataDAL;

import java.util.List;


public abstract class PathFinder {

    public GeoDataDAL geoDataDAL;

    public void setGeoDataDAL(GeoDataDAL geoDataDAL) {
        this.geoDataDAL = geoDataDAL;
    }

    /**
     * find the shortest path
     * @param start
     * @param end
     * @return
     */
    abstract public List<Path> findPath(ElenaNode start, ElenaNode end);
}
