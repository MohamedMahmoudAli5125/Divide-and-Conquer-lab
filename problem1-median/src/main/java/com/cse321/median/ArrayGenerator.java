package com.cse321.median;

import java.util.Random;

public class ArrayGenerator {

    private final Random random;

    public ArrayGenerator(long seed) {
        this.random = new Random(seed);
    }

    public int[] randomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }

    public static void main(String[] args){
        ArrayGenerator arrayGenerator = new ArrayGenerator(12345L);

        int[] testArray = arrayGenerator.randomArray(100000000);

        System.out.println("Generated Array size: " + testArray.length);
    }
}
