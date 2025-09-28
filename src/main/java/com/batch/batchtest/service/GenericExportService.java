package com.batch.batchtest.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenericExportService<ENTITY, DTO> {

    private final Validator validator;

    public Mono<Void> exportToTxt(
            Flux<ENTITY> entities,
            Function<ENTITY, DTO> entityToDto,
            Function<DTO, String> dtoToLine,
            String filePath) {
        return entities.map(entityToDto)
                .map(this::validateDto)
                .map(dtoToLine)
                .collectList()
                .flatMap(lines -> writeToFile(filePath, lines));
    }

    private DTO validateDto(DTO dto) {
        Set<ConstraintViolation<DTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<DTO> v : violations) {
                sb.append(v.getPropertyPath())
                        .append(" ")
                        .append(v.getMessage())
                        .append("; ");
            }
            throw new RuntimeException(sb.toString());
        }
        return dto;
    }

    private Mono<Void> writeToFile(String filePath, List<String> lines) {
        return Mono.fromRunnable(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error on write file ", e);
            }
        });
    }
}
