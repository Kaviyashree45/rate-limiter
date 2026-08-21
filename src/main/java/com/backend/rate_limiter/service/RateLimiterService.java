package com.backend.rate_limiter.service;
import org.springframework.stereotype.Service;
import com.backend.rate_limiter.exception.ClientNotFoundException;
import com.backend.rate_limiter.entity.ClientRateLimitConfig;
import com.backend.rate_limiter.repository.ClientRateLimitConfigRepository;
import com.backend.rate_limiter.strategy.TokenBucket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.backend.rate_limiter.exception.RateLimitExceededException;

@Service
public class RateLimiterService {
   
    private final Map<String,TokenBucket>buckets=new ConcurrentHashMap<>();
    public final ClientRateLimitConfigRepository configRepository;
    public RateLimiterService(ClientRateLimitConfigRepository configRepository){
        this.configRepository=configRepository;
    }
    public TokenBucket.RateLimitResult checkRequest(String clientKey){
        TokenBucket bucket=buckets.computeIfAbsent(clientKey, this::createBucket);
        TokenBucket.RateLimitResult result=bucket.allowRequest();
        if(!result.isAllowed()){
            throw new RateLimitExceededException("Rate limit exceeded for the client Key:"+clientKey,
            bucket.getRetryAfterSeconds());
        }
        return result;

    }
    private TokenBucket createBucket(String clientKey){
        ClientRateLimitConfig config= configRepository.findByClientKey(clientKey)
                                                      .orElseThrow(
                                                        ()->new ClientNotFoundException("Client config not found for key:" + clientKey)
                                                    );
        return new TokenBucket(config.getCapacity(),
                               config.getRefillRate()
                              );
    }
    
}
