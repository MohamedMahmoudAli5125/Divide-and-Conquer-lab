package com.cse321.median;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class SelectTest {

    private final List<SelectAlgorithm> algos = List.of(new RandomizedSelect(), new MedianOfMedians());

//    Checks every k for the given array (the array is copied for each call).
    private void checkAllK(int[] input) {
        int[] sorted = input.clone();
        Arrays.sort(sorted);
        for (SelectAlgorithm algo : algos) {
            for (int k = 1; k <= input.length; k++) {
                int actual = algo.select(input.clone(), k);
                assertEquals(sorted[k - 1], actual,
                        algo.name() + " wrong for k=" + k + " on " + Arrays.toString(input));
            }
        }
    }

    @Test
    void singleElement() {
        checkAllK(new int[]{7});
    }

    @Test
    void twoAndThreeElements() {
        checkAllK(new int[]{2, 1});
        checkAllK(new int[]{1, 2});
        checkAllK(new int[]{3, 1, 2});
        checkAllK(new int[]{2, 2, 1});
    }

    @Test
    void allEqual() {
        int[] a = new int[50];
        Arrays.fill(a, 5);
        checkAllK(a);
    }

    @Test
    void sortedAndReverseSorted() {
        int n = 60;
        int[] up = new int[n];
        int[] down = new int[n];
        for (int i = 0; i < n; i++) {
            up[i] = i;
            down[i] = n - i;
        }
        checkAllK(up);
        checkAllK(down);
    }

    @Test
    void negativeAndExtremeValues() {
        checkAllK(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE, -1, 1, Integer.MIN_VALUE, Integer.MAX_VALUE});
    }

    @Test
    void manySmallRandomArrays() {
        ArrayGenerator gen = new ArrayGenerator(1);
        for (int n = 1; n <= 40; n++) {
            for (int rep = 0; rep < 5; rep++) {
                checkAllK(gen.randomArray(n));
            }
        }
    }

    @Test
    void manyDuplicates() {
        ArrayGenerator gen = new ArrayGenerator(2);
        for (int n : new int[]{10, 11, 25, 64, 101}) {
            checkAllK(gen.randomArray(n, 3));   // only values 0,1,2
            checkAllK(gen.randomArray(n, 10));
        }
    }

    @Test
    void mediansOfLargerRandomArrays() {
        ArrayGenerator gen = new ArrayGenerator(3);
        for (int n : new int[]{1_000, 4_999, 10_000, 100_001}) {
            int[] input = gen.randomArray(n);
            int[] sorted = input.clone();
            Arrays.sort(sorted);
            int k = (n + 1) / 2;
            for (SelectAlgorithm algo : algos) {
                assertEquals(sorted[k - 1], algo.select(input.clone(), k), algo.name() + " n=" + n);
            }
        }
    }

    @Test
    void largeArrayWithDuplicatesDoesNotBlowUp() {
        // catches O(n^2) partitioning / stack overflow on equal keys
        ArrayGenerator gen = new ArrayGenerator(4);
        int[] input = gen.randomArray(1_000_000, 5);
        int[] sorted = input.clone();
        Arrays.sort(sorted);
        int k = (input.length + 1) / 2;
        for (SelectAlgorithm algo : algos) {
            assertEquals(sorted[k - 1], algo.select(input.clone(), k), algo.name());
        }
    }

    @Test
    @org.junit.jupiter.api.DisplayName("Test partition toggle logic to ensure 50/50 split for duplicates")
    void testPartitionToggleLogic() throws Exception {
        RandomizedSelect rs = new RandomizedSelect();

        int[] arr = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5};

        java.lang.reflect.Method partitionMethod = RandomizedSelect.class.getDeclaredMethod("partition", int[].class, int.class, int.class);
        partitionMethod.setAccessible(true);

        int pivotIndex = (int) partitionMethod.invoke(rs, arr, 0, arr.length - 1);

        org.junit.jupiter.api.Assertions.assertTrue(
                pivotIndex >= 3 && pivotIndex <= 6,
                "Toggle logic failed! It didn't split the array evenly. Pivot index returned: " + pivotIndex
        );
    }
}
