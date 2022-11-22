package com.shd.cloud.iot.controllers;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.payload.response.SensorResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.SensorService;
import com.shd.cloud.iot.utils.ResponseMapper;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "create new sensor")
    @PostMapping
    public ResponseEntity<SensorResponse> createSensor(@Valid @RequestBody SensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(sensorService.create(body, userDetails.getId())));

    }
    @ApiOperation(value = "get all user sensors")
    @GetMapping
    public ResponseEntity<SearchResponse> getAllSensors(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Sensor> sensors = sensorService.getAllByUser(userDetails.getId(), key);
        return ResponseEntity.ok(ResponseMapper.map(sensors));

    }
    @ApiOperation(value = "edit sensor")
    @PatchMapping("/{id}")
    public ResponseEntity<SensorResponse> EditSensor(@PathVariable("id") Long id, @Valid @RequestBody EditSensorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Sensor sensor = sensorService.Edit(body, id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(sensor));

    }
    @ApiOperation(value = "get sensor by id")
    @GetMapping("/{id}")
    public ResponseEntity<SensorResponse> getOneSensor(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(sensorService.getOneByUser(id, userDetails.getId())));
    }

    @ApiOperation(value = "delete sensor by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteSensor(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = sensorService.delete(id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(result));
    }

    @ApiOperation(value = "get all sensor histories")
    @GetMapping("/{id}/history")
    public ResponseEntity<SearchResponse> getSensorHistory(@PathVariable("id") Long id, @Valid SearchRequest sRequest) {
        List<SensorHistory> sh = sensorService.searchHistory(id, sRequest);
        return ResponseEntity.ok(ResponseMapper.map(sh));
    }

    @ApiOperation(value = "sensor health check request")
    @GetMapping("/check")
    public ResponseEntity<MessageResponse> healthCheck(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        sensorService.sensorHealthCheck(userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map("checked"));
    }
}
