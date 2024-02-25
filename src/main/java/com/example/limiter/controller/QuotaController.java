package com.example.limiter.controller;

import com.example.limiter.service.QuotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/quota")
public class QuotaController {
    private final QuotaService quotaService;

    public QuotaController(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Integer> getConsumerQuota(@PathVariable String userId){
        return new ResponseEntity<>(quotaService.getConsumerQuota(userId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String, Integer>> getUsersQuota(){
        return new ResponseEntity<>(quotaService.getUsersQuota(), HttpStatus.OK);
    }
}
