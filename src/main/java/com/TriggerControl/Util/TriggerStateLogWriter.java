package com.TriggerControl.Util;

import com.TriggerControl.Model.TriggerState;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

@Component
public class TriggerStateLogWriter {

    @Value("${TRIGGER_LOG_FILE_PATH}")
    private String FILE_PATH;
    private static BufferedWriter writer;

    @PostConstruct
    private void postConstruct() throws IOException {
        TriggerStateLogWriter.writer = this.createFileIfNotExists();
    }

    public void addEntry(TriggerState deviceTriggerState, RequestState requestState) throws IOException {
        try {
            this.writeToFile(deviceTriggerState, requestState);
        } catch (IOException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }
    }

    private BufferedWriter createFileIfNotExists() throws IOException {

        Path filePath = Paths.get(FILE_PATH);

        if(Files.exists(filePath)){
            return this.getBufferedWriter(filePath);
        }

        return this.getBufferedWriter(Files.createFile(filePath));

    }

    private BufferedWriter getBufferedWriter(Path path) throws IOException {
        return Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.SYNC);
    }

    private void writeToFile(TriggerState deviceTriggerState, RequestState requestState) throws IOException {
        TriggerStateLogWriter.writer.append(String.format("DEVICE_FINGERPRINT: %s, DEVICE_NAME: %s, TRIGGER_STATE: %s, TRIGGER_INSTANCE: %s, TRIGGER_REQUEST_STATE: %s\n", deviceTriggerState.getDeviceFingerPrint(), deviceTriggerState.getDeviceName(), deviceTriggerState.getTriggerState(), deviceTriggerState.getTriggerInstance().format(DateTimeFormatter.ISO_DATE_TIME), requestState.getState()));
        TriggerStateLogWriter.writer.flush();
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        TriggerStateLogWriter.writer.append("\n--------------------- ABOVE LOGS - INVALIDATED --------------------\n\n");
        writer.flush();
        writer.close();
    }

}
