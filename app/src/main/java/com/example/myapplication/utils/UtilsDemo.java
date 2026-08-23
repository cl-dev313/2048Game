package com.example.myapplication.utils;

/**
 * 工具类使用示例 Demo
 * 演示各个工具类的基本用法
 */
public class UtilsDemo {

    public static void main(String[] args) {
        System.out.println("===== SortUtils 排序演示 =====");
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        System.out.print("原数组：");
        SortUtils.printArray(arr);

        int[] arr1 = arr.clone();
        SortUtils.bubbleSort(arr1);
        System.out.print("冒泡排序：");
        SortUtils.printArray(arr1);

        int[] arr2 = arr.clone();
        SortUtils.selectionSort(arr2);
        System.out.print("选择排序：");
        SortUtils.printArray(arr2);

        int[] arr3 = arr.clone();
        SortUtils.quickSort(arr3);
        System.out.print("快速排序：");
        SortUtils.printArray(arr3);

        int target = 25;
        int index = SortUtils.binarySearch(arr3, target);
        System.out.println("二分查找 " + target + " 的索引：" + index);

        System.out.println("\n===== MathUtils 数学演示 =====");
        System.out.println("17 是质数吗？" + MathUtils.isPrime(17));
        System.out.println("5 的阶乘：" + MathUtils.factorial(5));
        System.out.println("第 10 个斐波那契数：" + MathUtils.fibonacci(10));
        System.out.println("48 和 18 的最大公约数：" + MathUtils.gcd(48, 18));
        System.out.println("12 和 15 的最小公倍数：" + MathUtils.lcm(12, 15));
        System.out.println("12321 是回文数吗？" + MathUtils.isPalindrome(12321));
        System.out.println("数组和：" + MathUtils.sum(arr));
        System.out.println("数组最大值：" + MathUtils.max(arr));
        System.out.println("数组最小值：" + MathUtils.min(arr));
        System.out.println("数组平均值：" + MathUtils.average(arr));

        System.out.println("\n===== DateUtils 日期演示 =====");
        System.out.println("当前日期：" + DateUtils.getCurrentDate(DateUtils.FORMAT_YMD));
        System.out.println("当前时间：" + DateUtils.getCurrentDateTime());
        System.out.println("今天星期几：" + DateUtils.getWeekDayCN(new java.util.Date()));
        System.out.println("今天是今年第几天：" + DateUtils.getDayOfYear());

        System.out.println("\n===== StringUtils 字符串演示 =====");
        String str = "Hello World";
        System.out.println("原字符串：" + str);
        System.out.println("反转：" + StringUtils.reverse(str));
        System.out.println("首字母大写：" + StringUtils.capitalize("hello"));
        System.out.println("'abcba' 是回文吗？" + StringUtils.isPalindrome("abcba"));
        System.out.println("手机号脱敏：" + StringUtils.maskPhone("13812345678"));
        System.out.println("邮箱脱敏：" + StringUtils.maskEmail("zhangsan@qq.com"));
        System.out.println("数字 123 转中文：" + StringUtils.numberToChinese(123));
        System.out.println("数字 2048 转中文：" + StringUtils.numberToChinese(2048));

        System.out.println("\n===== 演示结束 =====");
    }
}
