package com.elena.pathfinder;

import com.elena.utils.Algorithm;

import com.elena.utils.ElevationMode;
import org.springframework.stereotype.Component;

@Component
public class PathFinderFactory {

    public static PathFinder getPathFinder(Algorithm algorithm, ElevationMode elevationMode, int tolerance, int number) {
        switch (algorithm) {
            case ASTAR:
                if (tolerance == 100) {
                    if (number == 1) {
                        return new AStarPathFinder();
                    }
                    return new AStarMultiPathFinder(number);
                }
                return new AStarElevationModePathFinder(elevationMode, tolerance, number);

            case DIJKSTRA:
                if (tolerance == 100) {
                    if (number == 1) {
                        return new DijkstraPathFinder();
                    }
                    return new DijkstraMultiPathFinder(number);
                }
                return new DijkstraElevationModePathFinder(elevationMode, tolerance, number);

            default:
                return new AStarPathFinder();
        }
    }
}
