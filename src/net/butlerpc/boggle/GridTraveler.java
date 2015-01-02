package net.butlerpc.boggle;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

public class GridTraveler implements Callable {
    public GridBoard board;
    public int traverseCalls = 0;

    private int rowToOperateOn;
    private Set<String> foundWords;
    private Dictionary dict;

    public final int MIN_WORD_LENGTH = 3;
    public final char VISITED_CHAR = '0';

    public GridTraveler(GridBoard board, Dictionary dict) {
        this.board = board;
        this.dict = dict;
    }

    public void setRowToOperateOn(int i) {
        rowToOperateOn = i;
    }

    public Set call() {
        return findWords();
    }

    public Set findWords() {
        foundWords = new TreeSet<String>();
        /**
         * For each square of the grid, run this pseudo code: (so for a 4x4 grid, run total 16 times).
         * Pseudo-code:
         * 0. Start with empty word: ""
         * 1. I'm on a square. Add me as a letter to the letter chain, add current letter chain to the wordList.
         * 2. For each adjacent untouched square, perform 1.
         */

        for (int j = 0; j < board.size; j++) {
            GridBoard clonedBoard = board.getClone();
            traverseBoard(clonedBoard, "", rowToOperateOn, j);
        }

        return foundWords;
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
        // Special case for 'Q'... change to QU
        String touchedChar;
        if (grid.dice[x][y] == 'Q') {
            touchedChar = "QU";
        } else {
            touchedChar = "" + grid.dice[x][y];
        }
        letterChain = letterChain + touchedChar;

        // Mark as visited
        grid.dice[x][y] = VISITED_CHAR;

        if (letterChain.length() >= MIN_WORD_LENGTH) {
            WordSearchResponse w = dict.search(letterChain);
            if (w.isWord) {
                foundWords.add(letterChain);
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
