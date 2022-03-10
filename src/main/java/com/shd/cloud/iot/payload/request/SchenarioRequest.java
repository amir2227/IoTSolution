package com.shd.cloud.iot.payload.request;

import com.shd.cloud.iot.models.EConditions;
import com.shd.cloud.iot.validator.ValidateString;

public class SchenarioRequest {
    // if sensor_id smaller than 50 then operator_id must be on

    private Long sensor_id;

    private Long operator_id;

    @ValidateString(acceptedValues = { "SMALLER", "GREATER", "BETWEEN", "EQUAL" })
    private EConditions condition;

    private Float[] points;

    private Boolean operator_state;

    public SchenarioRequest() {
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

    public EConditions getCondition() {
        return condition;
    }

    public void setCondition(EConditions condition) {
        this.condition = condition;
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
