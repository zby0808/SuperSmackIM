package com.item.comm.util;

/**
 * Created by Fracesuit on 2017/8/18.
 */

public class StringUtils2 {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "null".equalsIgnoreCase(str);
    }
}
