package com.shd.cloud.iot.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditOperator {

    @Size(min = 3, max = 20)
    private String name;
    private Boolean state;
    @Size(max = 20)
    private String type;
    @Min(1)
    private Long location_id;
}
