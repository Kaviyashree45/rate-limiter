package com.backend.rate_limiter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody; 
import com.backend.rate_limiter.service.ClientRateLimitConfigService;
import com.backend.rate_limiter.dto.CreateClientConfigRequest;
import com.backend.rate_limiter.entity.ClientRateLimitConfig;

@RestController
@RequestMapping("/api/v1/admin/clients")

public class ClientRateLimitConfigController {
    private final ClientRateLimitConfigService service;
    public ClientRateLimitConfigController(ClientRateLimitConfigService service){
        this.service=service;
    }

    @PostMapping
    public ClientRateLimitConfig createConfig(@RequestBody CreateClientConfigRequest request){
        return service.createConfig(request);
    }
    
}
