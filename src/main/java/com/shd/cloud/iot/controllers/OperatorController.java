package com.shd.cloud.iot.controllers;

import javax.validation.Valid;

import com.shd.cloud.iot.dtos.OperatorDto;
import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.sevices.OperatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operator")
public class OperatorController extends handleValidationExceptions {

    @Autowired
    private OperatorService operatorService;

    @PostMapping("")
    public ResponseEntity<?> createOperator(@Valid @RequestBody OperatorDto body) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Operator operator = operatorService.create(body, userDetails.getUsername());
        return ResponseEntity.ok(operator);

    }
}
