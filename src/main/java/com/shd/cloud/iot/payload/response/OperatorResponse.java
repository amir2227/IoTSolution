package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.models.ScenarioOperators;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OperatorResponse {
    private Long id;
    private String name;
    private Boolean state;
    private String type;
    private List<ScenarioOperators> scenario_Operators;
    private String location;
}