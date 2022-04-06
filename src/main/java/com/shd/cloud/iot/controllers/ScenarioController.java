package com.shd.cloud.iot.controllers;

import java.util.List;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Scenario;
// import com.shd.cloud.iot.payload.request.ScenarioRequest;
import com.shd.cloud.iot.payload.response.ScenarioResponse;
import com.shd.cloud.iot.repositorys.ScenarioRepository;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.ScenarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario")
public class ScenarioController extends handleValidationExceptions {

    @Autowired
    private ScenarioService scenarioService;

    // @PostMapping("")
    // public ResponseEntity<?> createScenario(@Valid @RequestBody
    // List<ScenarioRequest> body) {
    // UserDetailsImpl userDetails = (UserDetailsImpl)
    // SecurityContextHolder.getContext().getAuthentication()
    // .getPrincipal();
    // List<Scenario> scenarios = scenarioService.create(body, userDetails.getId());
    // return ResponseEntity.ok(new ScenarioResponse(scenarios, scenarios.size(),
    // "successfully created!"));

    // }

}
