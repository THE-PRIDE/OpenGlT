package com.mengyu.retrofitUtils.tools;

public class StringUtils {

    /**
     * 判断string是否为空
     *
     * @param str 待判定的str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str) || "".equals(str.trim())) {
            return true;
        }
        return false;
    }
}
