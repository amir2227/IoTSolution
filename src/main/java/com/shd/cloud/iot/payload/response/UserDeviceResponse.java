package com.shd.cloud.iot.payload.response;

import java.util.List;

import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Sensor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeviceResponse {

    private List<Operator> operators;
    private List<Sensor> sensors;
}
