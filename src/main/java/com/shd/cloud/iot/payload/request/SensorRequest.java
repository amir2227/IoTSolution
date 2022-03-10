package com.shd.cloud.iot.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SensorRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    @Max(20)
    private String type;

    @NotBlank
    private Long user_id;

    private Long location_id;

    public SensorRequest() {
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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }

}
