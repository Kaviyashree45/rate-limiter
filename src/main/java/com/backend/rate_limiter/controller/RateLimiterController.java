package com.backend.rate_limiter.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.rate_limiter.service.RateLimiterService;
import com.backend.rate_limiter.dto.CheckRateLimitRequest;
import com.backend.rate_limiter.dto.CheckRateLimitResponse;
import com.backend.rate_limiter.strategy.TokenBucket;
@RestController
@RequestMapping("/api/v1/rate-limit")
public class RateLimiterController {
    final private RateLimiterService ratelimiterservice;
    public RateLimiterController(RateLimiterService ratelimiterservice){
        this.ratelimiterservice=ratelimiterservice;
        System.out.println(">>> RateLimiterController CREATED <<<");
    }
    @PostMapping("/check")
    public ResponseEntity<CheckRateLimitResponse> checkRateLimit( @RequestBody CheckRateLimitRequest request){
            TokenBucket.RateLimitResult result=ratelimiterservice.checkRequest(request.getClientKey());
                CheckRateLimitResponse response =
            new CheckRateLimitResponse(
                    result.isAllowed(),
                    result.getLimit(),
                    result.getRemaining()
            );
    return ResponseEntity.ok()
            .header(
                    "X-RateLimit-Limit",
                    String.valueOf(result.getLimit())
            )
            .header(
                    "X-RateLimit-Remaining",
                    String.valueOf(result.getRemaining())
            )
            .body(response);
    }
@GetMapping("/ping")
public String ping() {
    return "PING";
}

    
}
