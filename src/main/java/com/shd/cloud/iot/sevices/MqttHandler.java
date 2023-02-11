package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.models.SensorHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttHandler {
    private final SensorService sensorService;
    private final OperatorService operatorService;

    public void handleMessage(String topic,String payload){
        // head/token/deviceId/deviceType
        String[] t = topic.split("/");
        String head = t[0];
        //TODO: validate topics
        if(t.length > 3 && (head.equals("device") || head.equals("healthCheck"))) {
            String token = t[1];
            log.info("head: {}, token: {}, deviceId: {}, deviceType: {}",t[0],t[1],t[2],t[3]);
            UUID deviceId = UUID.fromString(t[2]);
            String deviceType = t[4];
            if (head.equals("device")) {
                handleDevice(token, deviceId,deviceType,payload);
            }
            if (head.equals("healthCheck")) {
                handleHealthCheck(token,deviceId,deviceType,payload);
            }
        }
        log.info("message: {}",payload);
        log.info("message topic: {}", topic);
    }
    private void handleHealthCheck(String token,UUID deviceId, String deviceType, String payload){
        if (deviceType.equals("sensor")){
            sensorService.sensorHealthCheck(deviceId,token);
        }else if (deviceType.equals("operator")){
            operatorService.operatorHealthCheck(deviceId,token, payload);
        }
    }
    private void handleDevice(String token,UUID deviceId, String deviceType, String payload) {
        if(deviceType.equals("sensor")){
            if (sensorService.existHistory(deviceId)){
                SensorHistory lastHistory = sensorService.findLastSensorHistory(deviceId);
                if(!payload.equals(lastHistory.getData())){
                    sensorService.saveSensorHistory(deviceId,token,payload);
                }else {
                    sensorService.changeLastUpdateHistory(lastHistory.getId());
                }
            }else {
                sensorService.saveSensorHistory(deviceId,token,payload);
            }
        }else if (deviceType.equals("operator")){
            Boolean state = payload.equals("1") ? Boolean.TRUE : payload.equals("0") ? false : null;
            if(state != null) {
                operatorService.changeStateByMqtt(deviceId,state);
            }
        }
    }
}
