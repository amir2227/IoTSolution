package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.models.ScenarioSensors;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SensorResponse {
    private Long id;
    private String name;
    private String type;
    private List<ScenarioSensors> scenarios;
    private String location;
}
