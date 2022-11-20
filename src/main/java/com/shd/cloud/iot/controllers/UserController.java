package com.shd.cloud.iot.controllers;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditUserRequest;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController extends handleValidationExceptions{
    private final UserService userService;

    @ApiOperation(value = "edit user profile")
    @PatchMapping("")
    public ResponseEntity<?> edit(@Valid @RequestBody EditUserRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User result = userService.Edit(userDetails.getId(), request);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "user profile")
    @GetMapping("/profile")
    public ResponseEntity<?> get() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User result = userService.get(userDetails.getId());
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "get all users only 'ADMIN' role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.search());
    }

}
