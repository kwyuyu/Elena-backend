package com.elena.autocomplete;

import java.util.Collection;
import java.util.List;

public interface AutoComplete {

    public void addSuggestion(String suggestion);
    public void buildSuggestions(Collection<String> suggestions);
    public List<String> getSuggestions(String userInput);

}
