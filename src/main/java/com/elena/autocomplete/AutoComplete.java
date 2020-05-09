package com.elena.autocomplete;

import com.elena.repository.GeoDataDAL;

import java.util.Collection;
import java.util.List;

public interface AutoComplete {

    public void setGeoDataDAL(GeoDataDAL geoDataDAL);
    public List<String> getSuggestions(String userInput);

}
