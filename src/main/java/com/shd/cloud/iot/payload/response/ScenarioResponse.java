package com.shd.cloud.iot.payload.response;

import java.util.List;

import com.shd.cloud.iot.models.Scenario;

public class ScenarioResponse {

    private List<Scenario> scenarios;

    private Integer created_count;

    private String message;

    public ScenarioResponse() {
    }

    public ScenarioResponse(List<Scenario> scenarios, Integer created_count, String message) {
        this.scenarios = scenarios;
        this.created_count = created_count;
        this.message = message;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public Integer getCreated_count() {
        return created_count;
    }

    public void setCreated_count(Integer created_count) {
        this.created_count = created_count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
