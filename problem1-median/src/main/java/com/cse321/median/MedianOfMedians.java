package com.cse321.median;

import java.util.Arrays;

public class MedianOfMedians implements SelectAlgorithm {

    @Override
    public String name() {
        return "MedianOfMedians";
    }

    private int partitionAroundValue(int[] a, int left, int right, int pivotValue) {
        int pivotIndex = -1;
        // fimd the pivot element
        for (int j = left; j <= right; j++) {
            if (a[j] == pivotValue) {
                pivotIndex = j;
                break;
            }
        }
        // swap with the first element
        int temp = a[left];
        a[left] = a[pivotIndex];
        a[pivotIndex] = temp;

        int pivot = a[left];
        int i = left + 1;

        for (int j = left + 1; j <= right; j++) {
            if (a[j] < pivot) {
                temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
            }
        }
        temp = a[left];
        a[left] = a[i - 1];
        a[i - 1] = temp;
        return i - 1;
    }

    private int getMedianOfGroup(int[] a, int left, int right) {
        //naive approach at a constant time
        Arrays.sort(a, left, right + 1);
        return a[left + (right - left) / 2];
    }

    private int getMedianOfMedians(int[] a, int left, int right) {
        int n = right - left + 1;
        if (n <= 5) {
            return getMedianOfGroup(a, left, right);
        }

        int numOfGroups = (n / 5) + (n % 5 != 0 ? 1 : 0);
        int[] medians = new int[numOfGroups];

        for (int i = 0; i < numOfGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            medians[i] = getMedianOfGroup(a, groupLeft, groupRight);
        }

        return selectHelper(medians, 0, numOfGroups - 1, numOfGroups / 2); // the Recursive call ( median of medians )
    }

    private int selectHelper(int[] a, int left, int right, int k) {
        if (left == right) {
            return a[left];
        }
        int pivotValue = getMedianOfMedians(a, left, right); //divide the array into groups of 5 elements , get the median of each group in constant time and then recursively get the median of those medians
        int pivotIndex = partitionAroundValue(a, left, right, pivotValue);
        int i = pivotIndex - left + 1;
        if (i == k) {
            return pivotValue;
        }
        if (k < i) {
            return selectHelper(a, left, pivotIndex - 1, k);
        } else {
            return selectHelper(a, pivotIndex + 1, right, k - i);
        }
    }

    @Override
    public int select(int[] a, int k) {
        return selectHelper(a, 0, a.length - 1, k);

    }

    public static void main(String[] args) {
        MedianOfMedians medianOfMedians = new MedianOfMedians();

        int[] manualArray = {15, 3, 9, 1, 20, 8};
        int k1 = 3;

        System.out.println("Original Array: " + Arrays.toString(manualArray));
        int result1 = medianOfMedians.select(manualArray, k1);
        System.out.println(result1);
        System.out.println();

        ArrayGenerator generator = new ArrayGenerator(12345L);
        int[] generatedArray = generator.randomArray(10);
        int k2 = 5;

        System.out.println("Generated Array: " + Arrays.toString(generatedArray));

        int[] sortedCopy = generatedArray.clone();
        Arrays.sort(sortedCopy);

        int result2 = medianOfMedians.select(generatedArray, k2);
        System.out.println("Sorted Array   : " + Arrays.toString(sortedCopy));
        System.out.println(result2);
    }
}