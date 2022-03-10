package com.shd.cloud.iot.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OperatorRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    private Boolean state;

    @NotBlank
    @Size(max = 20)
    private String type;

    @Min(1)
    private Long location_id;

    public OperatorRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }

}
