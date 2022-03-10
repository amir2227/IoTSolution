package com.shd.cloud.iot.controllers;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.sevices.SensorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/device")
public class DeviceController extends handleValidationExceptions {

    @Autowired
    private OperatorService operatorService;
    @Autowired
    private SensorService sensorService;

    @PostMapping("/operator")
    public ResponseEntity<?> createOperator(@Valid @RequestBody OperatorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.create(body, userDetails.getId());
        return ResponseEntity.ok(operator);

    }

    @PostMapping("/sensor")
    public ResponseEntity<?> createSensor(@Valid @RequestBody SensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Sensor sensor = sensorService.create(body, userDetails.getId());
        return ResponseEntity.ok(sensor);

    }

    @GetMapping("/operator/{id}")
    public ResponseEntity<?> getOneOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        System.out.println(userDetails.getId());
        return ResponseEntity.ok(userDetails);
    }
}
