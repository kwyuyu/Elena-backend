package com.elena.service;

import com.elena.autocomplete.AutoComplete;
import com.elena.model.LonLat;
import com.elena.model.Node;
import com.elena.model.Path;
import com.elena.repository.GeoDataDAL;
import com.elena.pathfinder.PathFinder;
import com.elena.utils.ElenaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("elenaService")
public class ElenaService {

    private final GeoDataDAL geoDataDAL;
    private final AutoComplete autoComplete;

    @Autowired
    public ElenaService(@Qualifier("graphDAL") GeoDataDAL geoDataDAL,
                        @Qualifier("bruteForce") AutoComplete autoComplete) {
        this.geoDataDAL = geoDataDAL;
        this.autoComplete = autoComplete;
        this.autoComplete.setGeoDataDAL(geoDataDAL);
    }

    public List<String> getAutoCompleteSuggestions(String userInput) {
        return this.autoComplete.getSuggestions(userInput);
    }

    public List<Path> findPathByAddress(PathFinder pathFinder, String addressFrom, String addressTo) {
        pathFinder.setGeoDataDAL(this.geoDataDAL);
        Node from = this.geoDataDAL.getNodeByAddress(ElenaUtils.elenaUrlDecode(addressFrom));
        Node to = this.geoDataDAL.getNodeByAddress(ElenaUtils.elenaUrlDecode(addressTo));

        return pathFinder.findPath(from, to);
    }

    public List<Path> findPathByLonLat(PathFinder pathFinder, String lonLatFrom, String lonLatTo) {
        pathFinder.setGeoDataDAL(this.geoDataDAL);

        String[] lonLatFromString = lonLatFrom.split(",");
        String[] lonLatToString = lonLatTo.split(",");

        Node from = this.geoDataDAL.getNearestNode(new LonLat(lonLatFromString[0], lonLatFromString[1]));
        Node to = this.geoDataDAL.getNearestNode(new LonLat(lonLatToString[0], lonLatToString[1]));

        return pathFinder.findPath(from, to);
    }
}
