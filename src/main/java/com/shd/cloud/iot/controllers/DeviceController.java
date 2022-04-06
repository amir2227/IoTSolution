package com.shd.cloud.iot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.mqtt.config.Mqtt;
import com.shd.cloud.iot.mqtt.model.MqttPublishModel;
import com.shd.cloud.iot.mqtt.model.MqttSubscribeModel;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.sevices.SensorService;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/mqtt")
public class DeviceController extends handleValidationExceptions {

    @PostMapping("publish")
    public ResponseEntity<?> publishMessage(@RequestBody @Valid MqttPublishModel request,
            BindingResult bindingResult) throws MqttException {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("some parameters is invalid!");
        }
        MqttMessage mqttMessage = new MqttMessage(request.getMessage().getBytes());
        mqttMessage.setQos(request.getQos());
        mqttMessage.setRetained(request.getRetained());

        Mqtt.getInstance().publish(request.getTopic(), mqttMessage);

        return ResponseEntity.ok("successfully published");
    }

    @GetMapping("/subscribe/{topic}/{wait_millis}")
    public ResponseEntity<?> subscribeChannel(@PathVariable("topic") String topic,
            @PathVariable("wait_millis") Integer wait_millis) throws InterruptedException, MqttException {
        List<MqttSubscribeModel> messages = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Mqtt.getInstance().subscribeWithResponse(topic, (s, mqttMessage) -> {
            MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel(
                    new String(mqttMessage.getPayload()), mqttMessage.getQos(), mqttMessage.getId());
            messages.add(mqttSubscribeModel);
            countDownLatch.countDown();
        });
        countDownLatch.await(wait_millis, TimeUnit.MILLISECONDS);
        return ResponseEntity.ok(messages);
    }

}
