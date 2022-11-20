package com.shd.cloud.iot.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shd.cloud.iot.models.Plants;
import com.shd.cloud.iot.sevices.PlantsService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PublicController {

    private final PlantsService plantsService;

    @GetMapping("")
    public ResponseEntity<?> search(@RequestParam(name = "key") String key,
            @RequestParam(name = "page") int page) {
        return ResponseEntity.ok(plantsService.search(key, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Plants plants = plantsService.get(id);
        return ResponseEntity.ok(plants);
    }
}
