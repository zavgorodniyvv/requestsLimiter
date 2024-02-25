package com.example.limiter.controller;

import com.example.limiter.service.QuotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dummy")
public class DummyController {

    private final QuotaService quotaService;

    public DummyController(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @GetMapping()
    public ResponseEntity<String> sendDummyRequest(@RequestParam String userId){
        quotaService.isRequestPermitted(userId);
        return ResponseEntity.ok("Dummy request handled successfully");
    }


}
