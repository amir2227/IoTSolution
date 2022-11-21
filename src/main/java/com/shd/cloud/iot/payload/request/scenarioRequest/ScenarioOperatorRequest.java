package com.shd.cloud.iot.payload.request.scenarioRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScenarioOperatorRequest {
    private Long operator_id;
    private Boolean operator_state;
}
