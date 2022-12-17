package com.shd.cloud.iot.payload.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditLocationRequest {

    @Size(min = 3, max = 20)
    private String name;
    @Max(20)
    private String type;
    private JsonNode geometric;
}
