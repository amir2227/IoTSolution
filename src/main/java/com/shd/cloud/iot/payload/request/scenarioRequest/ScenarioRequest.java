package com.shd.cloud.iot.payload.request.scenarioRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScenarioRequest {
    private String description;
    private List<ScenarioSensorRequest> scenarioSensors;
    private List<ScenarioOperatorRequest> scenarioOperators;
}
