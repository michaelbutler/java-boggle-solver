package net.butlerpc.boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solver {

    public final int DEFAULT_BOARD_SIZE = 4;
    public final boolean ENABLE_MULTI_PROCESSING = true;

    private long startTimer;

    private GridBoard board;
    private Dictionary dict;
    private Set<String> wordMap;

    public void run() {
        startTimer();
        board = GridBoard.getRandomBoggleBoard(DEFAULT_BOARD_SIZE);
        System.out.println("Built boggleBoard in " + endTimer() + "s");

        startTimer();
        dict.filter(board);
        System.out.println("Processed dictionary in " + endTimer() + "s");

        List<Future> futuresList = new ArrayList<Future>();
        int numProcessors = Runtime.getRuntime().availableProcessors();
        if (!ENABLE_MULTI_PROCESSING) {
            numProcessors = 1;
        }
        ExecutorService eService = Executors.newFixedThreadPool(numProcessors);

        System.out.println("Num Processors: " + numProcessors);
        System.out.println("");

        startTimer();
        doParallelWordSearch(eService, futuresList);
        System.out.println("Solved boggle words in " + endTimer() + "s");

        System.out.println(board);
        System.out.println("Found " + wordMap.size()  + " words");
        for (String s : wordMap) {
            System.out.print(s + " ");
        }
        Solver.exit();
    }

    private void doParallelWordSearch(ExecutorService eService, List<Future> futuresList) {
        // Chunk up the work of searching for words, row by row
        for (int i = 0; i < board.size; i++) {
            BoggleSearchTask gt = new BoggleSearchTask(board, dict);
            // This effectively sets the chunk on which to work,
            // In this case we're creating separate tasks for each row of the board.
            gt.setRowToOperateOn(i);
            futuresList.add(eService.submit(gt));
        }

        wordMap = new TreeSet<String>();
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
    }

    private String endTimer() {
        DecimalFormat df = new DecimalFormat("#.###");
        double elapsed = (new java.util.Date().getTime() - startTimer) * 0.001;
        return df.format(elapsed);
    }

    private void startTimer() {
        startTimer = new java.util.Date().getTime();
    }

    public void setBoggleBoard(String spaceSeparatedString) {
        // @todo implement this
    }

    public void loadDictionary(String path) {
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
        dict = new Dictionary(lines);
    }

    public static void exit() {
        System.out.println("");
        System.exit(0);
    }
}
