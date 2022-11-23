package com.shd.cloud.iot.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.payload.request.EditLocationRequest;
import com.shd.cloud.iot.payload.request.LocationRequest;
import com.shd.cloud.iot.payload.response.LocationResponse;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.LocationService;
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
@RequestMapping("/api/device/location")
@RequiredArgsConstructor
public class LocationController extends handleValidationExceptions {

    private final LocationService locationService;

    @ApiOperation(value = "create new location")
    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Location location = locationService.create(body, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(location));

    }
    @ApiOperation(value = "search location")
    @GetMapping
    public ResponseEntity<SearchResponse> getAllLocation(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Location> locations = locationService.search(userDetails.getId(), key);
        return ResponseEntity.ok(ResponseMapper.map(locations.stream().map(ResponseMapper::map).toList()));
    }
    @ApiOperation(value = "get one location by id")
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getOneLocation(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(ResponseMapper.map(locationService.getByUser(id, userDetails.getId())));
    }
    @ApiOperation(value = "edit location")
    @PatchMapping("/{id}")
    public ResponseEntity<LocationResponse> EditLocation(@PathVariable("id") Long id, @Valid @RequestBody EditLocationRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Location location = locationService.edit(id, userDetails.getId(), body);
        return ResponseEntity.ok(ResponseMapper.map(location));

    }
    @ApiOperation(value = "delete location by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteLocation(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = locationService.delete(id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(result));
    }
}
