package com.batch.batchtest.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FixedWidthUtil {

    public static String toFixedWidthLine(Object dto) {
        StringBuilder sb = new StringBuilder();
        for (Field field : dto.getClass().getDeclaredFields()) {
            FixedField fw = field.getAnnotation(FixedField.class);
            if (fw != null) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(dto);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                String str = formatValue(value, field.getType(), fw.pattern());
                if (fw.align() == FixedField.Align.RIGHT) {
                    str = padLeft(str, fw.length(), fw.padChar());
                } else {
                    str = padRight(str, fw.length(), fw.padChar());
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    private static String formatValue(Object value, Class<?> type, String pattern) {
        if (value == null) return "";
        // BigDecimal
        if (type == BigDecimal.class) {
            if (pattern != null && !pattern.isEmpty()) {
                return String.format(pattern, ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP));
            }
            return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP).toPlainString();
        }
        // LocalDate
        if (type == LocalDate.class) {
            if (pattern != null && !pattern.isEmpty()) {
                return ((LocalDate) value).format(DateTimeFormatter.ofPattern(pattern));
            }
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        // Numbers
        if (type == Long.class
                || type == long.class
                || type == Integer.class
                || type == int.class
                || type == Double.class
                || type == double.class
                || Number.class.isAssignableFrom(type)) {
            if (pattern != null && !pattern.isEmpty()) {
                return String.format(pattern, value);
            }
            return value.toString();
        }
        // Others
        if (pattern != null && !pattern.isEmpty()) {
            return String.format(pattern, value);
        }
        return value.toString();
    }

    private static String padLeft(String value, int length, char padChar) {
        if (value == null) value = "";
        if (value.length() >= length) return value.substring(0, length);
        StringBuilder sb = new StringBuilder();
        for (int i = value.length(); i < length; i++) sb.append(padChar);
        sb.append(value);
        return sb.toString();
    }

    private static String padRight(String value, int length, char padChar) {
        if (value == null) value = "";
        if (value.length() >= length) return value.substring(0, length);
        StringBuilder sb = new StringBuilder(value);
        for (int i = value.length(); i < length; i++) sb.append(padChar);
        return sb.toString();
    }
}
