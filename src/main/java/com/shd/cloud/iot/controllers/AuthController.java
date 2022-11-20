package com.shd.cloud.iot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.CronRequest;
import com.shd.cloud.iot.payload.request.LoginRequest;
import com.shd.cloud.iot.payload.request.SignupRequest;
import com.shd.cloud.iot.payload.response.JwtResponse;
import com.shd.cloud.iot.security.jwt.JwtUtils;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.UserService;
import com.shd.cloud.iot.sevices.scheduler.TaskDefinitionBean;
import com.shd.cloud.iot.sevices.scheduler.TaskSchedulingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends handleValidationExceptions {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final TaskSchedulingService taskSchedulingService;
    private final TaskDefinitionBean taskDefinitionBean;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPhone(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        User user = userService.create(signUpRequest);
        Map<String, Object> res = new HashMap<>();
        res.put("data", user);
        res.put("mesage", "successfully created.");
        res.put("status", 200);
        return ResponseEntity.ok(res);
    }

    @PostMapping(path="/taskdef", consumes = "application/json", produces="application/json")
    public void scheduleATask(@RequestBody CronRequest taskDefinition) {
        taskDefinitionBean.setCronRequest(taskDefinition);
        taskSchedulingService.scheduleATask(UUID.randomUUID().toString(), taskDefinitionBean, taskDefinition.getCron());
    }

    @GetMapping(path="/remove/{jobid}")
    public void removeJob(@PathVariable String jobid) {
        taskSchedulingService.removeScheduledTask(jobid);
    }

}