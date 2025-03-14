package com.demo.common;

public class StringUtil {
    
    public static String converToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
