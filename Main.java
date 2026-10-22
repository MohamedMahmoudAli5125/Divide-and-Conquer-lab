//package com.cse321.triangle;
//
//import java.awt.*;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.io.StreamTokenizer;
//import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Locale;
//import java.util.Scanner;
//
//
//public class Main {
//
//    public static void main(String[] args) throws IOException {
//        Scanner sc = new Scanner(System.in);
//
//        MinPerimeterTriangle sol = new MinPerimeterTriangle();
//        while (sc.hasNextInt()) {
//            int n = sc.nextInt();
//
//            MinPerimeterTriangle.Point[] arr = new MinPerimeterTriangle.Point[n];
//            for (int i = 0; i < n; i++) {
//                int x = sc.nextInt();
//                int y = sc.nextInt();
//                arr[i] = new MinPerimeterTriangle.Point(x, y);
//            }
//
//            double minPerimeter = sol.solve(arr);
//
//            System.out.printf(Locale.US, "%.3f\n", minPerimeter);
//        }
//
//        sc.close();
//    }
//    }
//
//
//
//
//
