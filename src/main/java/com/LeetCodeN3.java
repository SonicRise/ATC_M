package com;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LeetCodeN3 {
    public static void main(String[] args) {

        int[] test = {7, 1, 5, 3, 6, 4};
        int[] tesd = {2, 3, 4, 5, 7, 1, 5, 6};
        int[] tes1 = {1, 2};
        int[] tes2 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] tes3 = {2, 1, 4};

        System.out.println("size " + tes2.length);
        //int i = 0;
        /*while (i != tes2.length-2 || tes2[i] >= tes2[i+1]) {
            System.out.println("skip " + i);
            i++;
        }*/
/*
        for (int i = 0; i < tes2.length; i++) {
            if (tes2[i] >= tes2[i+1]) {
                System.out.println("skip " + i);
            }
        }*/
        System.out.println(maxProfit(tes2));
    }

    public static int maxProfit(int[] prices) {

        int result = 0;
        System.out.println("prices size: " + prices.length);

        for (int i = 0; i < prices.length; i++) {
            System.out.println("i: " + i + " price: " + prices[i]);
            for (int j = i; j < prices.length; j++) {
                System.out.println("j: " + j + " price: " + prices[j]);
                if (prices[i] > prices[j] || j == prices.length - 1) {
                    if (j - i == 1 && j != prices.length - 1) {
                        i = findNewIndex(prices, i) - 1;
                        System.out.println("search new index: " + i);
                        break;
                    }

                    int max = findMax(prices, i, j);
                    System.out.println("//max: " + max);
                    if (max - prices[i] > result) {
                        result = max - prices[i];
                    } else {
                        System.out.println("less or equal result");
                    }
                    System.out.println("//result: " + result);

                    if (j == prices.length - 1) {
                        i = j;
                    } else {
                        i = j-1;
                    }
                    break;
                } else {
                    System.out.println("1 less or equal 2");
                }
            }
        }

        System.out.println(result);

        return 0;
    }

    private static int findMax(int[] prices, int start, int end) {
        int max = prices[start];
        for (int i = start; i <= end; i++) {
            if (max < prices[i]) {
                max = prices[i];
            }
        }
        return max;
    }

    private static int findNewIndex(int[] prices, int start) {
        //1, 2
        for (int i = start; i < prices.length; i++) {
            if (i == prices.length - 1) {
                return i;
            }

            if (prices[i] < prices[i+1]) {
                return i;
            }
        }
        return -100;
    }
}
