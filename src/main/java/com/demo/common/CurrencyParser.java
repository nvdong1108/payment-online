package com.demo.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyParser {

    public static String extractNumericValue(String currencyString) {
        if (currencyString == null || currencyString.isEmpty()) {
            return "0.00";
        }

        // Sử dụng regex để giữ lại chỉ số và dấu chấm
        Pattern pattern = Pattern.compile("[^0-9.]");
        Matcher matcher = pattern.matcher(currencyString);
        String numericValue = matcher.replaceAll("");

        // Kiểm tra nếu không có số nào
        if (numericValue.isEmpty()) {
            return "0.00";
        }

        // Kiểm tra nếu có nhiều dấu chấm, chỉ giữ lại dấu chấm đầu tiên
        int firstDotIndex = numericValue.indexOf('.');
        if (firstDotIndex != -1) {
            int lastDotIndex = numericValue.lastIndexOf('.');
            if (firstDotIndex != lastDotIndex) {
                numericValue = numericValue.substring(0, firstDotIndex + 1) +
                        numericValue.substring(firstDotIndex + 1).replace(".", "");
            }
        }

        return numericValue;
    }

}
