package com.shd.cloud.iot.controllers;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.SensorHistoryRequest;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.sevices.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/history")
public class DeviceController extends handleValidationExceptions {

    @Autowired
    private OperatorService operatorService;
    @Autowired
    private SensorService sensorService;

    @PostMapping("sensor/{id}")
    public ResponseEntity<?> createDeviceHistory(@PathVariable("id") Long id,
            @RequestBody SensorHistoryRequest request) {
        SensorHistory sh = sensorService.saveSensorHistory(id, request);

        return ResponseEntity.ok(sh);
    }

    @GetMapping("/operator/{id}/{token}")
    public ResponseEntity<?> getOneOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(operatorService.getOneByUser(id, userDetails.getId()));
    }

}
