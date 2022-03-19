package com.shd.cloud.iot.controllers;

import java.util.List;

import javax.validation.Valid;
import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.EditOperator;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.payload.response.UserDeviceResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.sevices.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/device")
public class UserDeviceController extends handleValidationExceptions {

    @Autowired
    private OperatorService operatorService;
    @Autowired
    private SensorService sensorService;

    @GetMapping("")
    public ResponseEntity<?> getUserDevices() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Operator> operators = operatorService.getAllByUser(Long.valueOf(userDetails.getId()));
        System.out.println(operators);
        List<Sensor> sensors = sensorService.getAllByUser(userDetails.getId());

        return ResponseEntity.ok(new UserDeviceResponse(operators, sensors));
    }

    @PostMapping("/operator")
    public ResponseEntity<?> createOperator(@Valid @RequestBody OperatorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.create(body, userDetails.getId());
        return ResponseEntity.ok(operator);

    }

    @GetMapping("/operator")
    public ResponseEntity<?> getAllOperator() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Operator> operators = operatorService.getAllByUser(userDetails.getId());
        return ResponseEntity.ok(operators);

    }

    @GetMapping("/operator/{id}")
    public ResponseEntity<?> getOneOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(operatorService.getOneByUser(id, userDetails.getId()));
    }

    @PatchMapping("/operator/{id}")
    public ResponseEntity<?> EditOperator(@PathVariable("id") Long id, @Valid @RequestBody EditOperator body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.Edit(body, id, userDetails.getId());
        return ResponseEntity.ok(operator);

    }

    @DeleteMapping("/operator/{id}")
    public ResponseEntity<?> deleteOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = operatorService.delete(id, userDetails.getId());
        return ResponseEntity.ok(new MessageResponse(result));
    }

    @GetMapping("/operator/{id}/history")
    public ResponseEntity<?> getOperatorHistory(@PathVariable("id") Long id, @Valid SearchRequest sRequest) {
        List<OperatorHistory> sh = operatorService.searchHistories(id, sRequest);
        return ResponseEntity.ok(new SearchResponse(sh, sh.size()));
    }

    @PostMapping("/sensor")
    public ResponseEntity<?> createSensor(@Valid @RequestBody SensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(sensorService.create(body, userDetails.getId()));

    }

    @GetMapping("/sensor")
    public ResponseEntity<?> getAllSensors() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Sensor> sensors = sensorService.getAllByUser(userDetails.getId());
        return ResponseEntity.ok(sensors);

    }

    @PatchMapping("/sensor/{id}")
    public ResponseEntity<?> EditSensor(@PathVariable("id") Long id, @Valid @RequestBody EditSensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Sensor sensor = sensorService.Edit(body, id, userDetails.getId());
        return ResponseEntity.ok(sensor);

    }

    @GetMapping("/sensor/{id}")
    public ResponseEntity<?> getOneSensor(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(sensorService.getOneByUser(id, userDetails.getId()));
    }

    @DeleteMapping("/sensor/{id}")
    public ResponseEntity<?> deleteSensor(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = sensorService.delete(id, userDetails.getId());
        return ResponseEntity.ok(new MessageResponse(result));
    }

    @GetMapping("/sensor/{id}/history")
    public ResponseEntity<?> getSensorHistory(@PathVariable("id") Long id, @Valid SearchRequest sRequest) {
        List<SensorHistory> sh = sensorService.searchHistory(id, sRequest);
        return ResponseEntity.ok(new SearchResponse(sh, sh.size()));
    }

}
