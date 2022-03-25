package com.shd.cloud.iot.payload.request;

import com.shd.cloud.iot.models.EModality;
import com.shd.cloud.iot.validator.ValidateString;

public class ScenarioRequest {
    // if sensor smaller than 50 then operator must be on

    private Long sensor_id;

    private Long operator_id;

    private Long operator2_id;

    @ValidateString(acceptedValues = { "SMALLER", "GREATER", "BETWEEN", "EQUAL", "ON", "OFF" })
    private EModality modality;

    private Float[] points;

    private Boolean operator_state;

    public ScenarioRequest() {
    }

    public Long getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(Long sensor_id) {
        this.sensor_id = sensor_id;
    }

    public Long getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Long operator_id) {
        this.operator_id = operator_id;
    }

    public Long getOperator2_id() {
        return operator2_id;
    }

    public void setOperator2_id(Long operator2_id) {
        this.operator2_id = operator2_id;
    }

    public EModality getModality() {
        return modality;
    }

    public void setModality(EModality modality) {
        this.modality = modality;
    }

    public Float[] getPoints() {
        return points;
    }

    public void setPoints(Float[] points) {
        this.points = points;
    }

    public Boolean getOperator_state() {
        return operator_state;
    }

    public void setOperator_state(Boolean operator_state) {
        this.operator_state = operator_state;
    }

}
