package com.batch.batchtest.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ValueUtilTest {
    @Test
    void testGetOrDefaultString() {
        assertEquals("abc", ValueUtil.getOrDefault("abc", "default"));
        assertEquals("default", ValueUtil.getOrDefault(null, "default"));
    }

    @Test
    void testGetOrDefaultDouble() {
        assertEquals(1.5, ValueUtil.getOrDefault(1.5, 2.0));
        assertEquals(2.0, ValueUtil.getOrDefault(null, 2.0));
    }

    @Test
    void testGetOrDefaultBigDecimal() {
        assertEquals(BigDecimal.valueOf(10), ValueUtil.getOrDefault(BigDecimal.valueOf(10), BigDecimal.valueOf(20)));
        assertEquals(BigDecimal.valueOf(20), ValueUtil.getOrDefault(null, BigDecimal.valueOf(20)));
    }

    @Test
    void testGetOrDefaultBoolean() {
        assertTrue(ValueUtil.getOrDefault(true, false));
        assertFalse(ValueUtil.getOrDefault(null, false));
    }

    @Test
    void testGetOrDefaultLong() {
        assertEquals(100L, ValueUtil.getOrDefault(100L, 200L));
        assertEquals(200L, ValueUtil.getOrDefault(null, 200L));
    }

    @Test
    void testGetOrDefaultInteger() {
        assertEquals(10, ValueUtil.getOrDefault(10, 20));
        assertEquals(20, ValueUtil.getOrDefault(null, 20));
    }

    @Test
    void testGetOrDefaultBigDecimalToLong() {
        BigDecimal value = BigDecimal.valueOf(2);
        long result = ValueUtil.getOrDefault(value, 1, 999L);
        assertEquals((long) (2 * Math.pow(1, 10)), result);
        assertEquals(999L, ValueUtil.getOrDefault(null, 1, 999L));
    }
}
