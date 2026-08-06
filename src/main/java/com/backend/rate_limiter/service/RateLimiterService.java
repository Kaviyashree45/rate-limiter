package com.backend.rate_limiter.service;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    public boolean checkRequest(String clientKey){
        return true;
    }
    
}
