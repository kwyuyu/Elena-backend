package com.elena.pathfinder;

import com.elena.model.Edge;
import com.elena.model.Path;
import com.elena.utils.ElevationMode;

import java.util.*;
import java.util.stream.Collectors;

public class PathFinderUtils {

    /**
     * update the excludedEdges set for next round shortest path search,
     * by doing this, we can find the multiple paths by iterating single-pair shortest path algorithm
     * @param path
     * @param excludedEdges
     */
    public static void updateExcludedEdges(Path path, Set<Edge> excludedEdges) {
//        excludedEdges.addAll(path.getEdges());
        Random randomGenerator = new Random();
        Set<Integer> avoidIndex = new HashSet<Integer>(){{add(0);add(path.getEdges().size()-1);}};

        int excludeSize = (int) Math.ceil(path.getEdges().size() * 0.1);
        for (int i = 0; i < excludeSize; i++) {
            int excludedIndex;
            do {
                excludedIndex = randomGenerator.nextInt(path.getEdges().size());
            } while (avoidIndex.contains(excludedIndex));
            avoidIndex.add(excludedIndex);

            excludedEdges.add(path.getEdges().get(excludedIndex));
        }
    }

    /**
     * sort the given paths by path distance
     * @param paths
     */
    public static void sortPathByDistance(List<Path> paths) {
        Collections.sort(paths, Comparator.comparingDouble(Path::getPathDistance));
    }

    /**
     * sort the given paths by cumulative elevation,
     * and filter the path exceed the max distance tolerance.
     * @param elevationMode
     * @param tolerance
     * @param paths
     * @param number
     * @return
     */
    public static List<Path> sortByElevation(ElevationMode elevationMode, double tolerance, List<Path> paths, int number) {
        double maxDistanceTolerance = paths.get(0).getPathDistance() * tolerance;
        List<Path> filteredPaths = paths.stream().filter((path) -> path.getPathDistance() <= maxDistanceTolerance).collect(Collectors.toList());
        Collections.sort(filteredPaths, (path1, path2) -> {
            switch (elevationMode) {
                case MAX:
                    return -Integer.compare(path1.getCumulativeElevation(), path2.getCumulativeElevation());
                default:
                    return Integer.compare(path1.getCumulativeElevation(), path2.getCumulativeElevation());

            }
        });
        return new ArrayList<Path>(){{
            for (int i = 0; i < Math.min(filteredPaths.size(), number); i++) {
                add(filteredPaths.get(i));
            }
        }};
    }

}

