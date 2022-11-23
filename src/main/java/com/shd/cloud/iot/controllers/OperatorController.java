package com.shd.cloud.iot.controllers;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.payload.request.EditOperator;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.payload.response.OperatorResponse;
import com.shd.cloud.iot.payload.response.SearchResponse;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.utils.ResponseMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/device/operator")
@RequiredArgsConstructor
public class OperatorController extends handleValidationExceptions {
    private final OperatorService operatorService;
    @ApiOperation(value = "create new operator")
    @PostMapping
    public ResponseEntity<OperatorResponse> createOperator(@Valid @RequestBody OperatorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.create(body, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(operator));

    }
    @ApiOperation(value = "get all user operators")
    @GetMapping
    public ResponseEntity<SearchResponse> getAllOperator(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Operator> operators = operatorService.getAllByUser(userDetails.getId(), key);
        return ResponseEntity.ok(ResponseMapper.map(operators.stream().map(ResponseMapper::map).toList()));
    }
    @ApiOperation(value = "get one operator by id")
    @GetMapping("/{id}")
    public ResponseEntity<OperatorResponse> getOneOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(operatorService.getOneByUser(id, userDetails.getId())));
    }
    @ApiOperation(value = "edit operator")
    @PatchMapping("/{id}")
    public ResponseEntity<OperatorResponse> EditOperator(@PathVariable("id") Long id, @Valid @RequestBody EditOperator body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.Edit(body, id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(operator));

    }
    @ApiOperation(value = "delete operator")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = operatorService.delete(id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(result));
    }
    @ApiOperation(value = "search operator history")
    @GetMapping("/{id}/history")
    public ResponseEntity<SearchResponse> getOperatorHistory(@PathVariable("id") Long id, @Valid SearchRequest sRequest) {
        List<OperatorHistory> sh = operatorService.searchHistories(id, sRequest);
        return ResponseEntity.ok(ResponseMapper.map(sh));
    }
    @ApiOperation(value = "operator health check request")
    @GetMapping("/check")
    public ResponseEntity<MessageResponse> healthCheck(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        operatorService.OperatorHealthCheck(userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map("checked"));
    }
}
