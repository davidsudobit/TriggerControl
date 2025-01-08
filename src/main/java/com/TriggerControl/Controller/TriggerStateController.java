package com.TriggerControl.Controller;

import com.TriggerControl.Model.TriggerState;
import com.TriggerControl.Service.TriggerStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/trigger")
public class TriggerStateController {

    @Autowired
    private TriggerStateService triggerStateService;

    @PostMapping("/new-device/{triggerState}")
    public ResponseEntity<TriggerState> addDevice(@RequestParam(name = "deviceName", required = false, defaultValue = "DefaultDevice") String deviceName, @PathVariable(name = "triggerState") Boolean triggerState) throws IOException {
        return ResponseEntity.ok(triggerStateService.createNewDevice(deviceName, triggerState));
    }

    @PutMapping("/trigger/{triggerState}")
    public ResponseEntity<TriggerState> trigger(@RequestParam(name = "deviceFingerprint") String deviceFingerprint, @PathVariable(name = "triggerState") Boolean triggerState) throws IOException {
        return ResponseEntity.ok(triggerStateService.changeTriggerState(deviceFingerprint, triggerState));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> commonExceptionHandler(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
