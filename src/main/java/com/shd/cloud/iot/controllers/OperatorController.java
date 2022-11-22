package com.shd.cloud.iot.controllers;

import com.shd.cloud.iot.exception.handleValidationExceptions;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.payload.request.EditOperator;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.security.service.UserDetailsImpl;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.utils.ResponseMapper;
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

    @PostMapping
    public ResponseEntity<?> createOperator(@Valid @RequestBody OperatorRequest body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.create(body, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(operator));

    }

    @GetMapping
    public ResponseEntity<?> getAllOperator(@QueryParam("key") String key) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Operator> operators = operatorService.getAllByUser(userDetails.getId(), key);
        return ResponseEntity.ok(ResponseMapper.map(operators));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(ResponseMapper.map(operatorService.getOneByUser(id, userDetails.getId())));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> EditOperator(@PathVariable("id") Long id, @Valid @RequestBody EditOperator body) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Operator operator = operatorService.Edit(body, id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(operator));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOperator(@PathVariable("id") Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String result = operatorService.delete(id, userDetails.getId());
        return ResponseEntity.ok(ResponseMapper.map(result));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getOperatorHistory(@PathVariable("id") Long id, @Valid SearchRequest sRequest) {
        List<OperatorHistory> sh = operatorService.searchHistories(id, sRequest);
        return ResponseEntity.ok(ResponseMapper.map(sh));
    }
    @GetMapping("/check")
    public ResponseEntity<?> healthCheck(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        operatorService.OperatorHealthCheck(userDetails.getId());
        return ResponseEntity.ok("checked");
    }
}
