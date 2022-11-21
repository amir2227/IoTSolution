package com.shd.cloud.iot.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditSensorRequest {

    @Size(min = 3, max = 20)
    private String name;
    @Size(max = 20)
    private String type;
    private Long location_id;
}
