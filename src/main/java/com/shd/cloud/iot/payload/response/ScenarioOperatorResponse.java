package com.shd.cloud.iot.payload.response;

import com.shd.cloud.iot.enums.EModality;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Scenario;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
public class ScenarioOperatorResponse {
    private Long id;
    private Long operatorId;
    private Boolean operatorState;
}
