package com.shd.cloud.iot.payload.response;

import java.util.List;

import com.shd.cloud.iot.models.Scenario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScenarioResponse {

    private List<Scenario> scenarios;
    private Integer created_count;
    private String message;
}
