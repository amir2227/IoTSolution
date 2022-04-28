package com.shd.cloud.iot.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

public class EditLocationRequest {

    @Size(min = 3, max = 20)
    private String name;

    @Max(20)
    private String type;

    public EditLocationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
