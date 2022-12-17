package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.SensorHistoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttService {
    private final SensorService sensorService;
    private final OperatorService operatorService;

    public void handleMessage(String topic,String payload){
        if (topic.startsWith("sensor/")) {
            try {
                String[] t = topic.split("/");
                List<SensorHistory> sh = sensorService.searchHistory(Long.valueOf(t[1]));
                if (sh.size() > 0) {
                    SensorHistory lastHistory = sh.get(sh.size() -1);
                    String data = lastHistory.getData();
                    log.info("data   --> {}", data);
                    if (!data.equals(payload)) {
                        SensorHistoryRequest sr = new SensorHistoryRequest(payload, t[2]);
                        sensorService.saveSensorHistory(Long.valueOf(t[1]), sr);
                    }else {
                        sensorService.changeLastUpdateHistory(lastHistory.getId());
                    }
                } else {
                    SensorHistoryRequest sr = new SensorHistoryRequest(payload, t[2]);
                    sensorService.saveSensorHistory(Long.valueOf(t[1]), sr);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (topic.startsWith("healthCheck/")){
            try {
                String[] t = topic.split("/");
                operatorService.setHealthCheckDate(Long.valueOf(t[1]),t[2], payload);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        log.info("message: {}",payload);
        log.info("message topic: {}", topic);
    }
}
