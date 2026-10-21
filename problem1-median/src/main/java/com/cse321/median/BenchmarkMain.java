package com.cse321.median;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BenchmarkMain {

    private static final int[] DEFAULT_SIZES = {
            1_000, 5_000, 10_000, 50_000, 100_000, 500_000,
            1_000_000, 2_000_000, 5_000_000, 10_000_000
    };

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Benchmark Configuration ===");
        long seed =  42L ;

        System.out.print("Enter Max Size [Press Enter for default: All sizes]: ");
        String maxSizeInput = scanner.nextLine().trim();
        int maxSize = maxSizeInput.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxSizeInput);

        System.out.print("Enter Runs (-1 for Auto) [Press Enter for default: -1]: ");
        String runsInput = scanner.nextLine().trim();
        int runsOverride = runsInput.isEmpty() ? -1 : Integer.parseInt(runsInput);

        String outDir =  "problem1-median/results" ;

        System.out.println("\nStarting Benchmark with -> "+
                " | Max Size: " + (maxSize == Integer.MAX_VALUE ? "ALL" : maxSize) +
                " | Runs: " + (runsOverride == -1 ? "AUTO" : runsOverride) +
                " | Output Dir: " + outDir + "\n");

        scanner.close();

        List<SelectAlgorithm> algos = List.of(
                new RandomizedSelect(),
                new MedianOfMedians(),
                new SortSelect());

        ArrayGenerator gen = new ArrayGenerator(seed);
        BenchmarkRunner runner = new BenchmarkRunner(algos, gen);

        System.out.println("== JVM warm-up ==");
        runner.warmUpJvm();
        BenchmarkReporter reporter = new BenchmarkReporter(outDir);
            for (int n : DEFAULT_SIZES) {
                if (n > maxSize) break;

                int runs = runsOverride > 0 ? runsOverride : defaultRuns(n);

                double[][] times = runner.runSize(n, runs);

                reporter.reportSize(n, runs, algos, times);
            }
            reporter.printFinished();
        reporter.closeFiles();

    }

    private static int defaultRuns(int n) {
        if (n <= 100_000) return 100;
        if (n <= 1_000_000) return 40;
        if (n <= 5_000_000) return 20;
        return 10;
    }
}