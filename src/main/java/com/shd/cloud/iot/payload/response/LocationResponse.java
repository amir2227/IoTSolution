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
    private List<Operator> operators;
    private List<Sensor> sensors;
    private Long parentId;
}
