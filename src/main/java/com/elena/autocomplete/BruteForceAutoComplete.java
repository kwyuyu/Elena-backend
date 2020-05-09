package com.elena.autocomplete;

import com.elena.repository.GeoDataDAL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("bruteForce")
public class BruteForceAutoComplete implements AutoComplete {

    private GeoDataDAL geoDataDAL;

    @Override
    public void setGeoDataDAL(GeoDataDAL geoDataDAL) {
        this.geoDataDAL = geoDataDAL;
    }

    @Override
    public List<String> getSuggestions(String userInput) {
        return this.geoDataDAL.getAllAddress()
                .stream()
                .filter(address -> address.startsWith(userInput.toLowerCase()))
                .collect(Collectors.toList());
    }
}
