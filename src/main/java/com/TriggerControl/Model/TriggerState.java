package com.TriggerControl.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerState {
    @Id
    @Column(name = "DEVICE_FINGERPRINT", unique = true, nullable = false)
    private String deviceFingerPrint;

    @Column(name = "DEVICE_NAME", length = 100)
    private String deviceName;

    @Column(name = "TRIGGER_STATE", nullable = false)
    private Boolean triggerState;

    @Column(name = "TRIGGER_INSTANCE", nullable = false)
    private LocalDateTime triggerInstance;
}
