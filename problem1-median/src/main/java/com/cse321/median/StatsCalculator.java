package com.cse321.median;

import java.util.Arrays;

public class StatsCalculator {

    public static Stats calculate(double[] times) {
        int n = times.length;

        double sum = 0;
        for (double t : times) {
            sum += t;
        }
        double mean = sum / n;

        double sq = 0;
        for (double t : times) {
            sq += (t - mean) * (t - mean);
        }
        double stdDev = n > 1 ? Math.sqrt(sq / (n - 1)) : 0.0;

        double[] sorted = times.clone();
        Arrays.sort(sorted);

        double median = (n % 2 == 1) ? sorted[n / 2] : (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
        double min = sorted[0];
        double max = sorted[n - 1];

        return new Stats(mean, median, stdDev, min, max);
    }
}