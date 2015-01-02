package net.butlerpc.boggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridTraveler {
    public GridBoard board;
    Map<String, Integer> foundWords;
    int traverseCalls = 0;
    Dictionary dict;

    public final int MIN_WORD_LENGTH = 3;
    public final char VISITED_CHAR = '0';

    public GridTraveler(GridBoard board, Dictionary dict) {
        this.board = board;
        this.dict = dict;
    }

    public List<String> findWords() {
        foundWords = new HashMap<String, Integer>(100, 100);
        List<String> uniqueWords;

        /**
         * For each square of the grid, run this pseudo code: (so for a 4x4 grid, run total 16 times).
         * Pseudo-code:
         * 0. Start with empty word: ""
         * 1. I'm on a square. Add me as a letter to the letter chain, add current letter chain to the wordList.
         * 2. For each adjacent untouched square, perform 1.
         */
        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                GridBoard clonedBoard = board.getClone();
                traverseBoard(clonedBoard, "", i, j);
            }
        }

        uniqueWords = new ArrayList<String>(foundWords.size());
        uniqueWords.addAll(foundWords.keySet());
        return uniqueWords;
    }

    protected void traverseBoard(GridBoard grid, String letterChain, int x, int y) {
        traverseCalls += 1;
        /**
         * Exit early if the algorithm is too garbage
         */
        if (traverseCalls > 999999) {
            System.out.println("Catastrophic failure. traverseBoard called " + traverseCalls + " times.");
            System.exit(0);
        }
        if (x < 0 || y < 0 || x >= board.size || y >= board.size || grid.dice[x][y] == VISITED_CHAR) {
            return;
        }

        // Concat the current letter to the letter chain
        letterChain = letterChain + grid.dice[x][y];

        // Mark as visited
        grid.dice[x][y] = VISITED_CHAR;

        if (letterChain.length() >= MIN_WORD_LENGTH) {
            WordSearchResponse w = dict.search(letterChain);
            if (w.isWord) {
                foundWords.put(letterChain, 1);
            } else if (!w.isPrefix) {
                // Stop traversing if we'll never find a match with the current chain
                return;
            }
        }

        // Traverse all adjacent squares, if possible
        if (x < board.size) {
            traverseBoard(grid.getClone(), letterChain, x + 1, y);
        }
        if (y < board.size) {
            traverseBoard(grid.getClone(), letterChain, x, y + 1);
            if (x < board.size) {
                traverseBoard(grid.getClone(), letterChain, x + 1, y + 1);
            }
        }
        if (y > 0) {
            traverseBoard(grid.getClone(), letterChain, x, y - 1);
            if (x < board.size) {
                traverseBoard(grid.getClone(), letterChain, x + 1, y - 1);
            }
        }
        if (x > 0) {
            traverseBoard(grid.getClone(), letterChain, x - 1, y);
            if (y < board.size) {
                traverseBoard(grid.getClone(), letterChain, x - 1, y + 1);
            }
            if (y > 0) {
                traverseBoard(grid.getClone(), letterChain, x - 1, y - 1);
            }
        }
    }
}
