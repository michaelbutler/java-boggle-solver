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
        dict.filter(board);
        System.out.println("Filtered dictionary words in " + endTimer() + "s");

        startTimer();
        List<Future> futuresList = doParallelWordSearch();
        combineWords(futuresList);
        System.out.println("Found all words in " + endTimer() + "s");

        System.out.println(board);
        System.out.println("Found " + wordMap.size()  + " words");
        for (String s : wordMap) {
            System.out.print(s + " ");
        }
        Solver.exit();
    }

    /**
     * Submits tasks to an ExecutorService
     */
    private List<Future> doParallelWordSearch() {
        ExecutorService eService = getExecutorService();
        List<Future> futuresList = new ArrayList<Future>();

        // Chunk up the work of searching for words, row by row
        for (int i = 0; i < board.size; i++) {
            SearchTask gt = new SearchTask(board, dict);
            // This effectively sets the chunk on which to work,
            // In this case we're creating separate tasks for each row of the board.
            // Probably the most effective way to split up the work would be to divide the board
            //   by the number of available processors. If only 1 processor available, we should
            //   ideally not split up the board at all.
            gt.setRowToOperateOn(i);
            futuresList.add(eService.submit(gt));
        }

        return futuresList;
    }

    private ExecutorService getExecutorService() {
        int numProcessors = Runtime.getRuntime().availableProcessors();
        if (!ENABLE_MULTI_PROCESSING) {
            numProcessors = 1;
        }
        ExecutorService eService = Executors.newFixedThreadPool(numProcessors);
        System.out.println("Num Processors: " + numProcessors);
        System.out.println("");
        return eService;
    }

    private void combineWords(List<Future> futuresList) {
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

    /**
     * Given a string like "ABCD,EFGH,IJKL...." turn it into a boggle board
     */
    public void generateBoardFromString(String commaSeparatedString) {
        String[] rows = commaSeparatedString.split(",");
        board = GridBoard.generateBoard(rows);
    }

    public void generateRandomBoard() {
        startTimer();
        board = GridBoard.getRandomBoggleBoard(DEFAULT_BOARD_SIZE);
        System.out.println("Built boggleBoard in " + endTimer() + "s");
    }

    public void loadDictionary(String path) {
        Scanner sc;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Catastrophic failure: Dictionary file not found or could not be opened.");
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
