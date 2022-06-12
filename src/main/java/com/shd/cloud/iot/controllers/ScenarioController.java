package com.shd.cloud.iot.controllers;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Scenario;
import com.shd.cloud.iot.payload.request.scenarioRequest.ScenarioRequest;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.ScenarioService;
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
@RequestMapping("/api/scenario")
public class ScenarioController extends handleValidationExceptions {

    @Autowired
    private ScenarioService scenarioService;

    @PostMapping("")
    public ResponseEntity<?> createScenario(@Valid @RequestBody ScenarioRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Scenario scenario = scenarioService.create(body, userDetails.getId());
        return ResponseEntity.ok(scenario);

    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(scenarioService.getAll(userDetails.getId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> add(@PathVariable("id") Long id, @RequestBody ScenarioRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(scenarioService.add(body, id, userDetails.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scenarioService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(scenarioService.delete(id, userDetails.getId()));
    }

    @DeleteMapping("/sensor/{id}")
    public ResponseEntity<?> deleteSensorScenario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scenarioService.deleteScnarioSensor(id));
    }

    @DeleteMapping("/operator/{id}")
    public ResponseEntity<?> deleteOperatorScenario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scenarioService.deleteScnarioOperator(id));
    }
}
