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
                try {
                    field.setAccessible(true);
                    Object value = field.get(dto);
                    String str = formatValue(value, field.getType(), fw.pattern());
                    str = alignAndPad(str, fw.length(), fw.padChar(), fw.align());
                    sb.append(str);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sb.toString();
    }

    private static String formatValue(Object value, Class<?> type, String pattern) {
        if (value == null) return "";
        if (type == BigDecimal.class) {
            BigDecimal bd = ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP);
            return (pattern != null && !pattern.isEmpty()) ? String.format(pattern, bd) : bd.toPlainString();
        }
        if (type == LocalDate.class) {
            DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern((pattern != null && !pattern.isEmpty()) ? pattern : "yyyy-MM-dd");
            return ((LocalDate) value).format(fmt);
        }
        return (pattern != null && !pattern.isEmpty()) ? String.format(pattern, value) : value.toString();
    }

    private static String alignAndPad(String value, int length, char padChar, FixedField.Align align) {
        if (value == null) value = "";
        if (value.length() > length) return value.substring(0, length);
        int padLen = length - value.length();
        String pad = String.valueOf(padChar).repeat(padLen);
        return align == FixedField.Align.RIGHT ? pad + value : value + pad;
    }
}
