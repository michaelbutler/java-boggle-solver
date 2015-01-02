package net.butlerpc.boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        Double secs;
        long begTest = new java.util.Date().getTime();
        GridBoard board = GridBoard.getRandomBoggleBoard(4);

        secs = (new java.util.Date().getTime() - begTest) * 0.001;
        System.out.println("Built boggleBoard in " + secs + "s");

        if (args.length < 1) {
            System.out.println("Must provide dictionary input file as argument 1.");
            return;
        }

        begTest = new java.util.Date().getTime();

        Dictionary dictionary = loadDictionary(args[0]);
        dictionary.filter(board);

        secs = (new java.util.Date().getTime() - begTest) * 0.001;

        System.out.println("Processed dictionary in " + secs + "s");

        List<Future> futuresList = new ArrayList<Future>();
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService eService = Executors.newFixedThreadPool(numProcessors);

        System.out.println("Num Processors: " + numProcessors);
        System.out.println("");

        begTest = new java.util.Date().getTime();

        // Chunk up the work of searching for words, row by row
        for (int i = 0; i < board.size; i++) {
            GridTraveler gt = new GridTraveler(board, dictionary);
            gt.setRowToOperateOn(i);
            futuresList.add(eService.submit(gt));
        }

        Set<String> wordMap = new TreeSet<String>();
        for (Future future : futuresList) {
            try {
                // get the word map chunk
                wordMap.addAll((Set)future.get());
            } catch (InterruptedException e) {
                System.out.println("Interrupted process early.");
            } catch (ExecutionException e) {
                System.out.println("Execution of process failed.");
            }
        }

        secs = (new java.util.Date().getTime() - begTest) * 0.001;
        System.out.println("Solved boggle words in " + secs + "s");

        System.out.println(board);
        System.out.println("Found " + wordMap.size()  + " words");
        for (String s : wordMap) {
            System.out.print(s + " ");
        }
        System.out.println("");
        System.exit(0);
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
