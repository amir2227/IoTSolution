package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.enums.EModality;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ScenarioSensorResponse {
    private Long id;
    private EModality modality;
    private String points;
    private Long sensorId;
    private Long scenarioId;
}
