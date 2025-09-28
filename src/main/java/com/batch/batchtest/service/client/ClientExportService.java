package com.batch.batchtest.service.client;

import com.batch.batchtest.db.ClientDB;
import com.batch.batchtest.dto.ClientDTO;
import com.batch.batchtest.repository.ClientRepository;
import com.batch.batchtest.service.GenericExportService;
import com.batch.batchtest.util.FixedWidthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.batch.batchtest.util.ValueUtil.getOrDefault;

@RequiredArgsConstructor
@Service
public class ClientExportService {

    private final ClientRepository clientRepository;
    private final GenericExportService<ClientDB, ClientDTO> genericExportService;

    public Mono<Void> exportClientsToTxt(String filePath) {
        return genericExportService.exportToTxt(
                clientRepository.findAllByActive(true), this::mapDbToDto, FixedWidthUtil::toFixedWidthLine, filePath);
    }

    private ClientDTO mapDbToDto(ClientDB db) {
        String UNKNOWN = "UNKNOWN";
        String name = getOrDefault(db.getName(), UNKNOWN);
        String phone = "912001001";
        String email = getOrDefault(db.getName(), UNKNOWN).replace(" ", "_") + "@test.com";

        return ClientDTO.builder()
                .id(getOrDefault(db.getId(), 0L))
                .name(name)
                .email(email)
                .phone(phone)
                .salary(getOrDefault(db.getSalary(), 2, 0L))
                .birthday(db.getBirthday())
                .build();
    }
}
