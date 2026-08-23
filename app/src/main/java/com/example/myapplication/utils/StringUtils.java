package com.example.myapplication.utils;

/**
 * 字符串工具类
 * 常用的字符串处理方法
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否为空（包括空格）
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) return true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反转字符串
     */
    public static String reverse(String str) {
        if (isEmpty(str)) return str;
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 判断是否是回文字符串
     */
    public static boolean isPalindrome(String str) {
        if (isEmpty(str)) return true;
        int left = 0;
        int right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * 统计字符出现次数
     */
    public static int countChar(String str, char c) {
        if (isEmpty(str)) return 0;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) count++;
        }
        return count;
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) return str;
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 截取字符串，超出部分用省略号代替
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }

    /**
     * 字符串重复 n 次
     */
    public static String repeat(String str, int n) {
        if (isEmpty(str) || n <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 隐藏手机号中间四位
     */
    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 7) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 隐藏邮箱中间部分
     */
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) return email;
        int atIndex = email.indexOf("@");
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (username.length() <= 2) {
            return username.charAt(0) + "*" + domain;
        }
        return username.charAt(0) + repeat("*", username.length() - 2)
                + username.charAt(username.length() - 1) + domain;
    }

    /**
     * 数字转中文大写（简单版）
     * 支持 0-99999
     */
    public static String numberToChinese(int num) {
        if (num < 0 || num > 99999) {
            return "超出范围";
        }
        if (num == 0) return "零";

        String[] digits = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] units = {"", "十", "百", "千", "万"};

        StringBuilder result = new StringBuilder();
        boolean zeroFlag = false;
        int unitIndex = 0;

        while (num > 0) {
            int digit = num % 10;
            if (digit == 0) {
                if (!zeroFlag && result.length() > 0) {
                    result.insert(0, digits[0]);
                }
                zeroFlag = true;
            } else {
                result.insert(0, digits[digit] + units[unitIndex]);
                zeroFlag = false;
            }
            num /= 10;
            unitIndex++;
        }

        // 处理 "一十" -> "十"
        if (result.toString().startsWith("一十")) {
            result.deleteCharAt(0);
        }

        return result.toString();
    }
}
