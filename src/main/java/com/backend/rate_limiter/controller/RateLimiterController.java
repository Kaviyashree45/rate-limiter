package com.backend.rate_limiter.controller;
import org.springframework.web.bind.annotation.*;
import com.backend.rate_limiter.service.RateLimiterService;
import com.backend.rate_limiter.dto.CheckRateLimitRequest;
import com.backend.rate_limiter.dto.CheckRateLimitResponse;
@RestController
@RequestMapping("/api/v1/rate-limit")
public class RateLimiterController {
    final private RateLimiterService ratelimiterservice;
    public RateLimiterController(RateLimiterService ratelimiterservice){
        this.ratelimiterservice=ratelimiterservice;
    }
    @PostMapping("/check")
    public CheckRateLimitResponse checkRateLimit( @RequestBody CheckRateLimitRequest request){
            System.out.println("Controller reached");
            System.out.println(request.getClientKey());
        boolean isAllowed=ratelimiterservice.checkRequest(request.getClientKey());
        return new CheckRateLimitResponse(isAllowed);
    }


    
}
