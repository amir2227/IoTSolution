package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Sensor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LocationResponse {
    private Long id;
    private String name;
    private String type;
    private List<OperatorResponse> operators;
    private List<SensorResponse> sensors;
    private Long parentId;
}
