package com.shd.cloud.iot.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Document("sensor")
public class SensorHistory {

    @Id
    private String id;
    private String data;
    private Long updated_at;
    private Long sensor_id;


    public SensorHistory(String data, Long updated_at, Long sensor_id) {
        this.data = data;
        this.updated_at = updated_at;
        this.sensor_id = sensor_id;
    }


}
