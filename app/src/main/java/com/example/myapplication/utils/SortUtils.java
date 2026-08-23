package com.example.myapplication.utils;

/**
 * 排序算法工具类
 * 包含常见的排序算法实现，用于学习和参考
 */
public class SortUtils {

    /**
     * 冒泡排序
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     * 稳定排序
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // 如果一轮下来没有交换，说明已经有序
            if (!swapped) break;
        }
    }

    /**
     * 选择排序
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     * 不稳定排序
     */
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            // 交换最小元素到当前位置
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    /**
     * 快速排序
     * 时间复杂度：平均 O(nlogn)，最坏 O(n²)
     * 空间复杂度：O(logn)
     * 不稳定排序
     */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // 以最后一个元素为基准
        int i = low - 1; // 小于基准的区域边界

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // 把基准放到正确位置
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /**
     * 二分查找
     * 前提：数组必须有序
     * 时间复杂度：O(logn)
     * @return 目标值的索引，不存在返回 -1
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;

        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // 防止溢出
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 打印数组
     */
    public static void printArray(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }
}
