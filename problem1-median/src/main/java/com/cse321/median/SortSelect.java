package com.cse321.median;

import java.util.Arrays;

public class SortSelect implements SelectAlgorithm {

    @Override
    public String name() {
        return "Sort";
    }

    @Override
    public int select(int[] a, int k) {
        Arrays.sort(a);
        return a[k - 1];
    }


    public static void main(String[] args) {
        SortSelect sortSelect = new SortSelect();

        int[] manualArray = {15, 3, 9, 1, 20, 8};
        int k1 = 3;

        System.out.println(Arrays.toString(manualArray));
        int result1 = sortSelect.select(manualArray, k1);
        System.out.println(result1);

        ArrayGenerator generator = new ArrayGenerator(12345L);
        int[] generatedArray = generator.randomArray(10);
        int k2 = 5;
        System.out.println(Arrays.toString(generatedArray));

        int[] sortedCopy = generatedArray.clone();
        Arrays.sort(sortedCopy);
        int result2 = sortSelect.select(generatedArray, k2);
        System.out.println(Arrays.toString(sortedCopy));
        System.out.println(result2);
    }
}