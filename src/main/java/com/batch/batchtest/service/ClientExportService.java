package com.batch.batchtest.service;

import com.batch.batchtest.db.ClientDB;
import com.batch.batchtest.dto.ClientDto;
import com.batch.batchtest.repository.ClientRepository;
import com.batch.batchtest.util.FixedWidthUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.batch.batchtest.util.ValueUtil.getOrDefault;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientExportService {

    private final ClientRepository clientRepository;
    private final Validator validator;

    public Mono<Void> exportClientsToTxt(String filePath) {
        return clientRepository
                .findAllByActive(true)
                .map(this::mapDbToDto)
                .map(this::validateDto)
                .map(FixedWidthUtil::toFixedWidthLine)
                .collectList()
                .flatMap(lines -> writeToFile(filePath, lines));
    }

    private ClientDto mapDbToDto(ClientDB db) {
        String name = getOrDefault(db.getName(), "UNKNOWN");
        String phone = "912001001";
        String email = name.equals("UNKNOWN") ? "unknown@test.com" : name.replace(" ", "_") + "@test.com";

        return ClientDto.builder()
                .id(getOrDefault(db.getId(), 0L))
                .name(name)
                .email(email)
                .phone(phone)
                .salary(getOrDefault(db.getSalary(), 2, 0L))
                .birthday(db.getBirthday())
                .build();
    }

    private ClientDto validateDto(ClientDto dto) {
        Set<ConstraintViolation<ClientDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed for ClientDto: ");
            for (ConstraintViolation<ClientDto> v : violations) {
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
