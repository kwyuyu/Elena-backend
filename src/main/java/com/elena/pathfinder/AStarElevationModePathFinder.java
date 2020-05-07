package com.elena.pathfinder;

import com.elena.model.ElenaNode;
import com.elena.model.Path;
import com.elena.utils.ElevationMode;

import java.util.List;

public class AStarElevationModePathFinder extends PathFinder {

    private ElevationMode elevationMode;
    private double tolerance;
    private int number;

    public AStarElevationModePathFinder(ElevationMode elevationMode, int tolerance, int number) {
        this.elevationMode = elevationMode;
        this.tolerance = tolerance / 100.0;
        this.number = number;
    }

    @Override
    public List<Path> findPath(ElenaNode start, ElenaNode end) {
        PathFinder pathFinder = new AStarMultiPathFinder(this.geoDataDAL, 20);
        List<Path> paths = pathFinder.findPath(start, end);

        return PathFinderUtils.sortByElevation(elevationMode, tolerance, paths, this.number);
    }

}
