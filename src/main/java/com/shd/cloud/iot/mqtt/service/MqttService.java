package com.shd.cloud.iot.mqtt.service;

import com.shd.cloud.iot.mqtt.config.Mqtt;
import com.shd.cloud.iot.mqtt.model.MqttPublishModel;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    public void publishMessage(MqttPublishModel request) throws MqttException {
        // if (bindingResult.hasErrors()) {
        // throw new BadRequestException("some parameters is invalid!");
        // }
        MqttMessage mqttMessage = new MqttMessage(request.getMessage().getBytes());
        mqttMessage.setQos(request.getQos());
        mqttMessage.setRetained(request.getRetained());

        Mqtt.getInstance().publish(request.getTopic(), mqttMessage);
    }
}
