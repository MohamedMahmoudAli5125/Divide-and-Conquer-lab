package com.cse321.median;

import java.util.List;

public class BenchmarkRunner {
    private static final int JVM_WARMUP_RUNS = 30;
    private static final int JVM_WARMUP_SIZE = 100_000;
    private static final int PER_SIZE_WARMUP_RUNS = 2;

    private final List<SelectAlgorithm> algos;
    private final ArrayGenerator gen;

    public BenchmarkRunner(List<SelectAlgorithm> algos, ArrayGenerator gen) {
        this.algos = algos;
        this.gen = gen;
    }

    public void warmUpJvm() {
        for (int i = 0; i < JVM_WARMUP_RUNS; i++) {
            int[] base = gen.randomArray(JVM_WARMUP_SIZE);
            int k = (base.length + 1) / 2;
            for (SelectAlgorithm algo : algos) algo.select(base.clone(), k);
        }
    }

    public double[][] runSize(int n, int runs) {
        int k = (n + 1) / 2;
        double[][] times = new double[algos.size()][runs];

        int[] baseArray = gen.randomArray(n);
        int expected = expectedMedian(baseArray, k);

        for (int w = 0; w < PER_SIZE_WARMUP_RUNS; w++) {
            for (SelectAlgorithm algo : algos) algo.select(baseArray.clone(), k);
        }

        for (int a = 0; a < algos.size(); a++) {
            SelectAlgorithm algo = algos.get(a);

            settleGc();

            for (int r = 0; r < runs; r++) {
                int[] copy = baseArray.clone();

                long start = System.nanoTime();
                int result = algo.select(copy, k);
                long elapsed = System.nanoTime() - start;

                if (result != expected) {
                    throw new IllegalStateException(String.format(
                            "%s returned %d but correct answer is %d (n=%d, k=%d)",
                            algo.name(), result, expected, n, k));
                }

                times[a][r] = elapsed / 1_000_000.0;
            }
        }
        return times;
    }

    private int expectedMedian(int[] base, int k) {
        int[] sorted = base.clone();
        java.util.Arrays.sort(sorted);
        return sorted[k - 1];
    }

    private void settleGc() {
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}