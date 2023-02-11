package com.shd.cloud.iot.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document("sensor")
public class SensorHistory {

    @Id
    private String id;
    private String data;
    private UUID deviceId;

    private LocalDateTime lastUpdate;

    public SensorHistory(String data, UUID deviceId) {
        this.data = data;
        this.deviceId = deviceId;
        this.lastUpdate = LocalDateTime.now();
    }


}
