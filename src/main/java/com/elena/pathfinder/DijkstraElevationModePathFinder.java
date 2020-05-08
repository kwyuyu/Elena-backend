package com.elena.pathfinder;

import com.elena.model.Node;
import com.elena.model.Path;
import com.elena.utils.ElevationMode;

import java.util.List;

public class DijkstraElevationModePathFinder extends PathFinder {

    private ElevationMode elevationMode;
    private double tolerance;
    private int number;

    public DijkstraElevationModePathFinder(ElevationMode elevationMode, int tolerance, int number) {
        this.elevationMode = elevationMode;
        this.tolerance = tolerance / 100.0;
        this.number = number;
    }


    @Override
    public List<Path> findPath(Node start, Node end) {
        PathFinder pathFinder = new DijkstraMultiPathFinder(this.geoDataDAL, 20);
        List<Path> paths = pathFinder.findPath(start, end);

        return PathFinderUtils.sortByElevation(elevationMode, tolerance, paths, this.number);
    }
}
