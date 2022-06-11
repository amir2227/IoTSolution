package com.shd.cloud.iot.payload.request.scenarioRequest;

import com.shd.cloud.iot.models.EModality;
import com.shd.cloud.iot.validator.ValidateString;

public class ScenarioSensorRequest {

    private Long sensor_id;

    private Float[] points;

    @ValidateString(acceptedValues = { "SMALLER", "GREATER", "BETWEEN", "EQUAL" })
    private EModality modality;

    public Long getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(Long sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Float[] getPoints() {
        return points;
    }

    public void setPoints(Float[] points) {
        this.points = points;
    }

    public EModality getModality() {
        return modality;
    }

    public void setModality(EModality modality) {
        this.modality = modality;
    }

}
