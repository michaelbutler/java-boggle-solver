package net.butlerpc.boggle;

public class Main {

    public static final String DEFAULT_DICTIONARY = "dictionary.txt";

    public static void main(String[] args) {
        Solver s = new Solver();
        if (args.length >= 2) {
            s.loadDictionary(args[1]);
        } else {
            s.loadDictionary(DEFAULT_DICTIONARY);
        }
        if (args.length >= 1) {
            s.generateBoardFromString(args[0]);
        } else {
            s.generateRandomBoard();
        }
        s.run();
    }
}
