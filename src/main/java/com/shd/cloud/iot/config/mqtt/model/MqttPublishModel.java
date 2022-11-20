package com.shd.cloud.iot.config.mqtt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MqttPublishModel {
    @NotNull
    @Size(min = 1, max = 255)
    private String topic;
    @NotNull
    @Size(min = 1, max = 255)
    private String message;
    @NotNull
    private Boolean retained;
    @NotNull
    private Integer qos;
}
