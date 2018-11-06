package com.tools.utils;

public class StringUtil {
    /**
     * if a str is blank or not
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (null == str || "".equals(str))
            return true;
        return false;
    }
}
