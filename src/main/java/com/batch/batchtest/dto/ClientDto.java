package com.batch.batchtest.dto;

import com.batch.batchtest.util.FixedField;
import com.batch.batchtest.util.FixedField.Align;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ClientDto {

    @NotNull
    @FixedField(length = 5, padChar = '0', align = Align.RIGHT)
    private Long id;

    @NotEmpty
    @FixedField(length = 50, padChar = '_')
    private String name;

    @FixedField(length = 50, padChar = '_')
    private String email;

    @FixedField(length = 10, padChar = '_', align = Align.RIGHT)
    private String phone;

    @NotNull
    @FixedField(length = 7, padChar = '0', pattern = "%d", align = Align.RIGHT)
    private Long salary;

    @FixedField(length = 8, pattern = "yyyyMMdd")
    private LocalDate birthday;
}
