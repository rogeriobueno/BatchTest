package com.batch.batchtest.service.client;

import com.batch.batchtest.db.ClientDB;
import com.batch.batchtest.repository.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientExportServiceItTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientExportService clientExportService;

    private Path tempFilePath;

    @BeforeEach
    void setup() throws IOException {
        tempFilePath = Files.createTempFile("test-client-export", ".txt");
        clientRepository.deleteAll().block();
        ClientDB client = new ClientDB();
        //        client.setId(1L);
        client.setName("Test User");
        client.setSalary(BigDecimal.valueOf(1234));
        client.setBirthday(LocalDate.of(1990, 1, 1));
        client.setActive(true);
        clientRepository.save(client).block();
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(tempFilePath);
        clientRepository.deleteAll().block();
    }

    @Test
    void testExportClientsToTxt_GeneratesFileWithContent() throws IOException {
        StepVerifier.create(clientExportService.exportClientsToTxt(tempFilePath.toString()))
                .verifyComplete();
        assertTrue(Files.exists(tempFilePath), "Arquivo de exportação não foi gerado");
        String content = Files.readString(tempFilePath);
        String expectedContent = "00001"
                + String.format("%-50s", "Test User")
                + String.format("%-50s", "Test_User@test.com")
                + String.format("%10s", "912001001")
                + String.format("%05d00", 1234)
                + "19900101";
        assertEquals(expectedContent, content.trim(), "Conteúdo do arquivo exportado não corresponde ao esperado");
    }
}
