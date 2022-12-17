package com.shd.cloud.iot.payload.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScenarioResponse {

   private Long id;
   private String description;
   private Boolean isActive;
   private List<ScenarioSensorResponse> effectiveSensors;
   private List<ScenarioOperatorResponse> targetOperators;
}
