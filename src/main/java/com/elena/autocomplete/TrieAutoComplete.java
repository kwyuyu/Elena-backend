package com.elena.autocomplete;

import java.util.*;

public class TrieAutoComplete implements AutoComplete {

    private TrieNode root = new TrieNode();


    private class TrieNode {

        public Map<Character, TrieNode> children = new HashMap<>();
        public String word = null;

    }

    @Override
    public void buildSuggestions(Collection<String> suggestions) {
        for (String suggestion: suggestions) {
            this.addSuggestion(suggestion);
        }
    }

    @Override
    public void addSuggestion(String suggestion) {
        TrieNode cur = this.root;
        for (char c: suggestion.toCharArray()) {
            if (!cur.children.containsKey(c)) {
                cur.children.put(c, new TrieNode());
            }
            cur = cur.children.get(c);
        }
        cur.word = suggestion;
    }

    private TrieNode startWith(String userInput) {
        char[] userInputCharacters = userInput.toCharArray();

        if (!this.root.children.containsKey(userInputCharacters[0])) {
            return null;
        }

        TrieNode cur = this.root;
        for (char c: userInputCharacters) {
            if (!cur.children.containsKey(c)) {
                break;
            }
            cur = cur.children.get(c);
        }
        return cur;
    }

    private void collectWord(TrieNode foundTrie, List<String> suggestions) {
        if (foundTrie.word != null) {
            suggestions.add(foundTrie.word);
        }

        if (foundTrie.children.isEmpty()) {
            return;
        }

        for (char c: foundTrie.children.keySet()) {
            this.collectWord(foundTrie.children.get(c), suggestions);
        }
    }

    @Override
    public List<String> getSuggestions(String userInput) {
        TrieNode foundTrie = this.startWith(userInput);

        if (foundTrie == null) {
            return new ArrayList<>();
        }

        List<String> suggestions = new ArrayList<>();
        this.collectWord(foundTrie, suggestions);
        return suggestions;
    }
}
