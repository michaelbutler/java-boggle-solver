package net.butlerpc.boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GridBoard board = GridBoard.getRandomBoggleBoard(4);

        if (args.length < 1) {
            System.out.println("Must provide dictionary input file as argument 1.");
            return;
        }

        Dictionary dictionary = loadDictionary(args[0]);
        dictionary.filter(board);

        GridTraveler gt = new GridTraveler(board, dictionary);
        List<String> words = gt.findWords();

        System.out.println(board);
        System.out.println("Found " + words.size()  + " words");
        for (String s : words) {
            System.out.print(s + " ");
        }
        System.out.println("");
    }

    private static Dictionary loadDictionary(String path) {
        Scanner sc;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary file not found or could not be opened.");
            throw new RuntimeException("Catastrophic failure");
        }
        List<String> lines = new ArrayList<String>(2048);
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        return new Dictionary(lines);
    }
}
