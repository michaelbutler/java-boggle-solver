package net.butlerpc.boggle;

import java.util.*;

public class Dictionary {

    public static final int MIN_WORD_LENGTH = 3;

    Map<String, Set<String>> dict;

    public Dictionary() {
        dict = new HashMap<>(3000);
    }

    // Run through all words in dictionary and build a map indexed by the first 3 characters of 
    // each word. Will skip words too short or that have missing letters in the boggle board
    public void mapWords(Scanner sc, GridBoard board) {
        String word, prefix;
        Set<Character> boggleCharSet = board.getLettersAsSet();

        while (sc.hasNextLine()) {
            word = sc.nextLine().toUpperCase();

            if (word.length() < MIN_WORD_LENGTH) {
                continue;
            }

            prefix = prefix(word);
            if (!boggleCharSet.containsAll(chars(word))) {
                // Skip this word because it couldn't possible exist anywhere on the board
                // due to missing letter(s)
                continue;
            }
            if (!dict.containsKey(prefix)) {
                dict.put(prefix, new TreeSet<String>());
            }
            dict.get(prefix).add(word);
        }
    }

    private Set<Character> chars(String word) {
        Set<Character> set = new TreeSet<>();
        for (char c : word.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    private String prefix(String word) {
        return word.substring(0, MIN_WORD_LENGTH);
    }

    public WordSearchResponse search(String word) {
        WordSearchResponse searchResponse = new WordSearchResponse(false, false);
        String prefix = prefix(word);
        if (!dict.containsKey(prefix)) {
            searchResponse.isPrefix = false;
            searchResponse.isWord = false;
            return searchResponse;
        }
        if (dict.get(prefix).contains(word)) {
            searchResponse.isWord = true;
            searchResponse.isPrefix = true;
            return searchResponse;
        }
        if (word.length() > MIN_WORD_LENGTH) {
           searchResponse.isPrefix = prefixExistsInList(dict.get(prefix), word);
        } else {
            searchResponse.isPrefix = true;
        }
        return searchResponse;
    }

    private boolean prefixExistsInList(Set<String> set, String prefix) {
        for (String s : set) {
            if (s.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
