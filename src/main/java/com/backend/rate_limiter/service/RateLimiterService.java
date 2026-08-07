package com.backend.rate_limiter.service;
import org.springframework.stereotype.Service;

import com.backend.rate_limiter.strategy.TokenBucket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
   
    private final Map<String,TokenBucket>buckets=new ConcurrentHashMap<>();
    public boolean checkRequest(String clientKey){
        TokenBucket bucket=buckets.computeIfAbsent(clientKey,key->new TokenBucket(10,1));
        return bucket.allowRequest();

    }
    
}
