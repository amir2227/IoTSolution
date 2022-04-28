package com.shd.cloud.iot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.EditLocationRequest;
import com.shd.cloud.iot.payload.request.EditOperator;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.LocationRequest;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.payload.response.UserDeviceResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.LocationService;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.sevices.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/device")
public class UserDeviceController extends handleValidationExceptions {

    @Autowired
    private OperatorService operatorService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private LocationService locationService;

    @GetMapping("")
    public ResponseEntity<?> getUserDevices() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Operator> operators = operatorService.getAllByUser(Long.valueOf(userDetails.getId()), null);
        List<Sensor> sensors = sensorService.getAllByUser(userDetails.getId());

        return ResponseEntity.ok(new UserDeviceResponse(operators, sensors));
    }

    @PostMapping("/operator")
    public ResponseEntity<?> createOperator(@Valid @RequestBody OperatorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.create(body, userDetails.getId());
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("data", operator);
        res.put("mesage", "successfully created.");
        res.put("status", 200);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/operator")
    public ResponseEntity<?> getAllOperator(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Operator> operators = operatorService.getAllByUser(userDetails.getId(), key);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("data", operators);
        res.put("count", operators.size());
        res.put("status", 200);
        return ResponseEntity.ok(res);

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

    @PostMapping("/location")
    public ResponseEntity<?> createLocation(@Valid @RequestBody LocationRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Location location = locationService.create(body, userDetails.getId());
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("data", location);
        res.put("mesage", "successfully created.");
        res.put("status", 200);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/location")
    public ResponseEntity<?> getAllLocation(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Location> locations = locationService.search(userDetails.getId(), key);
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("data", locations);
        res.put("count", locations.size());
        res.put("status", 200);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/location/{id}")
    public ResponseEntity<?> getOneLocation(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(locationService.getByUser(id, userDetails.getId()));
    }

    @PatchMapping("/location/{id}")
    public ResponseEntity<?> EditLocation(@PathVariable("id") Long id, @Valid @RequestBody EditLocationRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Location location = locationService.edit(id, userDetails.getId(), body);
        return ResponseEntity.ok(location);

    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = locationService.delete(id, userDetails.getId());
        return ResponseEntity.ok(new MessageResponse(result));
    }
}
