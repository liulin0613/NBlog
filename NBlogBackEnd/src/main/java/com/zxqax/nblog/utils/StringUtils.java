package com.zxqax.nblog.utils;

public class StringUtils {
    // isEmpty
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    // isBlank
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    // isNotEmpty
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    // isNotBlank
    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }
}
