package com.shd.cloud.iot.controllers;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Scenario;
import com.shd.cloud.iot.payload.request.scenarioRequest.ScenarioRequest;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.payload.response.ScenarioResponse;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.ScenarioService;
import com.shd.cloud.iot.utils.ResponseMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ScenarioController extends handleValidationExceptions {
    private final ScenarioService scenarioService;

    @ApiOperation(value = "create new scenario")
    @PostMapping("")
    public ResponseEntity<ScenarioResponse> createScenario(@Valid @RequestBody ScenarioRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Scenario scenario = scenarioService.create(body, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(scenario));

    }

    @ApiOperation(value = "get all user scenario")
    @GetMapping("")
    public ResponseEntity<SearchResponse> getAll() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(scenarioService.getAll(userDetails.getId())));
    }
    @ApiOperation(value = "edit scenario ")
    @PatchMapping("/{id}")
    public ResponseEntity<ScenarioResponse> add(@PathVariable("id") Long id, @RequestBody ScenarioRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(scenarioService.add(body, id, userDetails.getId())));
    }
    @ApiOperation(value = "get scenario by id")
    @GetMapping("/{id}")
    public ResponseEntity<ScenarioResponse> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ResponseMapper.map(scenarioService.get(id)));
    }
    @ApiOperation(value = "delete scenario by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(scenarioService.delete(id, userDetails.getId())));
    }
    @ApiOperation(value = "delete one sensor modality from scenario")
    @DeleteMapping("/sensor/{id}")
    public ResponseEntity<MessageResponse> deleteSensorScenario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ResponseMapper.map(scenarioService.deleteScnarioSensor(id)));
    }
    @ApiOperation(value = "delete one operator from scenario")
    @DeleteMapping("/operator/{id}")
    public ResponseEntity<MessageResponse> deleteOperatorScenario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ResponseMapper.map(scenarioService.deleteScnarioOperator(id)));
    }
}
