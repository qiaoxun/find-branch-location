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

    /**
     * if the str contains the word lookingFor
     * @param str
     * @param lookingFor
     * @return
     */
    public static boolean contains(String str, String lookingFor) {
        if (isBlank(str)) {
            return false;
        }
        return str.contains(lookingFor);
    }
}
