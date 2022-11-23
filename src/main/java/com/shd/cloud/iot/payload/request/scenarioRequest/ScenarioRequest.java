package com.shd.cloud.iot.payload.request.scenarioRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScenarioRequest {
    @Size(max = 255)
    private String description;
    private Boolean isActive;
    private List<ScenarioSensorRequest> scenarioSensors;
    private List<ScenarioOperatorRequest> scenarioOperators;
}
