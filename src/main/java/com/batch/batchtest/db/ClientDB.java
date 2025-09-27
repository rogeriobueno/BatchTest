package com.batch.batchtest.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("client")
public class ClientDB {
    @Id
    private Long id;

    private String name;
    private BigDecimal salary;
    private LocalDate birthday;
    private Boolean active;
}
