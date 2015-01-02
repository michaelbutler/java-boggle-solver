package net.butlerpc.boggle;

import java.util.Arrays;
import java.util.List;

public class GridBoard {
    public char[][] dice;
    public int size = 4;

    protected GridBoard(int size) {
        this.size = size;
        dice = new char[size][size];
    }

    public static GridBoard getRandomBoggleBoard(int size) {
        GridBoard g = new GridBoard(size);
        g.initializeBoard();
        return g;
    }

    public GridBoard getClone() {
        GridBoard cloneVersion = new GridBoard(size);
        cloneVersion.dice = clone2dArray(dice);
        return cloneVersion;
    }

    public char[][] clone2dArray(char[][] source) {
        char[][] clone = new char[size][size];
        for (int i = 0; i < size; i++) {
            clone[i] = Arrays.copyOf(source[i], size);
        }
        return clone;
    }

    public String getLettersAsString() {
        String output = "";
        int i, j;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                output += dice[i][j];
            }
        }
        return output;
    }

    public String toString() {
        String output = "";
        int i, j;
        for (i = 0; i < size; i++) {
            output += "[";
            for (j = 0; j < size; j++) {
                output += "" + dice[i][j] + "   ";
            }
            output = output.trim();
            output += "]\n";
        }
        return output;
    }

    /**
     * Gets a random list of dice and assigns them to the grid squares
     */
    void initializeBoard() {
        int i, j;
        List<Character> dieList = DiceGenerator.getRandomDice(size * size);
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                dice[i][j] = dieList.remove(0);
            }
        }
    }
}
