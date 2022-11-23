package com.shd.cloud.iot.payload.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ScenarioOperatorResponse {
    private Long id;
    private Long operatorId;
    private Long scenarioId;
    private Boolean operatorState;
}
