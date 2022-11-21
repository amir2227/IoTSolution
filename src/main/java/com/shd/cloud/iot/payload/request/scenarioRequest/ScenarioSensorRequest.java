package com.shd.cloud.iot.payload.request.scenarioRequest;

import com.shd.cloud.iot.enums.EModality;
import com.shd.cloud.iot.validator.ValidateString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScenarioSensorRequest {
    private Long sensor_id;
    private Float[] points;
    @ValidateString(acceptedValues = { "SMALLER", "GREATER", "BETWEEN", "EQUAL" })
    private EModality modality;

}
