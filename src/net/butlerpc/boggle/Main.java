package net.butlerpc.boggle;

public class Main {

    public static void main(String[] args) {
        validateArguments(args);
        Solver s = new Solver();
        s.loadDictionary(args[0]);
        if (args.length >= 2) {
            s.setBoggleBoard(args[1]);
        }
        s.run();
    }

    private static void validateArguments(String[] args) {
        if (args.length < 1) {
            System.out.println("Must provide dictionary input file as argument 1.");
            System.exit(0);
        }
    }
}
