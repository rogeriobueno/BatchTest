package com.batch.batchtest.util;

import java.math.BigDecimal;

public class ValueUtil {
    public static String getOrDefault(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static Double getOrDefault(Double value, Double defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static BigDecimal getOrDefault(BigDecimal value, BigDecimal defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static Long getOrDefault(BigDecimal value, long decimal, long defaultValue) {
        return value != null
                ? value.multiply(BigDecimal.valueOf(Math.pow(decimal, 10L))).longValue()
                : defaultValue;
    }

    public static Boolean getOrDefault(Boolean value, Boolean defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static Long getOrDefault(Long value, Long defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static Integer getOrDefault(Integer value, Integer defaultValue) {
        return value != null ? value : defaultValue;
    }
}
