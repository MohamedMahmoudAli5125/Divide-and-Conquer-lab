package com.cse321.triangle;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;


public final class MinPerimeterTriangle {
    public static class Point {
        int x, y;
        boolean isLeft;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public MinPerimeterTriangle() {}

    public  double solve(Point[] arr) {
        Point[] X = SortbyX(arr);
        Point[] Y = SortbyY(arr);
        return recursive(X, Y, 0, X.length - 1);
    }

    public double recursive(Point[] X, Point[] Y, int left, int right) {
        int n = right - left + 1;

        // 1. Base Cases
        if (n == 3) {
            return getPerimeter(X[left], X[left + 1], X[left + 2]);
        }
        if (n < 3) {
            return Double.POSITIVE_INFINITY; // Impossible to form a triangle
        }

        // 2. Divide: Split points into left and right halves around the median line
        int midIndex = left + (right - left) / 2;
        for (int i = left; i <= midIndex; i++) {
            X[i].isLeft = true;
        }
        for (int i = midIndex + 1; i <= right; i++) {
            X[i].isLeft = false;
        }
        Point medianPoint = X[midIndex];

        int leftSize = midIndex - left + 1;
        int rightSize = right - midIndex;

        Point[] YL = new Point[leftSize];
        Point[] YR = new Point[rightSize];

        // Partition Y array while maintaining the Y-sorted order
        partition(Y, YL, YR);

        // 3. Conquer: Recursively find the minimum perimeter in both halves
        double leftPerimeter = recursive(X, YL, left, midIndex);
        double rightPerimeter = recursive(X, YR, midIndex + 1, right);
        double delta = Math.min(leftPerimeter, rightPerimeter);

        // 4. Merge Step (The Strip & Bounding Box)
        // Filter points within delta/2 width from the median line
        Point[] Ydash = getYinStrip(Y, medianPoint.x, delta);
        int m = Ydash.length ;

        // Theoretical Upper Bound & The Integer Grid Effect:
        // A (delta x delta/2) box holds max 16 points (2 per 8 sub-blocks).
        // Geometrically it's 12 points, and constrained by distinct points on an Integer Grid,
        // it shrinks to ~10 max points. limit = 15 acts as a safe margin against floating-point
        // inaccuracies, checking a constant O(1) number of points and ensuring overall O(n log n) time.
        int limit = 15;

        // Check triplets within our theoretical limit bound
        for (int i = 0; i < m; i++) {
            Point firstPoint = Ydash[i];

            int maxIndex = Math.min(i + limit, m-1);

            for (int j = i + 1; j <= maxIndex; j++) {
                Point secondPoint = Ydash[j];

                for (int k = j + 1; k <= maxIndex; k++) {
                    Point thirdPoint = Ydash[k];

                    double currentPerimeter = getPerimeter(firstPoint, secondPoint, thirdPoint);
                    if (currentPerimeter < delta) {
                        delta = currentPerimeter;
                    }
                }
            }
        }

        return delta;
    }


    // - SortbyX
    private Point[] SortbyX(Point[] X) {
        Point[] sortedArray = X.clone();
        Arrays.sort(sortedArray, Comparator.comparingInt((Point p) -> p.x).thenComparingInt(p -> p.y));
        return sortedArray;
    }

    // - SortbyY
    private Point[] SortbyY(Point[] Y) {
        Point[] sortedArray = Y.clone();
        Arrays.sort(sortedArray, Comparator.comparingInt(p -> p.y));
        return sortedArray;
    }
    // - partition
    private void partition(Point[] Y, Point[] YL, Point[] YR) {
        int ptr1 = 0;
        int ptr2 = 0;
        for (Point point : Y) {
            if (point.isLeft) {
                YL[ptr1++] = point;
            } else {
                YR[ptr2++] = point;
            }
        }
    }

    private double dist(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double getPerimeter(Point p1, Point p2, Point p3) {
        return dist(p1, p2) + dist(p2, p3) + dist(p3, p1);
    }
    // - getYinStrip

    private Point[] getYinStrip(Point[] Y, int medianPointX, double delta) {
        Point[] temp = new Point[Y.length];
        int count = 0;
        // Search width is delta/2 on both sides of the median line
        double half = delta / 2.0;

        for (Point point : Y) {
            double d = Math.abs(medianPointX - point.x);
            if (d <= half) {
                temp[count++] = point;
            }
        }

        return Arrays.copyOf(temp, count);
    }


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        MinPerimeterTriangle sol = new MinPerimeterTriangle();
        while (sc.hasNextInt()) {
            int n = sc.nextInt();

            MinPerimeterTriangle.Point[] arr = new MinPerimeterTriangle.Point[n];
            for (int i = 0; i < n; i++) {
                int x = sc.nextInt();
                int y = sc.nextInt();
                arr[i] = new MinPerimeterTriangle.Point(x, y);
            }

            double minPerimeter = sol.solve(arr);

            System.out.printf(Locale.US, "%.3f\n", minPerimeter);
        }

        sc.close();
    }

}