package com.shd.cloud.iot.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document("sensor")
public class SensorHistory {

    @Id
    private String id;
    private String data;
    private Long sensorId;

    private Date updatedAt;

    private Date lastUpdate;

    public SensorHistory(String data, Long sensor_id) {
        this.data = data;
        this.updatedAt = new Date();
        this.sensorId = sensor_id;
        this.lastUpdate = new Date();
    }


}
