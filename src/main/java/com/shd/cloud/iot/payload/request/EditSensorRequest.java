package com.shd.cloud.iot.payload.request;

import javax.validation.constraints.Size;

public class EditSensorRequest {

    @Size(min = 3, max = 20)
    private String name;

    @Size(max = 20)
    private String type;

    private Long location_id;

    public EditSensorRequest() {
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

    public Long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Long location_id) {
        this.location_id = location_id;
    }

}
