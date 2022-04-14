package com.shd.cloud.iot.payload.request.scenarioRequest;

public class ScenarioOperatorRequest {

    private Long operator_id;

    private Boolean operator_state;

    public ScenarioOperatorRequest() {
    }

    public Long getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Long operator_id) {
        this.operator_id = operator_id;
    }

    public Boolean getOperator_state() {
        return operator_state;
    }

    public void setOperator_state(Boolean operator_state) {
        this.operator_state = operator_state;
    }

}
