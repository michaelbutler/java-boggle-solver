package net.butlerpc.boggle;

import org.apache.commons.cli.*;

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
    public final String DEFAULT_DICTIONARY = "dictionary.txt";

    private long startTimer;

    private GridBoard board;
    private Dictionary dict;
    private Set<String> wordMap;
    private CommandLine inputCommand;
    private boolean useMultiProcessing = false;

    public void setOptions(String[] args) {
        Options options = new Options();
        options.addOption("s", true, "Set the grid size (NxN) of the Boggle board");
        options.addOption("d", true, "Path to dictionary file");
        options.addOption("b", true, "Board layout to solve [abcd,efgh,ijkl,mnyz]");
        options.addOption("multi", false, "Use parallel processing (multiple CPUs)");
        CommandLineParser parser = new GnuParser();
        try {
            inputCommand = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.print("Invalid program arguments: " + e);
            exit();
        }

        if (inputCommand.hasOption("multi")) {
            useMultiProcessing = true;
        }

        // Grid Size Option
        if (inputCommand.hasOption("s")) {
            generateRandomBoard(inputCommand.getOptionValue("s"));
        } else if (inputCommand.hasOption("b")) {
            generateBoardFromString(inputCommand.getOptionValue("b"));
        } else {
            generateRandomBoard(DEFAULT_BOARD_SIZE);
        }

        // Dictionary Option
        if (inputCommand.hasOption("d")) {
            loadDictionary(inputCommand.getOptionValue("d"));
        } else {
            loadDictionary(DEFAULT_DICTIONARY);
        }
    }

    public void run() {
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
        if (!useMultiProcessing) {
            numProcessors = 1;
        }
        ExecutorService eService = Executors.newFixedThreadPool(numProcessors);
        System.out.println("Num Processors: " + numProcessors);
        System.out.println("");
        return eService;
    }

    private void combineWords(List<Future> futuresList) {
        wordMap = new TreeSet<>();
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
        String[] rows = commaSeparatedString.toUpperCase().split(",");
        board = GridBoard.generateBoard(rows);
    }

    public void generateRandomBoard(String size) {
        generateRandomBoard(Integer.parseInt(size, 10));
    }

    public void generateRandomBoard(int size) {
        startTimer();
        board = GridBoard.getRandomBoggleBoard(size);
        System.out.println("Built boggleBoard in " + endTimer() + "s");
    }

    public void loadDictionary(String path) {
        Scanner sc;
        startTimer();
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Catastrophic failure: Dictionary file not found or could not be opened.");
        }
        dict = new Dictionary();
        dict.mapWords(sc, board);
        sc.close();
        System.out.println("Filtered dictionary words in " + endTimer() + "s");
    }

    public static void exit() {
        System.out.println("");
        System.exit(0);
    }
}
