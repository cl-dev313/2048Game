package com.example.myapplication.utils;

/**
 * 数学工具类
 * 常用的数学计算方法
 */
public class MathUtils {

    /**
     * 判断是否是质数
     */
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * 计算阶乘
     */
    public static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("n 不能为负数");
        if (n <= 1) return 1;

        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * 斐波那契数列（第 n 项）
     * F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2)
     */
    public static long fibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException("n 不能为负数");
        if (n <= 1) return n;

        long prev2 = 0;
        long prev1 = 1;
        long current = 0;

        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return current;
    }

    /**
     * 求最大公约数（欧几里得算法）
     */
    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * 求最小公倍数
     */
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a * b) / gcd(a, b);
    }

    /**
     * 判断是否是回文数
     * 例如：121、12321 是回文数
     */
    public static boolean isPalindrome(int n) {
        if (n < 0) return false;
        int original = n;
        int reversed = 0;

        while (n > 0) {
            reversed = reversed * 10 + n % 10;
            n /= 10;
        }
        return original == reversed;
    }

    /**
     * 反转数字
     */
    public static int reverseNumber(int n) {
        int reversed = 0;
        boolean negative = n < 0;
        n = Math.abs(n);

        while (n > 0) {
            reversed = reversed * 10 + n % 10;
            n /= 10;
        }
        return negative ? -reversed : reversed;
    }

    /**
     * 计算数组的和
     */
    public static int sum(int[] arr) {
        int sum = 0;
        for (int num : arr) sum += num;
        return sum;
    }

    /**
     * 求数组最大值
     */
    public static int max(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        int max = arr[0];
        for (int num : arr) {
            if (num > max) max = num;
        }
        return max;
    }

    /**
     * 求数组最小值
     */
    public static int min(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        int min = arr[0];
        for (int num : arr) {
            if (num < min) min = num;
        }
        return min;
    }

    /**
     * 计算平均值
     */
    public static double average(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        return (double) sum(arr) / arr.length;
    }

    /**
     * 生成随机数 [min, max]
     */
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
