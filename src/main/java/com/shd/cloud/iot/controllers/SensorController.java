package com.shd.cloud.iot.controllers;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.SensorService;
import com.shd.cloud.iot.utils.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/device/sensor")
@RequiredArgsConstructor
public class SensorController extends handleValidationExceptions {

    private final SensorService sensorService;


    @PostMapping
    public ResponseEntity<?> createSensor(@Valid @RequestBody SensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(sensorService.create(body, userDetails.getId())));

    }

    @GetMapping
    public ResponseEntity<?> getAllSensors(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Sensor> sensors = sensorService.getAllByUser(userDetails.getId(), key);
        return ResponseEntity.ok(ResponseMapper.map(sensors));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> EditSensor(@PathVariable("id") Long id, @Valid @RequestBody EditSensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Sensor sensor = sensorService.Edit(body, id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(sensor));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneSensor(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(sensorService.getOneByUser(id, userDetails.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSensor(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = sensorService.delete(id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(result));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getSensorHistory(@PathVariable("id") Long id, @Valid SearchRequest sRequest) {
        List<SensorHistory> sh = sensorService.searchHistory(id, sRequest);
        return ResponseEntity.ok(ResponseMapper.map(sh));
    }

    @GetMapping("/check")
    public ResponseEntity<?> healthCheck(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        sensorService.sensorHealthCheck(userDetails.getId());
        return ResponseEntity.ok("checked");
    }
}
