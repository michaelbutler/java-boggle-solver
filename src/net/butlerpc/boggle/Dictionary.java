package net.butlerpc.boggle;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    List<String> dict;

    public Dictionary(List<String> dict) {
        this.dict = dict;
    }

    /**
     * Filter out dictionary words that would be impossible to spell, given the current grid board
     */
    public void filter(GridBoard board) {
        String boggleBoard = board.getLettersAsString();
        List<String> newList = new ArrayList<String>(1000);
        int wordsRemoved = 0;
        boolean remove;
        for (String s : dict) {
            remove = false;
            for (char l : s.toCharArray()) {
                if (!boggleBoard.contains("" + l)) {
                    remove = true;
                    wordsRemoved++;
                    break;
                }
            }
            if (!remove) {
                newList.add(s);
            }
        }
        dict = newList;
        System.out.println("Removed " + wordsRemoved + " words from dictionary.");
    }

    /**
     * @todo use a faster search mechanism
     */
    public WordSearchResponse search(String word) {
        WordSearchResponse searchResponse = new WordSearchResponse(false, false);
        for (String s : dict) {
            if (s.equals(word)) {
                searchResponse.isWord = true;
                searchResponse.isPrefix = true;
                break;
            }
            if (s.startsWith(word)) {
                searchResponse.isPrefix = true;
            }
            // dict is alphabetized, so we can exit early if we pass over the letter that begins search word
            if (s.charAt(0) > word.charAt(0)) {
                break;
            }
        }
        return searchResponse;
    }
}
