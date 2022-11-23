package com.shd.cloud.iot.controllers;

import javax.validation.Valid;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditUserRequest;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.payload.response.UserResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.UserService;

import com.shd.cloud.iot.utils.ResponseMapper;
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
    public ResponseEntity<UserResponse> edit(@Valid @RequestBody EditUserRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userService.Edit(userDetails.getId(), request);
        return ResponseEntity.ok(ResponseMapper.map(user));
    }

    @ApiOperation(value = "user profile")
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> get() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userService.get(userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(user));
    }

    @ApiOperation(value = "get all users only 'ADMIN' role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<SearchResponse> getAll() {
        return ResponseEntity.ok(ResponseMapper.map(userService.search().stream().map(ResponseMapper::map).toList()));
    }

}
