package com.shd.cloud.iot.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LocationRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    @Max(20)
    private String type;

    private Long parent_id;

    public LocationRequest() {
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

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

}
