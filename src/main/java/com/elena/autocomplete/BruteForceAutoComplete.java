package com.elena.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BruteForceAutoComplete implements AutoComplete {

    private List<String> suggestions = new ArrayList<>();

    @Override
    public void addSuggestion(String suggestion) {
        this.suggestions.add(suggestion);
    }

    @Override
    public void buildSuggestions(Collection<String> suggestions) {
        this.suggestions.addAll(suggestions);
    }

    @Override
    public List<String> getSuggestions(String userInput) {
        return this.suggestions
                .stream()
                .filter(address -> address.startsWith(userInput)).collect(Collectors.toList());
    }
}
