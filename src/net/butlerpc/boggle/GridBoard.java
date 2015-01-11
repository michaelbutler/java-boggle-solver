package net.butlerpc.boggle;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GridBoard {

    public static final char VISITED_CHAR = '0';

    public char[][] dice;
    public int size;

    protected GridBoard(int size) {
        this.size = size;
        dice = new char[size][size];
    }

    public static GridBoard generateBoard(String[] rows) {
        int size = rows[0].length();
        GridBoard g = new GridBoard(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                g.dice[i][j] = rows[i].trim().charAt(j);
            }
        }
        return g;
    }

    public static GridBoard getRandomBoggleBoard(int size) {
        GridBoard g = new GridBoard(size);
        g.randomizeBoard();
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

    public Set<Character> getLettersAsSet() {
        Set<Character> output = new TreeSet<>();
        int i, j;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                output.add(dice[i][j]);
            }
        }
        return output;
    }

    public boolean isVisited(int x, int y) {
        try {
            return this.dice[x][y] == GridBoard.VISITED_CHAR;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    public void markAsVisited(int x, int y) {
        this.dice[x][y] = VISITED_CHAR;
    }

    /**
     * Special case for 'Q'... actually means "QU"
     */
    public String getStringAt(int x, int y) {
        char c = this.dice[x][y];
        if (c == 'Q') {
            return "QU";
        }
        return "" + c;
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
    void randomizeBoard() {
        int i, j;
        List<Character> dieList = DiceGenerator.getRandomDice(size * size);
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                dice[i][j] = dieList.remove(0);
            }
        }
    }
}
