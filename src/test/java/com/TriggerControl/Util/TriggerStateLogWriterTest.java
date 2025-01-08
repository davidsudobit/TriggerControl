package com.TriggerControl.Util;

import com.TriggerControl.Model.TriggerState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
@ActiveProfiles("test")
public class TriggerStateLogWriterTest {

    @Value("${TRIGGER_LOG_FILE_PATH}")
    private String FILE_PATH;

    @Autowired
    private TriggerStateLogWriter triggerStateLogWriter;

    @Test
    public void testAddEntry() throws IOException {
        // Arrange
        LocalDateTime testDateTime = LocalDateTime.now();
        TriggerState triggerState = new TriggerState("deviceFingerprint", "deviceName", true, testDateTime);
        RequestState requestState = RequestState.CREATE;

        // Act
        triggerStateLogWriter.addEntry(triggerState, requestState);

        // Assert
        File logFile = new File(FILE_PATH);
        assertTrue(logFile.exists());
        String logContent = new String(Files.readAllBytes(logFile.toPath()));
        assertTrue(logContent.contains("DEVICE_FINGERPRINT: deviceFingerprint"));
        assertTrue(logContent.contains("DEVICE_NAME: deviceName"));
        assertTrue(logContent.contains("TRIGGER_STATE: true"));
        assertTrue(logContent.contains("TRIGGER_INSTANCE: " + testDateTime.format(DateTimeFormatter.ISO_DATE_TIME)));
        assertTrue(logContent.contains("TRIGGER_REQUEST_STATE: CREATE"));
    }
}