package com.batch.batchtest.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedWidthUtilTest {

    static class TestDto {
        @FixedField(length = 5, padChar = '0', align = FixedField.Align.RIGHT)
        Long id;

        @FixedField(length = 10, align = FixedField.Align.LEFT)
        String name;

        @FixedField(length = 8, padChar = '0', align = FixedField.Align.RIGHT)
        Long value;

        @FixedField(length = 8, pattern = "yyyyMMdd", align = FixedField.Align.LEFT)
        LocalDate date;

        @FixedField(length = 4, padChar = 'X', align = FixedField.Align.RIGHT)
        Integer number;
    }

    @Test
    void testToFixedWidthLine_AllFieldsFilled() {
        TestDto dto = new TestDto();
        dto.id = 42L;
        dto.name = "Ana";
        dto.value = ValueUtil.getOrDefault(BigDecimal.valueOf(12.5), 2, 0L);
        dto.date = LocalDate.of(2025, 9, 27);
        dto.number = 7;
        String result = FixedWidthUtil.toFixedWidthLine(dto);
        // id: right, 5, pad '0' => "00042"
        // name: left, 10, pad '_' => "Ana_______"
        // value: right, 8, pad '0', pattern %.2f => "00012.50"
        // date: left, 8, pattern yyyyMMdd => "20250927"
        // number: right, 4, pad 'X' => "XXX7"
        assertEquals("00042Ana       0000125020250927XXX7", result);
    }

    @Test
    void testToFixedWidthLine_NullFields() {
        TestDto dto = new TestDto();
        dto.id = null;
        dto.name = null;
        dto.value = null;
        dto.date = null;
        dto.number = null;
        String result = FixedWidthUtil.toFixedWidthLine(dto);
        assertEquals("00000          00000000        XXXX", result);
    }

    @Test
    void testToFixedWidthLine_Truncation() {
        TestDto dto = new TestDto();
        dto.id = 1234567L; // will be truncated to 5 digits
        dto.name = "ABCDEFGHIJKLJGKHG"; // will be truncated to 10 chars
        dto.value = ValueUtil.getOrDefault(BigDecimal.valueOf(32.323), 2, 0L); // will be truncated to 8 chars
        dto.date = LocalDate.of(2025, 12, 31); // will be truncated to 8 chars
        dto.number = 12125; // will be truncated to 4 chars
        String result = FixedWidthUtil.toFixedWidthLine(dto);
        assertEquals("12345ABCDEFGHIJ00003232202512311212", result);
    }
}
