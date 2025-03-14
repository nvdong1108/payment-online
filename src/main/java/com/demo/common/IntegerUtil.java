package com.demo.common;

public class IntegerUtil {
    public static Integer convertToInteger(Object object) {
        if(object == null) {
            return null;    
        }
        return Integer.parseInt(object.toString());
    }
}
