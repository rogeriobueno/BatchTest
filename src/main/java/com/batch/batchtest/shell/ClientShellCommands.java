package com.batch.batchtest.shell;

import com.batch.batchtest.service.client.ClientExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Profile("!test")
@ShellComponent
@RequiredArgsConstructor
public class ClientShellCommands {

    private final ClientExportService clientExportService;

    @ShellMethod(key = "client", value = "Export client to text file at /tmp/test.txt with fixed columns.")
    public String exportClientFile() {
        try {
            clientExportService.exportClientsToTxt("/tmp/test.txt").block();
            return "File generated with success: ";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
