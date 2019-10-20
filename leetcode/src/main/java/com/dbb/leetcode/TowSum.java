package com.dbb.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素
 * <p>
 * 数据量少第三种最快，但是数据量一般的时候第一种反而最快
 *
 * @author tc
 * @date 2019-10-10
 */
public class TowSum {

    public static void main(String[] args) {
        //int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 32, 42, 53, 67, 79, 90, 100};
        int[] nums = {2, 7, 11, 15};
        int target = 22;
        TowSum towSum = new TowSum();

        long start = System.nanoTime();
        int[] r1 = towSum.twoSum1(nums, target);

        System.out.println("1 : " + (System.nanoTime() - start));

        for (int i : r1) {
            System.out.println(i);
        }

        start = System.nanoTime();
        int[] r2 = towSum.twoSum2(nums, target);

        System.out.println("2 : " + (System.nanoTime() - start));

        for (int i : r2) {
            System.out.println(i);
        }

        start = System.nanoTime();
        int[] r3 = towSum.twoSum3(nums, target);

        System.out.println("3 : " + (System.nanoTime() - start));

        for (int i : r3) {
            System.out.println(i);
        }
    }

    public int[] twoSum1(int[] nums, int target) {
        int length = nums.length;
        int sum;
        int[] result = new int[2];
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                sum = nums[i] + nums[j];
                if (sum == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }

        return result;
    }

    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < length; i++) {
            int other = target - nums[i];
            if (map.containsKey(other) && map.get(other) != i) {
                return new int[] {i, map.get(other)};
            }

        }

        return null;
    }

    public int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            int other = target - nums[i];
            if (map.containsKey(other)) {
                return new int[] {map.get(other), i};
            }

            map.put(nums[i], i);
        }

        return null;
    }

}
