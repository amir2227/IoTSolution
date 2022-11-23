package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.enums.DeviceStatus;
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
    private DeviceStatus status;
    private List<ScenarioOperatorResponse> scenarios;
    private String location;
}
