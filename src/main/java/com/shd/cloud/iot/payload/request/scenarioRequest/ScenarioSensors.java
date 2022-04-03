package com.shd.cloud.iot.payload.request.scenarioRequest;

import com.shd.cloud.iot.models.EModality;
import com.shd.cloud.iot.validator.ValidateString;

public class ScenarioSensors {

    private Long sensor_id;

    private Long operator_id;

    private Float[] points;

    @ValidateString(acceptedValues = { "SMALLER", "GREATER", "BETWEEN", "EQUAL", "ON", "OFF" })
    private EModality modality;

}
