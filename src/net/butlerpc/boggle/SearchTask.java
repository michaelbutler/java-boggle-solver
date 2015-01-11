package net.butlerpc.boggle;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

public class SearchTask implements Callable {

    public GridBoard board;

    private int traverseCalls = 0;
    private int rowToOperateOn;
    private Set<String> foundWords;
    private Dictionary dict;


    public SearchTask(GridBoard board, Dictionary dict) {
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
        foundWords = new TreeSet<>();
        /**
         * For each square of the grid, run this pseudo code:
         * 0. Start with empty word: ""
         * 1. I'm on a square. Add me as a letter to the letter chain, add current letter chain to the wordList.
         * 2. For each adjacent untouched square, perform 1.
         */

        for (int y = 0; y < board.size; y++) {
            traverseCalls = 0;
            traverseBoard(board, "", rowToOperateOn, y);
        }

        return foundWords;
    }

    protected void traverseBoard(GridBoard grid, String letterChain, int x, int y) {
        traverseCalls += 1;
        /**
         * Exit early if the algorithm is too inefficient
         */
        if (traverseCalls > 999999) {
            System.out.println("Inefficient algorithm detected. traverseBoard called " + traverseCalls + " times.");
            Solver.exit();
        }
        if (grid.isVisited(x, y)) {
            return;
        }

        GridBoard clonedBoard = grid.getClone();

        // Concat the current letter to the letter chain
        String touchedChar = grid.getStringAt(x, y);
        letterChain = letterChain + touchedChar;

        clonedBoard.markAsVisited(x, y);

        if (letterChain.length() >= Dictionary.MIN_WORD_LENGTH) {
            WordSearchResponse w = dict.search(letterChain);
            if (!w.isPrefix) {
                // Stop traversing if we'll never find a match with the current chain
                return;
            }
            if (w.isWord) {
                foundWords.add(letterChain);
            }
        }

        // Traverse all adjacent squares, if possible
        traverseBoard(clonedBoard, letterChain, x + 1, y);
        traverseBoard(clonedBoard, letterChain, x, y + 1);
        traverseBoard(clonedBoard, letterChain, x + 1, y + 1);
        traverseBoard(clonedBoard, letterChain, x, y - 1);
        traverseBoard(clonedBoard, letterChain, x + 1, y - 1);
        traverseBoard(clonedBoard, letterChain, x - 1, y);
        traverseBoard(clonedBoard, letterChain, x - 1, y + 1);
        traverseBoard(clonedBoard, letterChain, x - 1, y - 1);
    }
}
