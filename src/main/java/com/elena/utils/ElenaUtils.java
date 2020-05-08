package com.elena.utils;

import com.elena.model.LonLat;
import com.elena.model.Node;

public class ElenaUtils {

    /**
     * url decoding
     * @param input
     * @return
     */
    public static String elenaUrlDecode(String input) {
        input = input.replace("!-", "+");
        input = input.replace("!,", "&");
        input = input.replace("!<", "=");
        return input;
    }


    /**
     * calculate the distance between two nodes
     * @param startNode
     * @param goalNode
     * @return
     */
    public static double distanceBetweenNodes(Node startNode, Node goalNode) {
        LonLat start = startNode.getLonLat();
        LonLat goal = goalNode.getLonLat();

        if (start.equals(goal)) {
            return 0.0;
        }

        double phi1 = Math.toRadians(start.getLat());
        double phi2 = Math.toRadians(goal.getLat());
        double dphi = Math.toRadians(goal.getLat() - start.getLat());
        double dlambda = Math.toRadians(goal.getLon() - start.getLon());

        double a = Math.pow(Math.sin(dphi / 2), 2) + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(dlambda / 2), 2);
        double R = 6372800;

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /**
     * calculate the distnace between two pair of longitude and latitude
     * @param start
     * @param goal
     * @return
     */
    public static double distanceBetweenNodes(LonLat start, LonLat goal) {
        if (start.equals(goal)) {
            return 0.0;
        }

        double phi1 = Math.toRadians(start.getLat());
        double phi2 = Math.toRadians(goal.getLat());
        double dphi = Math.toRadians(goal.getLat() - start.getLat());
        double dlambda = Math.toRadians(goal.getLon() - start.getLon());

        double a = Math.pow(Math.sin(dphi / 2), 2) + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(dlambda / 2), 2);
        double R = 6372800;

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }



}
