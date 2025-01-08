package com.TriggerControl.Service;

import com.TriggerControl.Model.TriggerState;
import com.TriggerControl.Repository.TriggerStateRepository;
import com.TriggerControl.Util.RequestState;
import com.TriggerControl.Util.TriggerStateLogWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TriggerStateService {

    @Autowired
    private TriggerStateRepository triggerStateRepository;

    @Autowired
    private TriggerStateLogWriter triggerStateLogWriter;

    public TriggerState createNewDevice(String deviceName, Boolean triggerState) throws IOException {
        TriggerState newDeviceTriggerState = new TriggerState(
                UUID.nameUUIDFromBytes(UUID.randomUUID().toString().concat(deviceName).getBytes()).toString(),
                deviceName,
                triggerState,
                LocalDateTime.now()
        );

        if(newDeviceTriggerState.getDeviceName().equals("DefaultDevice")){
            newDeviceTriggerState.setDeviceName(newDeviceTriggerState.getDeviceName()
                    .concat("-")
                    .concat(newDeviceTriggerState.getDeviceFingerPrint().substring(0, 10)));
        }

        newDeviceTriggerState = triggerStateRepository.save(newDeviceTriggerState);

        this.triggerStateLogWriter.addEntry(newDeviceTriggerState, RequestState.CREATE);

        if(newDeviceTriggerState.getDeviceName()!=null){
            throw new IOException("Test exception: " + newDeviceTriggerState);
        }

        return newDeviceTriggerState;
    }

    public TriggerState changeTriggerState(String deviceFingerprint, Boolean triggerState) throws NoSuchElementException, IOException {
        TriggerState deviceTriggerState = triggerStateRepository.findById(deviceFingerprint).orElseThrow(()->new NoSuchElementException("Device with Fingerprint not found"));
        deviceTriggerState.setTriggerState(triggerState);
        deviceTriggerState.setTriggerInstance(LocalDateTime.now());

        deviceTriggerState = triggerStateRepository.save(deviceTriggerState);

        try{
            triggerStateLogWriter.addEntry(deviceTriggerState, RequestState.UPDATE);
        } catch (NoSuchElementException e){
            throw new NoSuchElementException("Log file not found");
        } catch (IOException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }

        return deviceTriggerState;
    }

}
