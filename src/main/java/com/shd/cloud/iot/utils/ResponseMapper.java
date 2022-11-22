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

    public static UserDeviceResponse map(List<Sensor> sensors, List<Operator> operators){
        return UserDeviceResponse.builder()
                .sensors(sensors)
                .operators(operators)
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
                .location(sensor.getLocation() != null ? sensor.getLocation().getName() : null)
                .scenarios(sensor.getScenarios())
                .build();
    }
    public static OperatorResponse map(Operator operator){
        return OperatorResponse.builder()
                .id(operator.getId())
                .name(operator.getName())
                .type(operator.getType())
                .state(operator.getState())
                .location(operator.getLocation() != null ? operator.getLocation().getName() : null)
                .scenario_Operators(operator.getScenario_Operators())
                .build();
    }
    public static LocationResponse map(Location location){
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .type(location.getType())
                .parentId(location.getParent() != null ? location.getParent().getId() : null)
                .operators(location.getOperators())
                .sensors(location.getSensors())
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
}
