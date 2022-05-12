package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeetCodeN1 {

    private static int count = 0;
    private static int reqCount = 0;

    public static void main(String[] args) {
        //int[] nums = { 4,9,7,1,2,1,6,3,3,6,7,9,2 };

        int[] nums = {4,1,2,1,2};
        //int[] nums = {-1,-1,-2};
        singleNumber3(nums);
        System.out.println("result: " + count);
        if (count == 0) {
            for (int i = 0; i < nums.length; i++) {
                if (i == nums.length - 1) {
                    System.out.println("new result: " + nums[i]);
                    break;
                }
                if (nums[i] != nums[i + 1]) {
                    System.out.println("new result: " + nums[i]);
                    break;
                }
                i++;
            }
        }
    }

    public static int singleNumber1(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }

        int uniqCounter = 0;
        //var1
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    uniqCounter++;
                }
            }

            if (uniqCounter == 1) {
                return nums[i];
            }
            uniqCounter = 0;
        }
        return 0;
    }

    public static int singleNumber2(int[] nums) {
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));
        for (int i = 0; i < nums.length; i++) {
            if (i == nums.length - 1) {
                return nums[i];
            }
            if (nums[i] != nums[i + 1]) {
                return nums[i];
            }
            i++;
        }
        return 0;
    }

    // 4 1 2 1 2
    // 2 1 2 1 4
    public static int singleNumber3(int[] nums) {
        sort(nums, 0, nums.length - 1);
        return 0;
    }

    private static void sort(int[] nums, int initialLeftIndex, int initialRightIndex) {
        int middleIndex = (initialLeftIndex + initialRightIndex) / 2;
        int middleValue = nums[middleIndex];
        int leftIndex = initialLeftIndex;
        int rightIndex = initialRightIndex;

        //System.out.println("END initialLeftIndex: " + initialLeftIndex + " | rightIndex: " + rightIndex);

        if (initialLeftIndex >= initialRightIndex) {
            if (nums.length == 1) {
                count = nums[0];
            }
            return;
        }

        System.out.println(Arrays.toString(nums));

        while (leftIndex < rightIndex) {

            System.out.println(middleIndex + "|" + middleValue);

            while (nums[leftIndex] < middleValue) {
                /*if (leftIndex == middleIndex) {
                    System.out.println("left index stop: " + leftIndex);
                    break;
                }*/
                leftIndex++;
            }
            System.out.println("left index: " + leftIndex + " | value: " + nums[leftIndex]);

            while (nums[rightIndex] > middleValue) {
                /*if (rightIndex == middleIndex) {
                    System.out.println("right index stop: " + rightIndex);
                    break;
                }*/
                rightIndex--;
            }
            System.out.println("right index: " + rightIndex + " | value: " + nums[rightIndex]);

            int temp;
            if (leftIndex <= rightIndex) {
                temp = nums[rightIndex];
                nums[rightIndex] = nums[leftIndex];
                nums[leftIndex] = temp;
                leftIndex++;
                rightIndex--;
            }

            System.out.println(Arrays.toString(nums));
            System.out.println("____________________________________");
        }

        reqCount++;
        System.out.println("initialLeftIndex: " + initialLeftIndex + " | rightIndex: " + rightIndex);
        if (initialLeftIndex < rightIndex) {
            System.out.println("GOING TO LEFT");
            System.out.println("////////////recurs: " + reqCount);
            sort(nums, initialLeftIndex, rightIndex);
        }

        System.out.println("initialRightIndex: " + initialRightIndex + " | leftIndex: " + leftIndex);
        if (initialRightIndex > leftIndex) {
            System.out.println("GOING TO RIGHT");
            for (int i = 0; i < leftIndex; i++) {
                System.out.println("i: " + i + " | leftindex: " + leftIndex);

                if (i == nums.length-1) {
                    count = nums[i];
                    System.out.println("//////////////////////////////////SET COUNT1: " + count);
                    break;
                }

                if (i+1 != leftIndex) {
                    if (nums[i] != nums[i + 1]) {
                        count = nums[i];
                        System.out.println("//////////////////////////////////SET COUNT2: " + count);
                        return;
                    }
                }
                i++;

                /*System.out.println("//////////////i: " + i + " | nums length: " + nums.length);
                if (i == nums.length - 2) {
                    count = nums[i-1];
                    return;
                }*/
            }

            sort(nums, leftIndex, initialRightIndex);
        }
    }
}
