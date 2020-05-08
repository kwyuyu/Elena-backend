package com.elena.pathfinder;

import com.elena.model.Edge;
import com.elena.model.Node;
import com.elena.model.Path;
import com.elena.repository.GeoDataDAL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AStarMultiPathFinder extends PathFinder {

    private int number;

    public AStarMultiPathFinder() {

    }

    public AStarMultiPathFinder(int number) {
        this.number = number;
    }

    public AStarMultiPathFinder(GeoDataDAL geoDataDAL, int number) {
        this.geoDataDAL = geoDataDAL;
        this.number = number;
    }


    @Override
    public List<Path> findPath(Node start, Node end) {
        Set<Edge> excludedEdges = new HashSet<>();
        PathFinder pathFinder = new AStarPathFinder(this.geoDataDAL, excludedEdges);

        List<Path> paths = new ArrayList<>();
        for (int i = 0; i < this.number; i++) {
            List<Path> foundPaths = pathFinder.findPath(start, end);
            if (foundPaths.size() == 0) {
                break;
            }
            PathFinderUtils.updateExcludedEdges(foundPaths.get(0), excludedEdges);
            paths.add(foundPaths.get(0));
        }

        PathFinderUtils.sortPathByDistance(paths);
        return paths;
    }
}
