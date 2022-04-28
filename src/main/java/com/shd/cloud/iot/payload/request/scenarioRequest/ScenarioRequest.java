package com.shd.cloud.iot.payload.request.scenarioRequest;

import java.util.List;

public class ScenarioRequest {

    private String name;

    private List<ScenarioSensorRequest> scenarioSensors;

    private List<ScenarioOperatorRequest> scenarioOperators;

    public ScenarioRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScenarioSensorRequest> getScenarioSensors() {
        return scenarioSensors;
    }

    public void setScenarioSensors(List<ScenarioSensorRequest> scenarioSensors) {
        this.scenarioSensors = scenarioSensors;
    }

    public List<ScenarioOperatorRequest> getScenarioOperators() {
        return scenarioOperators;
    }

    public void setScenarioOperators(List<ScenarioOperatorRequest> scenarioOperators) {
        this.scenarioOperators = scenarioOperators;
    }

}
