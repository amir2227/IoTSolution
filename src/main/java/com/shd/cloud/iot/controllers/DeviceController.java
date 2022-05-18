package com.shd.cloud.iot.controllers;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.mqtt.config.MqttGateway;
import com.shd.cloud.iot.mqtt.model.MqttPublishModel;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/mqtt")
public class DeviceController extends handleValidationExceptions {

    @Resource
    private MqttGateway mqttGateway;

    @PostMapping("publish")
    public ResponseEntity<?> publishMessage(@RequestBody @Valid MqttPublishModel request,
            BindingResult bindingResult) throws MqttException {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("some parameters is invalid!");
        }
        
        mqttGateway.sendToMqtt(request.getTopic(), request.getQos(), request.getMessage());

        return ResponseEntity.ok("successfully published");
    }

}
