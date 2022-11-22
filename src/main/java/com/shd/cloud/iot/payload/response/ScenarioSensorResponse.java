package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.enums.EModality;
import com.shd.cloud.iot.models.Sensor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
public class ScenarioSensorResponse {
    private Long id;
    private EModality modality;
    private String points;
    private Long sensorId;

}
