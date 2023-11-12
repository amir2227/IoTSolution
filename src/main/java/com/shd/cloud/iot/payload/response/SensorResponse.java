package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.enums.DeviceStatus;
import com.shd.cloud.iot.models.ScenarioSensors;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SensorResponse {
    private Long id;
    private String name;
    private String type;
    private DeviceStatus status;
    private UUID deviceId;
    private LocalDateTime createdAt;
    private LocalDateTime lastHealthCheck;
    private List<ScenarioSensorResponse> scenarios;
    private String location;
}
