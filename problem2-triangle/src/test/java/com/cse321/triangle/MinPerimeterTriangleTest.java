package com.cse321.triangle;

import com.cse321.triangle.MinPerimeterTriangle.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MinPerimeterTriangleTest {

    private static final double EPS = 1e-9;
    private final MinPerimeterTriangle sol = new MinPerimeterTriangle();

    private static Point[] pts(int... xy) {
        Point[] p = new Point[xy.length / 2];
        for (int i = 0; i < p.length; i++) p[i] = new Point(xy[2 * i], xy[2 * i + 1]);
        return p;
    }

    private static double bruteForce(Point[] a) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                for (int k = j + 1; k < a.length; k++) {
                    double d1 = Math.hypot(a[i].x - a[j].x, a[i].y - a[j].y);
                    double d2 = Math.hypot(a[j].x - a[k].x, a[j].y - a[k].y);
                    double d3 = Math.hypot(a[k].x - a[i].x, a[k].y - a[i].y);
                    best = Math.min(best, d1 + d2 + d3);
                }
        return best;
    }

    private void assertSolves(double expected, Point[] pts) {
        assertEquals(expected, sol.solve(pts), 1e-6);
    }

    private void assertMatchesBrute(Point[] pts) {
        assertEquals(bruteForce(pts), sol.solve(pts), EPS, "Algorithm failed to match Brute Force!");
    }


    @Test
    @DisplayName("Tests Base Cases & Problem Sample")
    void basicSamples() {
        assertSolves(4.0, pts(0, 0, 1, 0, 2, 0, 1, 3));
        assertSolves(24.0, pts(0, 0, 6, 0, 0, 8)); // 6-8-10 Right Triangle = 24
    }

    @Test
    @DisplayName("Forces the algorithm to rely ONLY on the Strip (Merge Step)")
    void forcesStripExecution() {
        // 4 points split into 2 left & 2 right (Base cases return Infinity)
        // The algorithm MUST find the answer in the strip.
        assertMatchesBrute(pts(0, 0, 10, 0, 0, 10, 10, 10));
    }

    @Test
    @DisplayName("Best triangle spans across the median line (Divide and Conquer Core)")
    void crossesTheMedianLine() {
        Point[] p = pts(-2000, 500, -1000, -1000, 5, 0, 5, 1, 6, 0, 3000, 3000);
        assertSolves(2 + Math.sqrt(2), p);
    }

    @Test
    @DisplayName("Tests the 15-point limit logic using a dense grid")
    void denseGrid_TestsLimit15() {
        List<Point> l = new ArrayList<>();
        for (int x = 0; x < 20; x++)
            for (int y = 0; y < 20; y++)
                l.add(new Point(x, y));

        assertSolves(2 + Math.sqrt(2), l.toArray(new Point[0]));
    }

    @Test
    @DisplayName("Worst geometric case: All points are collinear")
    void collinearPoints() {
        Point[] p = new Point[1000];
        for (int i = 0; i < p.length; i++) p[i] = new Point(i * 3, 7);
        assertSolves(12.0, p);
    }

    @Test
    @DisplayName("Fuzz Testing: Random points vs Brute Force")
    void fuzzTestVsBruteForce() {
        Random r = new Random(42);

        for (int it = 0; it < 500; it++) {
            int n = 3 + r.nextInt(40);
            Set<Long> seen = new HashSet<>();
            Point[] p = new Point[n];
            int c = 0;

            while (c < n) {
                int x = r.nextInt(21) - 10;
                int y = r.nextInt(21) - 10;
                if (seen.add(x * 4_000_003L + y)) {
                    p[c++] = new Point(x, y);
                }
            }
            assertMatchesBrute(p);
        }
    }
}