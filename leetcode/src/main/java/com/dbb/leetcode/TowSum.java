package com.dbb.leetcode;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素
 *
 * @author tc
 * @date 2019-10-10
 */
public class TowNumSum {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 7, 32, 42, 77, 90};
        int target = 79;
        int[] ints = new TowNumSum().twoSum(nums, target);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    public int[] twoSum(int[] nums, int target) {
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

}
