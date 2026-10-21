package com.cse321.median;

import java.util.Arrays;
import java.util.Random;
public class RandomizedSelect implements SelectAlgorithm {

    @Override
    public String name() {
        return "RandomizedSelect";
    }
    private Random random = new Random();

    private int partition(int[] arr, int low, int high) {

        int randomIndex = low + random.nextInt(high - low + 1);
        // Swap pivot to start
        int temp = arr[low];
        arr[low] = arr[randomIndex];
        arr[randomIndex] = temp;


        int pivot = arr[low];
        int i = low + 1;

        for (int j = low + 1; j <= high; j++) {
            if (arr[j] < pivot) {
                // Swap arr[i] and arr[j]
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
            }
        }

        temp = arr[low];
        arr[low] = arr[i - 1];
        arr[i - 1] = temp;

        return i - 1;
    }
    public int selectHelper(int[] a ,int p ,int q ,int k ){
        if (p==q){
            return a[p];
        }
        int r = partition(a , p , q );
        int i  = r - p + 1 ;
        if (i == k){
            return a[r] ;
        }
        if (k < i){
            return selectHelper(a , p , r-1 , k);
        }
        else{
            return selectHelper(a,r+1, q , k - i );
        }

    }
    @Override
    public int select(int[] a, int k) {
        return selectHelper(a,0,a.length-1,k);
    }

    public static void main(String[] args) {
        RandomizedSelect randomizedSelect = new RandomizedSelect();

        int[] manualArray = {15, 3, 9, 1, 20, 8};
        int k1 = 3;

        System.out.println(Arrays.toString(manualArray));
        int result1 = randomizedSelect.select(manualArray, k1);
        System.out.println(result1);

        ArrayGenerator generator = new ArrayGenerator(12345L);
        int[] generatedArray = generator.randomArray(10);
        int k2 = 5;
        System.out.println(Arrays.toString(generatedArray));

        int[] sortedCopy = generatedArray.clone();
        Arrays.sort(sortedCopy);

        int result2 = randomizedSelect.select(generatedArray, k2);
        System.out.println("Array after Run: "+Arrays.toString(generatedArray));

        System.out.println("Sorted Copy: "+Arrays.toString(sortedCopy));
        System.out.println(result2);
    }
}
