package net.butlerpc.boggle;

public class Main {

    public static void main(String[] args) {
        Solver s = new Solver();
        s.setOptions(args);
        s.run();
    }
}
