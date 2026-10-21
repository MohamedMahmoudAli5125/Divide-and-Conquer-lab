package com.cse321.median;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class BenchmarkReporter {
    private final PrintWriter summary;
    private final PrintWriter raw;
    private final Path outPath;

    public BenchmarkReporter(String outDir) throws IOException {
        this.outPath = Path.of(outDir);
        Files.createDirectories(outPath);

        this.summary = new PrintWriter(Files.newBufferedWriter(outPath.resolve("summary.csv")));
        this.raw = new PrintWriter(Files.newBufferedWriter(outPath.resolve("raw.csv")));

        summary.println("n,algorithm,runs,mean_ms,median_ms,stddev_ms,min_ms,max_ms");
        raw.println("n,algorithm,run,time_ms");
    }

    public void reportSize(int n, int runs, List<SelectAlgorithm> algos, double[][] times) {
        System.out.printf("%n== n = %,d  (%d runs) ==%n", n, runs);
        for (int a = 0; a < algos.size(); a++) {
            String algoName = algos.get(a).name();
            Stats stats = StatsCalculator.calculate(times[a]);

            writeSummaryCsv(n, algoName, runs, stats);
            writeRawCsv(n, algoName, runs, times[a]);
            printToConsole(algoName, stats);
        }
        summary.flush();
        raw.flush();
    }

    private void writeSummaryCsv(int n, String algoName, int runs, Stats s) {
        summary.printf(Locale.US, "%d,%s,%d,%.6f,%.6f,%.6f,%.6f,%.6f%n",
                n, algoName, runs, s.mean(), s.median(), s.stdDev(), s.min(), s.max());
    }

    private void writeRawCsv(int n, String algoName, int runs, double[] algoTimes) {
        for (int r = 0; r < runs; r++) {
            raw.printf(Locale.US, "%d,%s,%d,%.6f%n", n, algoName, r + 1, algoTimes[r]);
        }
    }

    private void printToConsole(String algoName, Stats s) {
        System.out.printf(Locale.US, "  %-17s mean %10.3f ms | median %10.3f ms | sd %9.3f ms%n",
                algoName, s.mean(), s.median(), s.stdDev());
    }

    public void printFinished() {
        System.out.println("\nDone. CSVs written to: " + outPath.toAbsolutePath());
    }

    public void closeFiles() {
        summary.close();
        raw.close();
    }
}