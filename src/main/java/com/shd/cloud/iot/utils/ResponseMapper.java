package com.shd.cloud.iot.utils;

import com.shd.cloud.iot.models.*;
import com.shd.cloud.iot.payload.response.*;
import com.shd.cloud.iot.security.service.UserDetailsImpl;

import java.util.List;

public class ResponseMapper {
    public static JwtResponse map(String jwt, UserDetailsImpl userDetails, List<String> roles){
        return JwtResponse.builder()
                .id(userDetails.getId())
                .phone(userDetails.getPhone())
                .username(userDetails.getUsername())
                .accessToken(jwt)
                .roles(roles)
                .type("Bearer")
                .build();
    }

    public static SearchResponse map(List<?> data){
        return SearchResponse.builder()
                .status(200)
                .count(data.size())
                .data(data)
                .build();
    }
    public static MessageResponse map(String message){
        return MessageResponse.builder()
                .message(message)
                .build();
    }
    public static SensorResponse map(Sensor sensor){
        return SensorResponse.builder()
                .id(sensor.getId())
                .name(sensor.getName())
                .type(sensor.getType())
                .status(sensor.getStatus())
                .location(sensor.getLocation() != null ? sensor.getLocation().getName() : null)
                .scenarios(sensor.getScenarios() == null ? null
                        : sensor.getScenarios().stream()
                        .map(scenarioSensors -> ScenarioSensorResponse.builder()
                                .id(scenarioSensors.getId())
                                .points(scenarioSensors.getPoints())
                                .modality(scenarioSensors.getModality())
                                .sensorId(scenarioSensors.getSensor().getId())
                                .scenarioId(scenarioSensors.getScenario().getId())
                                .build()).toList())
                .build();
    }
    public static OperatorResponse map(Operator operator){
        return OperatorResponse.builder()
                .id(operator.getId())
                .name(operator.getName())
                .type(operator.getType())
                .state(operator.getState())
                .location(operator.getLocation() != null ? operator.getLocation().getName() : null)
                .scenarios(operator.getScenario_Operators() == null ? null
                        : operator.getScenario_Operators().stream()
                        .map(scenarioOperators -> ScenarioOperatorResponse.builder()
                                .id(scenarioOperators.getId())
                                .operatorState(scenarioOperators.getOperator_state())
                                .operatorId(scenarioOperators.getOperator().getId())
                                .scenarioId(scenarioOperators.getScenario().getId())
                                .build()).toList())
                .build();
    }
    public static LocationResponse map(Location location){
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .type(location.getType())
                .parentId(location.getParent() != null ? location.getParent().getId() : null)
                .operators(location.getOperators() == null ? null
                        : location.getOperators().stream()
                        .map(operator -> OperatorResponse.builder()
                                .state(operator.getState())
                                .name(operator.getName())
                                .id(operator.getId())
                                .type(operator.getType())
                                .status(operator.getStatus())
                                .build()).toList())
                .sensors(location.getSensors() == null ? null
                        : location.getSensors().stream()
                        .map(sensor -> SensorResponse.builder()
                                .id(sensor.getId())
                                .name(sensor.getName())
                                .type(sensor.getType())
                                .status(sensor.getStatus())
                                .build()).toList())
                .build();
    }
    public static UserResponse map(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .phone(user.getPhone())
                .token(user.getToken())
                .roles(user.getRoles())
                .build();
    }
    public static ScenarioResponse map(Scenario scenario){
        return ScenarioResponse.builder()
                .id(scenario.getId())
                .description(scenario.getDescription())
                .isActive(scenario.getIsActive())
                .effectiveSensors(scenario.getEffective_sensors().stream()
                        .map(scenarioSensor -> ScenarioSensorResponse.builder()
                                .id(scenarioSensor.getId())
                                .modality(scenarioSensor.getModality())
                                .points(scenarioSensor.getPoints())
                                .sensorId(scenarioSensor.getSensor().getId())
                                .build()).toList())
                .targetOperators(scenario.getTarget_operators().stream()
                        .map(scenarioOperator -> ScenarioOperatorResponse.builder()
                                .id(scenarioOperator.getId())
                                .operatorState(scenarioOperator.getOperator_state())
                                .operatorId(scenarioOperator.getOperator().getId())
                                .build()).toList())
                .build();
    }
}
