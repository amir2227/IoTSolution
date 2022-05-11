package com.shd.cloud.iot.controllers;

import com.shd.cloud.iot.exception.handleValidationExceptions;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController extends handleValidationExceptions {
    
    
}
